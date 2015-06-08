package be.kazaag.scheduler;

import java.util.Optional;

/**
 * Interface for a scheduler queue that can implement different priority strategy.
 * This type of Scheduler Queue handles message that have a group parameter.
 * The implementation shouldn't be thread safe as the Scheduler handle synchronisation
 */
public interface SchedulerQueue {
    /**
     * Add a message in the queue.
     * @param m the message to add.
     */
    void add(GroupMessage m);

    /**
     * Return an optional containing the next message to send if it exists.
     * Otherwise return an empty optional.
     * @return the optional containing the next message to send.
     */
    Optional<GroupMessage> poll();

    /**
     * Cancel all messages belonging to a given group name.
     * All message queued with the given group will be removed from the queue.
     * @param group the group to cancel.
     */
    void cancel(String group);
}
