package mini_servlet_contrainer;


import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;

/**
 * Selector.select()에서 OP_ACCEPT 감지
 */
public class Acceptor implements Runnable  {
    private final ServerSocketChannel serverChannel;
    private final PollerQueue pollerQueue;

    public Acceptor(int port, PollerQueue pollerQueue) throws IOException {
        this.pollerQueue = pollerQueue;
        this.serverChannel=ServerSocketChannel.open();
        serverChannel.bind(new InetSocketAddress(port));
        serverChannel.configureBlocking(true); // acceptor 는 블로킹 모드
    }


    @Override
    public void run(){
        while(true){
            try{
                SocketChannel clientChannel = serverChannel.accept();
                clientChannel.configureBlocking(false);
                System.out.println("Accepted: "+clientChannel.getRemoteAddress());
                pollerQueue.offer(clientChannel);

            }catch(IOException e){
                e.printStackTrace();
            }
        }
    }



}
