package owpk;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import ru.sparural.KafkaContainerBeanFactory;
import ru.sparural.KafkaProducerImpl;
import ru.sparural.common.KafkaProducer;
import ru.sparural.common.handler.RequestHandler;
import ru.sparural.dto.ResponseDto;

@Configuration
@Import(ru.sparural.KafkaConfigAutoconfiguration.class)
public class KafkaConfig {

    @Value("${sparural.kafka.response.listenerGroup}")
    private String groupId;
    @Value("${sparural.kafka.response.topic}")
    private String serviceResponseTopic;

    @Bean
    public RequestHandler requestHandler() {
        return request -> new ResponseDto("Response A");
    }

    @Bean
    public KafkaProducer kafkaProducer(KafkaContainerBeanFactory kafkaContainerBeanFactory) {
        return new KafkaProducerImpl(kafkaContainerBeanFactory,
                serviceResponseTopic, groupId);
    }
}
