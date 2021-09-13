package pl.czytajto;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

@SpringBootApplication
public class BooksInfoApplication {

	public static void main(String[] args) {
		SpringApplication.run(BooksInfoApplication.class, args);
			
	}

}
