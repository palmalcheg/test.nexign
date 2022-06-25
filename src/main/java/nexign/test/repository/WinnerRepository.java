package nexign.test.repository;

import org.springframework.data.r2dbc.repository.R2dbcRepository;

import nexign.test.entity.Winner;

public interface WinnerRepository extends R2dbcRepository<Winner, Long> {
	
}
