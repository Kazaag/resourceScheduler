package be.kazaag.scheduler;

/**
 * The capacity aware scheduler.
 * The capacity and the scheduler queue are constructor parameter to allow to customise the capacity and
 * the scheduling priority strategy.
 * This scheduler is thread safe.
 */
public class Scheduler {

    private final Gateway gateway;
    private final SchedulerQueue queue;
    private final int capacity;
    private int usage;

    /**
     * The constructor taking the Gateway, the SchedulerQueue and the capacity.
     * @param gateway the gateway to send message to.
     * @param queue the scheduler queue.
     * @param capacity the capacity.
     */
    public Scheduler(Gateway gateway, SchedulerQueue queue, int capacity){
        this.gateway = gateway;
        this.queue = queue;
        this.capacity = capacity;
        this.usage = 0;
    }

    /**
     * Schedule new message.
     * @param m the message to schedule.
     */
    public synchronized void schedule(GroupMessage m){
        queue.add(m);
        checkCapacity();
    }

    /**
     * Cancel all messages belonging to a given group name.
     * All message queued with the given group will be removed from the queue.
     * @param group the group to cancel.
     */
    public synchronized void cancel(String group) {
        queue.cancel(group);
    }

    /**
     * Method call when a message has been completed.
     */
    private synchronized void releaseCapacity() {
        usage--;
        checkCapacity();
    }

    /**
     * Check the capacity and send a message if the capacity allow it.
     */
    private void checkCapacity() {
        if (usage < capacity) {
            queue.poll().ifPresent(this::send);
        }
    }

    /**
     * Send a message an increase the current usage.
     * @param groupMessage the message to send.
     */
    private void send(GroupMessage groupMessage) {
        usage++;
        gateway.send(newWrappedMessage(groupMessage));
    }

    private WrappedMessage newWrappedMessage(GroupMessage message) {
        return this.new WrappedMessage(message);
    }


    /**
     * Wrapper of Group message that add the completed call back needed by the gateway.
     */
    private class WrappedMessage implements GroupMessage,Message {
        private final GroupMessage wrapped;

        private WrappedMessage(GroupMessage message) {
            this.wrapped = message;
        }

        @Override
        public void completed() {
            Scheduler.this.releaseCapacity();
        }

        @Override
        public String getGroup() {
            return wrapped.getGroup();
        }
    }


}
