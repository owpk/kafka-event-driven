package owpk;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;
import org.springframework.kafka.listener.ConcurrentMessageListenerContainer;
import org.springframework.kafka.requestreply.ReplyingKafkaTemplate;

@Configuration
public class KafkaConfig {

    @Value("${kafka.group.id}")
    private String groupId;

    @Bean
    public ReplyingKafkaTemplate<String, Request, Response> replyingKafkaTemplate(ProducerFactory<String, Request> pf,
                                                                                  ConcurrentKafkaListenerContainerFactory<String, Response> factory) {
        ConcurrentMessageListenerContainer<String, Response> replyContainer = factory.createContainer("serviceA-response");
        replyContainer.getContainerProperties().setMissingTopicsFatal(false);
        replyContainer.getContainerProperties().setGroupId(groupId);
        return new ReplyingKafkaTemplate<>(pf, replyContainer);
    }

    @Bean
    public KafkaTemplate<String, Response> replyTemplate(ProducerFactory<String, Response> pf,
                                                       ConcurrentKafkaListenerContainerFactory<String, Response> factory) {
        KafkaTemplate<String, Response> kafkaTemplate = new KafkaTemplate<>(pf);
        factory.getContainerProperties().setMissingTopicsFatal(false);
        factory.setReplyTemplate(kafkaTemplate);
        return kafkaTemplate;
    }
}
