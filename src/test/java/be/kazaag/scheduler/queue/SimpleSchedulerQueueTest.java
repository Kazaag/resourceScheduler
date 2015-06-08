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
 * Test the simple scheduler queue.
 */
public class SimpleSchedulerQueueTest {

    /**
     * Test empty queue.
     */
    @Test
    public void testPollEmptyQueue() {
        SchedulerQueue queue = new SimpleSchedulerQueue();

        assertEquals(Optional.empty(), queue.poll());
    }

    /**
     * Test first in first out.
     */
    @Test
    public void testFirstInFirstOutSequence() {

        List<GroupMessage> messages = Arrays.asList(new MockMessage("g1"),
                new MockMessage("g2"),
                new MockMessage("g3"),
                new MockMessage("g1"));

        SchedulerQueue queue = new SimpleSchedulerQueue();

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
     * Test cancel
     */
    @Test
    public void testCancel() throws Exception {

        List<GroupMessage> messages = Arrays.asList(new MockMessage("g1"),
                new MockMessage("g2"),
                new MockMessage("g3"),
                new MockMessage("g2"),
                new MockMessage("g1"),
                new MockMessage("g2"),
                new MockMessage("g5"));

        List<GroupMessage> expected = Arrays.asList(messages.get(0),
                messages.get(2),
                messages.get(4),
                messages.get(6));

        SchedulerQueue queue = new SimpleSchedulerQueue();

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