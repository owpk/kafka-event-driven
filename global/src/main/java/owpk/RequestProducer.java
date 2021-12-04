package owpk;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.web.client.RestTemplate;

import java.util.Random;
import java.util.UUID;

@SpringBootApplication
public class RequestProducer implements CommandLineRunner {


    public static void main(String[] args) {
        SpringApplication.run(RequestProducer.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
        var restTemplate = new RestTemplate();
        var header = new HttpHeaders();
        header.setContentType(MediaType.APPLICATION_JSON);

        var random = new Random();

        for (int i = 0; i < 10000; i++) {
            new Thread(() -> {
                for (int j = 0; j < 2; j++) {
                    var rand = random.nextInt();
                    var httpEntity = new HttpEntity<>(String.format("{\"reqId\" : \"%d\", \"name\" : \"test\", \"value\" : \"test-value\"}", rand), header);
                    var value = restTemplate.exchange("http://localhost:8080/request", HttpMethod.POST, httpEntity, String.class);
                    synchronized (this) {
                        System.out.println("REQUEST UUID: " + rand);
                        System.out.println(value);
                    }
                }
            }).start();
        }
    }
}
