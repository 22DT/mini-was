package mini_servlet_contrainer;

import java.nio.channels.SocketChannel;
import java.util.concurrent.ConcurrentLinkedQueue;

public class PollerQueue {
    private final ConcurrentLinkedQueue<SocketChannel> queue = new ConcurrentLinkedQueue<>();

    public void offer(SocketChannel channel){
        queue.offer(channel);
    }

    public SocketChannel poll(){
        return queue.poll();
    }

    public boolean isEmpty(){
        return queue.isEmpty();
    }
}
