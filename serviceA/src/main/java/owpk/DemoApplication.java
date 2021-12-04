package owpk;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import ru.sparural.common.KafkaProducer;
import ru.sparural.dto.RequestDto;
import java.util.concurrent.ExecutionException;

@SpringBootApplication
@RestController
@Slf4j
public class DemoApplication {

    @Bean
    public ObjectMapper objectMapper() {
        return new ObjectMapper();
    }

    public static void main(String[] args) {
        SpringApplication.run(DemoApplication.class, args);
    }

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private KafkaProducer producer;

    @PostMapping(value = "/request", produces = "text/plain")
    public String postGetResponse(@RequestBody RequestDto req) throws ExecutionException, InterruptedException, JsonProcessingException {
        log.info("CONTROLLER REQ: {}", req);
        return producer.sendRequest(req, "serviceB.request").getResponseBody();
    }

}