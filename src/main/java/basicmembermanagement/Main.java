package basicmembermanagement;


import com.vaadin.flow.spring.annotation.EnableVaadin;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@EnableVaadin({"basicmembermanagement.view"})
@SpringBootApplication
@EnableJpaRepositories
public class Main {

    public static void main(String[] args) {
        SpringApplication.run(Main.class, args);

    }
}
