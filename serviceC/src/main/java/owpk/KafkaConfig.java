package owpk;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import ru.sparural.KafkaContainerBeanFactory;
import ru.sparural.KafkaProducerImpl;
import ru.sparural.common.KafkaProducer;
import ru.sparural.common.handler.RequestHandler;
import ru.sparural.dto.ResponseDto;

@Configuration
public class KafkaConfig {

    @Value("${sparural.kafka.response.listenerGroup}")
    private String groupId;
    @Value("${sparural.kafka.response.topic}")
    private String serviceResponseTopic;

    @Bean
    public RequestHandler requestHandler(KafkaProducer kafkaProducer) {
        return request -> new ResponseDto("Response from service C: requestData: " + request);
    }

    @Bean
    public KafkaProducer kafkaProducer(KafkaContainerBeanFactory kafkaContainerBeanFactory) {
        return new KafkaProducerImpl(kafkaContainerBeanFactory,
                serviceResponseTopic, groupId);
    }
}
