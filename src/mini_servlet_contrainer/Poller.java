package mini_servlet_contrainer;

import java.io.IOException;
import java.nio.channels.*;
import java.util.Iterator;
import java.util.Set;
import java.util.concurrent.ExecutorService;

public class Poller implements Runnable{
    private final Selector selector;
    private final PollerQueue pollerQueue;
    private final ExecutorService workerPool;
    private static final int BUFFER_SIZE=1024;

    public Poller(Selector selector, PollerQueue pollerQueue, ExecutorService workerPool) {
        this.selector = selector;
        this.pollerQueue = pollerQueue;
        this.workerPool = workerPool;
    }

    /**
     * pollerQueue -> selector 가 관리하도록
     */
    public void registerNewChannel() throws ClosedChannelException {
        SocketChannel channel;

        while((channel=pollerQueue.poll())!=null){
            channel.register(selector, SelectionKey.OP_READ);
        }
    }

    @Override
    public void run() {
        while(true){
            try{
                registerNewChannel();
                selector.select();

                Set<SelectionKey> selectedKeys = selector.selectedKeys();
                Iterator<SelectionKey> keyIterator = selectedKeys.iterator();


                while(keyIterator.hasNext()){
                    SelectionKey key = keyIterator.next();

                    if(key.isReadable()){
                        key.interestOps(0); // 중복 처리 방지

                        workerPool.submit(()->{
                            try{
                                handleRead(key);
                            }catch(IOException e){
                                e.printStackTrace();
                                close(key.channel());
                            }
                        });
                    }


                    keyIterator.remove();
                }

            } catch (IOException e) {
                e.printStackTrace();
            }



        }
    }

    private void handleRead(SelectionKey key) throws IOException {}


    private void close(Channel channel){}
}
