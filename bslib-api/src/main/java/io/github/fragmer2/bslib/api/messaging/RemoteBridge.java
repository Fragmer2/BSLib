package io.github.fragmer2.bslib.api.messaging;

/**
 * Transport abstraction for future Redis/WebSocket synchronization.
 */
@io.github.fragmer2.bslib.api.annotation.ApiStatus.Experimental(since = "1.0.1", notes = "Transport SPI preview")
public interface RemoteBridge {
    void publish(String channel, String payload);

    void subscribe(String channel, java.util.function.Consumer<String> consumer);

    default boolean isConnected() {
        return true;
    }
}
