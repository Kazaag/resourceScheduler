package be.kazaag.scheduler;

/**
 * Inteface of the message to send via the gateway.
 */
public interface Message {

    /**
     * Call back method call by the gateway when the message has been processed.
     */
    public void completed();
}
