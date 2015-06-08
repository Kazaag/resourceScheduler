package be.kazaag.scheduler;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.Optional;
import java.util.stream.IntStream;

/**
 * Test the scheduler.
 */
@RunWith(MockitoJUnitRunner.class)
public class SchedulerTest {

    @Spy
    private QueuingGateway gateway = new QueuingGateway();

    @Mock
    private SchedulerQueue queue;


    /**
     * Test the scheduler doesn't send message over its capacity.
     */
    @Test
    public void testScheduleCheckMaxCapacity() {
        Scheduler scheduler = new Scheduler(gateway,queue, 3);

        when(queue.poll()).thenReturn(Optional.of(new MockMessage("hello")));

        IntStream.range(0,10).forEach(i -> scheduler.schedule(new MockMessage("hello")));

        verify(gateway,times(3)).send(any());
        verify(queue,times(3)).poll();
        verify(queue,times(10)).add(any());
    }

    /**
     * Test if the next message is send when a message is completed.
     */
    @Test
    public void testScheduleCheckSendingWhenMessageFinished() {
        Scheduler scheduler = new Scheduler(gateway,queue, 3);

        when(queue.poll()).thenReturn(Optional.of(new MockMessage("hello")));
        IntStream.range(0,10).forEach(i -> scheduler.schedule(new MockMessage("hello")));

        verify(gateway,times(3)).send(any());
        verify(queue,times(3)).poll();
        verify(queue,times(10)).add(any());

        // call completed on the message
        gateway.queue.poll().completed();
        gateway.queue.poll().completed();

        // as queue mock will return a message each time
        verify(gateway,times(5)).send(any());
        verify(queue,times(5)).poll();
    }

    /**
     * Test if some capacity is recovered after the completion of message at a time the queue doesn't provide message.
     */
    @Test
    public void testScheduleCheckSendingWhenSchedulingAfterCompletion() {
        Scheduler scheduler = new Scheduler(gateway,queue, 3);

        when(queue.poll()).thenReturn(Optional.of(new MockMessage("hello")));
        IntStream.range(0,10).forEach(i -> scheduler.schedule(new MockMessage("hello")));

        verify(gateway,times(3)).send(any());
        verify(queue,times(3)).poll();
        verify(queue,times(10)).add(any());

        // make sure there is no more message in the queue
        when(queue.poll()).thenReturn(Optional.empty());

        // call completed on the message but no more message
        gateway.queue.poll().completed();
        gateway.queue.poll().completed();
        gateway.queue.poll().completed();

        // as queue mock will not return a message
        verify(gateway,times(3)).send(any());
        verify(queue,times(6)).poll();

        // re activate queue content
        when(queue.poll()).thenReturn(Optional.of(new MockMessage("hello")));

        IntStream.range(0,10).forEach(
                i -> scheduler.schedule(new MockMessage("hello")));

        // 2 more messages should be send
        verify(gateway,times(6)).send(any());
        verify(queue,times(9)).poll();
        verify(queue,times(20)).add(any());
    }



    /**
     * Test if the cancel order is pass to the queue.
     */
    @Test
    public void testCancel() {
        Scheduler scheduler = new Scheduler(gateway,queue, 1);

        scheduler.cancel("groupName");

        verify(queue).cancel("groupName");
    }
}