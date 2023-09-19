package me.toddydev;

import com.google.gson.Gson;
import lombok.Getter;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class Init {

	@Getter
	private static Gson gson = new Gson();

	public static void main(String[] args) {
		SpringApplication.run(Init.class, args);
	}

}
