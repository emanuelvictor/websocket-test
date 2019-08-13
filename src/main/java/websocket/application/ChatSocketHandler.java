package websocket.application;

import org.springframework.web.reactive.socket.WebSocketHandler;
import org.springframework.web.reactive.socket.WebSocketMessage;
import org.springframework.web.reactive.socket.WebSocketSession;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.publisher.UnicastProcessor;
import websocket.application.JsonConverter;
import websocket.domain.Message;

import java.util.Optional;

public class ChatSocketHandler implements WebSocketHandler {

    private UnicastProcessor<Message> messagePublisher;
    private Flux<String> outputMessages;

    public ChatSocketHandler(UnicastProcessor<Message> messagePublisher, Flux<Message> messages) {
        this.messagePublisher = messagePublisher;
        this.outputMessages = Flux.from(messages).map(new JsonConverter<Message>()::toJSON);
    }

    @Override
    public Mono<Void> handle(WebSocketSession session) {
        WebSocketMessageSubscriber subscriber = new WebSocketMessageSubscriber(messagePublisher);
        session.receive()
                .map(WebSocketMessage::getPayloadAsText)
                .map(s -> new JsonConverter<Message>().toEvent(s, Message.class))
                .subscribe(subscriber::onNext, subscriber::onError, subscriber::onComplete);
        return session.send(outputMessages.map(session::textMessage));
    }

}
