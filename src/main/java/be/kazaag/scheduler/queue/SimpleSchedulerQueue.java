package be.kazaag.scheduler.queue;

import be.kazaag.scheduler.GroupMessage;
import be.kazaag.scheduler.SchedulerQueue;

import java.util.LinkedList;
import java.util.ListIterator;
import java.util.Optional;
import java.util.Queue;
import java.util.stream.Collectors;

/**
 * Simple first in first out scheduler queue.
 */
public class SimpleSchedulerQueue implements SchedulerQueue {

    private LinkedList<GroupMessage> queue = new LinkedList<>();


    @Override
    public void add(GroupMessage m) {
        queue.add(m);
    }

    @Override
    public Optional<GroupMessage> poll() {
        return Optional.ofNullable(queue.poll());
    }

    @Override
    public void cancel(String group) {
        ListIterator<GroupMessage> li = queue.listIterator();

        while(li.hasNext()) {
            if(li.next().getGroup().equals(group)) {
                li.remove();
            }
        }
    }
}
