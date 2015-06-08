package be.kazaag.scheduler;

/**
 * Interface representing a message within a group.
 */
public interface GroupMessage {

    /**
     * The group the message belongs to.
     * @return the group name.
     */
    String getGroup();

    // other element should be added as the real payload of the message.

}
