package pl.example.calc;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class CalcServiceApplication {

	public static void main(String[] args) {
		SpringApplication springApplication = new SpringApplication(CalcServiceApplication.class);
//		springApplication.addListeners(new SwaggerConfigResource());
		springApplication.run(args);
	}

}
