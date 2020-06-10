package stage.server;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan({ "stage.common", "stage.server" })
public class Main {
    public static void main(String[] args) {
        // ApplicationContext context = SpringApplication(Main.class, args);
        SpringApplication.run(Main.class, args);

        // TODO: replace Main.class by the implementation of JwtUserDatabase once the UserService is done.
        // JwtUserDetailsService.setDatabase(context.getBean(Main.class));
    }
}
