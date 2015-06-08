package be.kazaag.scheduler.queue;

import be.kazaag.scheduler.GroupMessage;
import be.kazaag.scheduler.SchedulerQueue;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Scheduler queue that prioritise the group in a first in first out way (first on groups level,
 * then within non empty group).
 */
public class GroupPrioritySchedulerQueue implements SchedulerQueue {

    private List<GroupEntry> groups = new LinkedList<>();
    private Map<String,GroupEntry> groupIndex = new HashMap<>();

    @Override
    public void add(GroupMessage m) {
        GroupEntry entry = groupIndex.get(m.getGroup());

        if(entry == null) {
            entry = new GroupEntry(m.getGroup());
            groupIndex.put(m.getGroup(), entry);
            groups.add(entry);
        }

        entry.queue.add(m);
    }

    @Override
    public Optional<GroupMessage> poll() {

        for(GroupEntry entry: groups) {
            GroupMessage m = entry.queue.poll();
            if(m != null) {
                return Optional.of(m);
            }
        }

        return Optional.empty();
    }

    @Override
    public void cancel(String group) {
        groupIndex.remove(group);
        ListIterator<GroupEntry> li = groups.listIterator();

        while(li.hasNext()) {
            if(li.next().group.equals(group)) {
                li.remove();
            }
        }
    }

    private static class GroupEntry {
        private String group;
        private Queue<GroupMessage> queue = new LinkedList<>();

        private GroupEntry(String group) {
            this.group = group;
        }
    }
}
