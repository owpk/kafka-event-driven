package owpk;

import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.header.internals.RecordHeader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.requestreply.ReplyingKafkaTemplate;
import org.springframework.kafka.requestreply.RequestReplyFuture;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.SendTo;

import java.nio.charset.StandardCharsets;
import java.util.UUID;
import java.util.concurrent.ExecutionException;

@SpringBootApplication
@Slf4j
public class DemoApplication {

	public static void main(String[] args) {
		SpringApplication.run(DemoApplication.class, args);
	}

	@Autowired
	private ReplyingKafkaTemplate<String, Request, Response> replyingKafkaTemplate;

	@KafkaListener(topics = "serviceA-request", groupId = "${kafka.group.id}")
	@SendTo
	public Response response(Request request) throws ExecutionException, InterruptedException {
		// Лоигка управления запросом...
		// RequestHandler reqHandler = RequestHandlerFactory.getHandler(req_topic);
		// var response = reqHandler.handleRequest(request);
		log.info("REQ: {} ", request);
		var record = new ProducerRecord<>("serviceB-request", null, UUID.randomUUID().toString(), request);
		record.headers().add(new RecordHeader(KafkaHeaders.REPLY_TOPIC, "serviceB-response".getBytes(StandardCharsets.UTF_8)));
		RequestReplyFuture<String, Request, Response> future = replyingKafkaTemplate.sendAndReceive(record);
		ConsumerRecord<String, Response> response = future.get();
		return response.value();
	}

}
