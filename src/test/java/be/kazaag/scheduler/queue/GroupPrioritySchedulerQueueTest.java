package be.kazaag.scheduler.queue;


import be.kazaag.scheduler.GroupMessage;
import be.kazaag.scheduler.MockMessage;
import be.kazaag.scheduler.SchedulerQueue;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

/**
 * Test the group priority scheduler queue.
 */
public class GroupPrioritySchedulerQueueTest {

    /**
     * Test empty queue.
     */
    @Test
    public void testPollEmptyQueue() {
        SchedulerQueue queue = new GroupPrioritySchedulerQueue();

        assertEquals(Optional.empty(), queue.poll());
    }

    /**
     * Test all different group.
     */
    @Test
    public void testAllDifferentGroupSequence() {

        List<GroupMessage> messages = Arrays.asList(new MockMessage("g1"),
                new MockMessage("g2"),
                new MockMessage("g3"),
                new MockMessage("g5"));

        SchedulerQueue queue = new GroupPrioritySchedulerQueue();

        messages.stream().forEach(queue::add);

        List<GroupMessage> result = new ArrayList<>();
        Optional<GroupMessage> m;
        do {
            m = queue.poll();
            result.add(m.orElse(null));
        } while (m.isPresent());

        assertEquals(5,result.size());
        for(int i =0; i < 4; i++) {
            assertEquals(messages.get(i),result.get(i));
        }

        assertNull(result.get(4));

    }

    /**
     * Test group priority.
     */
    @Test
    public void testGroupPriority() {

        GroupMessage m1_2 = new MockMessage("g2");
        GroupMessage m2_1 = new MockMessage("g1");
        GroupMessage m3_2 = new MockMessage("g2");
        GroupMessage m4_3 = new MockMessage("g3");

        List<GroupMessage> messages = Arrays.asList(m1_2, m2_1, m3_2, m4_3);

        SchedulerQueue queue = new GroupPrioritySchedulerQueue();

        messages.stream().forEach(queue::add);

        List<GroupMessage> result = new ArrayList<>();
        Optional<GroupMessage> m;
        do {
            m = queue.poll();
            result.add(m.orElse(null));
        } while (m.isPresent());

        assertEquals(5,result.size());

        assertEquals(m1_2, result.get(0));
        assertEquals(m3_2, result.get(1));
        assertEquals(m2_1, result.get(2));
        assertEquals(m4_3, result.get(3));

        assertNull(result.get(4));

    }

    /**
     * Test cancel
     */
    @Test
    public void testCancel() throws Exception {

        List<GroupMessage> messages = Arrays.asList(new MockMessage("g1"),
                new MockMessage("g2"),
                new MockMessage("g3"),
                new MockMessage("g2"),
                new MockMessage("g6"),
                new MockMessage("g2"),
                new MockMessage("g7"));

        List<GroupMessage> expected = Arrays.asList(messages.get(0),
                messages.get(2),
                messages.get(4),
                messages.get(6));

        SchedulerQueue queue = new GroupPrioritySchedulerQueue();

        messages.stream().forEach(queue::add);

        queue.cancel("g2");

        List<GroupMessage> result = new ArrayList<>();
        Optional<GroupMessage> m;
        do {
            m = queue.poll();
            result.add(m.orElse(null));
        } while (m.isPresent());

        assertEquals(5,result.size());
        for(int i =0; i < 4; i++) {
            assertEquals(expected.get(i),result.get(i));
        }

        assertNull(result.get(4));
    }
}