package jebi.hendardi.restTemplate.client;

import jebi.hendardi.restTemplate.service.UserConsumerService;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

public class RestTemplateJsonListClient {
    public static void main(String[] args) {
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext();
        context.scan("jebi.hendardi");
        context.refresh();

        UserConsumerService userConsumerService = context.getBean(UserConsumerService.class);
        userConsumerService.getUserNames().forEach(System.out::println);

        context.close();
    }
}
