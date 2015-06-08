package be.kazaag.scheduler;

/**
 * Created by frank_000 on 08-06-15.
 */
public class MockMessage implements GroupMessage {

    private String group;

    public MockMessage(String group) {
        this.group = group;
    }

    @Override
    public String getGroup() {
        return group;
    }
}
