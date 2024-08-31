package lk.nibm.eventservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.Query;

@SpringBootApplication
public class EventServiceApplication {
	@Query
	public static void main(String[] args) {
		SpringApplication.run(EventServiceApplication.class, args);
	}

}