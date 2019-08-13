package websocket.application;

import reactor.core.publisher.UnicastProcessor;
import websocket.domain.Message;

import java.util.Optional;

public class WebSocketMessageSubscriber {
    private UnicastProcessor<Message> messagePublisher;
    private Optional<Message> lastReceivedMessage = Optional.empty();

    public WebSocketMessageSubscriber(UnicastProcessor<Message> messagePublisher) {
        this.messagePublisher = messagePublisher;
    }

    public void onNext(Message message) {
        lastReceivedMessage = Optional.of(message);
        messagePublisher.onNext(message);
    }

    public void onError(Throwable error) {
        error.printStackTrace();
    }

    public void onComplete() {
        lastReceivedMessage.ifPresent(messagePublisher::onNext);
    }
}
