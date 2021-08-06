package br.com.resistenciarebelde.itemsinventario.producers;

import br.com.resistenciarebelde.itemsinventario.model.Item;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ItemEventProducer {

    private final JmsTemplate jmsTemplate;

    @Value("${activemq.resistencia.rebelde.items.queue}")
    private String queueName;

    public void sendEvent(final Item item) {
        jmsTemplate.convertAndSend(queueName, item);
    }
}
