# Comment about the implementation decision.

## Message prioritisation abstraction.
To allow easy change of the prioritisation algorithm, the algorithm and its needed
 data structure is abstracted in a Queue that is responsible to queue message and
 poll the message base on prioritisation algorithm.  As different algorithm will
 perform better with different data structure, it make sence to encapsulate both
 in one class.

## Synchronisation
As the Scheduler is the gate keeper to prevent overloading the 3rd party system,
it should be able to safely be call by several thread (the message completed
call back will most probably call from another thread).  The synchronised section
of the Scheduler will also include the queue operation to prevent the synchronisation
of the queue it self and the lost of performance related to it.  The usage of the
capacity is obviously part of the synchronised section.

If the send to the 3rd party take too much time, we may put the sending out side
of the synchronised section (probably by returning an operation to perform after
the synchronised block).

## Implementation of the group priority scheduler queue.
The queue is using a first in first out queue with an entry per group, base on
the order of the first message of the group.  The entry contains a first in first out
queue for the message with the same group.

To improve addition of a new message, a map of the entry with the group as key is kept
making addition of a message to an existing group linear complexity.  The poll operation
complexity is bound to the number of group (the message queue have constant complexity
for poll).

The groups queue is a linked list to make addition of new group constant and cancellation
of a group linear to the number of group.