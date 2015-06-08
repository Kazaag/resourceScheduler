package be.kazaag.scheduler;

import java.util.LinkedList;
import java.util.Queue;

/**
 * Created by frank_000 on 08-06-15.
 */
public class QueuingGateway implements Gateway {

    Queue<Message> queue = new LinkedList<>();

    @Override
    public void send(Message msg) {
        queue.add(msg);
    }
}
