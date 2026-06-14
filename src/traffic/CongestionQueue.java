package traffic;

import java.util.*;

public class CongestionQueue {

    private Queue<CongestionEvent> queue;

    public CongestionQueue() {

        queue = new LinkedList<>();
    }

    public void addEvent(
            CongestionEvent event) {

        queue.offer(event);
    }

    public CongestionEvent processEvent() {

        return queue.poll();
    }

    public boolean isEmpty() {

        return queue.isEmpty();
    }
}