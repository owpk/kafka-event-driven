package owpk;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.handler.annotation.SendTo;

import java.util.stream.Collectors;
import java.util.stream.IntStream;

@SpringBootApplication
@Slf4j
public class DemoApplication {

	public static void main(String[] args) {
		SpringApplication.run(DemoApplication.class, args);
	}

	@KafkaListener(topics = "serviceB-request", groupId = "${kafka.group.id}")
	@SendTo
	public Response response(Request request) {
		// Лоигка управления запросом...
		// RequestHandler reqHandler = RequestHandlerFactory.getHandler(req_topic);
		// var response = reqHandler.handleRequest(request);
		log.info("REQ: {} ", request);
		var response = new Response();
		var listObj2 = IntStream.range(0, 3)
				.mapToObj(i -> new InnerResponseObject2("name: " + i))
				.collect(Collectors.toList());
		var listObj1 = IntStream.range(0, 2)
				.mapToObj(i -> new InnerResponseObject1("name: " + i, listObj2))
				.collect(Collectors.toList());
		response.setResponseId(request.getReqId());
		response.setResponseBody(String.format(" request info: %s", request));
		response.setInnerResponseObject1List(listObj1);
		return response;
	}

}
