package test.nexign;

import javax.annotation.Resource;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;

import nexign.test.App;
import nexign.test.entity.Participant;
import nexign.test.repository.ParticipantRepository;
import nexign.test.repository.WinnerRepository;

@SpringBootTest(classes = App.class,  webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class AppTest {
	
	@Resource
	private WebTestClient  wb;
	
	@Resource
	ParticipantRepository participants;
	
	@Resource
	WinnerRepository winners;
	
	@BeforeEach	
	public void cleanup() {
		winners.deleteAll().then(participants.deleteAll()).block();
	}
	
	@Test
	public void testGetAll() {
		wb.get().uri("/lottery/participant").exchange().expectStatus().isOk()
		    .expectBody()
            .jsonPath("$").isArray()
            .jsonPath("$.length()").isEqualTo(0);
	}
	
	@Test
	public void testBasic() {
		Participant p1 = Participant.builder().age(12).city("city1").name("name1").build();
		
		wb.post()
		  .uri("/lottery/participant")
		  .contentType(MediaType.APPLICATION_JSON)
		  .bodyValue(p1)
		  .exchange().expectStatus().isOk();
		
		wb.get().uri("/lottery/participant").exchange().expectStatus().isOk()
		        .expectBody()
		        .jsonPath("$").isArray()
		        .jsonPath("$.length()").isEqualTo(1);
		
		wb.get().uri("/lottery/start").exchange().expectStatus().isBadRequest();
		
		Participant p2 = Participant.builder().age(12).city("city2").name("name2").build();
		
		wb.post()
		  .uri("/lottery/participant")
		  .contentType(MediaType.APPLICATION_JSON)
		  .bodyValue(p2)
		  .exchange().expectStatus().isOk();
		
		wb.get().uri("/lottery/start").exchange().expectStatus().isOk()
		       .expectBody()
		       .jsonPath("$.amount").exists();
		
		wb.get().uri("/lottery/winners").exchange().expectStatus().isOk()
           .expectBody()
           .jsonPath("$").isArray()
           .jsonPath("$.length()").isEqualTo(1);
	}

}
