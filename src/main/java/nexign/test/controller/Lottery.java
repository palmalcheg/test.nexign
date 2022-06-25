package nexign.test.controller;

import org.apache.commons.lang3.RandomUtils;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import nexign.test.entity.Participant;
import nexign.test.entity.Winner;
import nexign.test.repository.ParticipantRepository;
import nexign.test.repository.WinnerRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping(value = "/lottery")
@RequiredArgsConstructor
@Slf4j
public class Lottery {

	private final ParticipantRepository participants;
	private final WinnerRepository winners;

	@PostMapping("/participant")
	public Mono<Participant> create(@RequestBody Participant p) {
		return participants.save(p);
	}
	
	@GetMapping("/participant")
	public Flux<Participant> getAll() {
		return participants.findAll();
	}
	
	@GetMapping("/winners")
	public Flux<Winner> getAllWinners() {
		return winners.findAll();
	}
	
	
	@GetMapping("/start")
	public Mono<ResponseEntity> start() {	
		return participants.count()
		   .filter(c -> !(c < 2) )		 
		   .flatMap(this::winner)
		   .flatMap(w -> { 
		    	  log.info("Step 4: save winner and cleanup participants ");
		    	  return winners.save(w)
		    			    .and(participants.deleteAll())
		    			    .thenReturn(ResponseEntity.ok(w));
		   })
		   .cast(ResponseEntity.class)
		   .defaultIfEmpty(ResponseEntity.badRequest().body("Invalid participant count"));
            
		   
				
	}
	
	private Integer generateRandom(int count) {
		return RandomUtils.nextInt(1, count);
	}
	
	private Mono<Integer> randPosition(int upper) {
		log.info("Step 3: generate random position from (1,{})",upper);
		return Mono.just(generateRandom(upper));
	}
	
	private Mono<Integer> randAmount() {
		log.info("Step 2: generate random win ammount from interval (1,100000)");
		return Mono.just(generateRandom(100000));
	}
	
	private Mono<Winner> winner(Long seed) {		
		return randAmount()
		         .zipWith( getRandomParticipant(seed), (amount, p) -> 
														   Winner.builder()
													        .age(p.getAge())
													        .name(p.getName())
													        .city(p.getCity())
													        .amount(amount)
													      .build());
		}
	
	private Mono<Participant> getRandomParticipant(Long seed) {
		return randPosition(seed.intValue())
		         .flatMap(pos -> participants.getAllIds()
			                    .elementAt(pos - 1)
			                    .flatMap(participants::findById)
			              );
	}
	

}
