package pl.bartlomiejpietrzyk;

import io.swagger.models.Contact;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import static springfox.documentation.builders.PathSelectors.regex;

@EnableSwagger2
@SpringBootApplication

public class DriverApplication {

    public static void main(String[] args) {
        SpringApplication.run(DriverApplication.class, args);
    }

    @Bean
    public Docket newsApi() {
        return new Docket(DocumentationType.SWAGGER_2)
                .groupName("Server API")
                .apiInfo(apiInfo())
                .select()
                .paths(regex("/*"))
                .paths(PathSelectors.any())
                .build();
    }

    private ApiInfo apiInfo() {
        Contact contact = new Contact();
        contact.setName("Bartlomiej Pietrzyk");
        contact.setEmail("kontakt@bartlomiejpietrzyk.com");
        contact.setUrl("http://github.com/bartlomiejpietrzyk");
        return new ApiInfoBuilder()
                .title("Driver application")
                .description("Application for Drivers with tricks&tips")
                .build();
    }
}
