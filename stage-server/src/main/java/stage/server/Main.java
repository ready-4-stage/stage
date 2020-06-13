package stage.server;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import stage.server.authentication.JwtUserDetailsService;
import stage.server.user.UserService;

@SpringBootApplication
@ComponentScan({ "stage.common", "stage.server" })
public class Main {
    public static void main(String[] args) {
        ApplicationContext context = SpringApplication.run(Main.class, args);
        JwtUserDetailsService.setDatabase(context.getBean(UserService.class));
    }
}
