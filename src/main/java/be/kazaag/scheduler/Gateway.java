package be.kazaag.scheduler;

/**
 * Interface of the gateway to a 3rd party system.
 */
public interface Gateway {

    /**
     * Send a message to the 3rd party system via the gateway.
     * @param msg the message to send.
     */
    void send(Message msg);
}
