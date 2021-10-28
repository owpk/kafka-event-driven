package owpk;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.header.internals.RecordHeader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.kafka.requestreply.ReplyingKafkaTemplate;
import org.springframework.kafka.requestreply.RequestReplyFuture;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.nio.charset.StandardCharsets;
import java.util.UUID;
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
    private ReplyingKafkaTemplate<String, Request, Response> replyingKafkaTemplate;

    @PostMapping(value = "/request", produces = "text/plain")
    public String postGetResponse(@RequestBody Request req) throws ExecutionException, InterruptedException, JsonProcessingException {
        log.info("CONTROLLER REQ: {}", req);
        var record = new ProducerRecord<>("serviceA-request", null, UUID.randomUUID().toString(), req);
        req.setReqId(record.hashCode());
        record.headers().add(new RecordHeader(KafkaHeaders.REPLY_TOPIC, "serviceA-response".getBytes(StandardCharsets.UTF_8)));
        RequestReplyFuture<String, Request, Response> future = replyingKafkaTemplate.sendAndReceive(record);
        ConsumerRecord<String, Response> response = future.get();
        var result = response.value();
        return objectMapper
                .writerWithDefaultPrettyPrinter()
                .writeValueAsString(response.value());
    }

}