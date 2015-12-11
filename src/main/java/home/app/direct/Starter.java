package home.app.direct;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.web.SpringBootServletInitializer;
import org.springframework.web.WebApplicationInitializer;

@SpringBootApplication
public class Starter  extends SpringBootServletInitializer implements WebApplicationInitializer {
    public static void main(String...args){
        SpringApplication.run(Starter.class);
    }

}
