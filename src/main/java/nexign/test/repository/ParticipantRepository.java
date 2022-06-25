package nexign.test.repository;

import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.r2dbc.repository.R2dbcRepository;

import nexign.test.entity.Participant;
import reactor.core.publisher.Flux;

public interface ParticipantRepository extends R2dbcRepository<Participant, Long> {
	
	@Query("select p.id from participant p")
	Flux<Long> getAllIds();

}
