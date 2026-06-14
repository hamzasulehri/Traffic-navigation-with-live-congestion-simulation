package traffic;

public class CongestionEvent {

    private String type;
    private String source;
    private String destination;

    public CongestionEvent(
            String type,
            String source,
            String destination) {

        this.type = type;
        this.source = source;
        this.destination = destination;
    }

    public String getType() {
        return type;
    }

    public String getSource() {
        return source;
    }

    public String getDestination() {
        return destination;
    }

    @Override
    public String toString() {

        return type
                + " : "
                + source
                + " -> "
                + destination;
    }
}