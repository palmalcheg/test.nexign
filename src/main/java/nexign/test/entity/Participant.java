package nexign.test.entity;

import org.springframework.data.annotation.Id;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

@SuperBuilder(toBuilder = true)
@EqualsAndHashCode
@ToString
@NoArgsConstructor
public class Participant {
	
	@Id
	int id;
	
	@Getter
	private String name;
	
	@Getter
	private String city;
	
	@Getter
	private int age;

}
