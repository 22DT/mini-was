package v5;

import v2.HttpParser;
import v2.HttpRequest;
import v2.HttpResponse;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.StringReader;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.*;
import java.util.Iterator;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class NioHttpServerWithWorkers {
    private static final int PORT=8080;
    private static final int BUFFER_SIZE=1024;
    private static final int WORKER_POOL_SIZE=4;

    public static void main(String[] args) throws IOException {
        Selector selector = Selector.open();
        ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();

        serverSocketChannel.bind(new InetSocketAddress(PORT));
        serverSocketChannel.configureBlocking(false);
        serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);

        ExecutorService workerPool = Executors.newFixedThreadPool(WORKER_POOL_SIZE);
        System.out.println("Tomcat-style NIO server started on port: " + PORT);

        while(true){
            selector.select();
            Set<SelectionKey> keys = selector.selectedKeys();
            Iterator<SelectionKey> keyIterator = keys.iterator();

            while(keyIterator.hasNext()){
                SelectionKey key = keyIterator.next();

                if(key.isAcceptable()){
                    handleAccept(key);
                }
                else if(key.isReadable()){
                    // 중복 처리 방지
                    key.interestOps(0);

                    workerPool.submit(()->{
                        try {
                            handleRead(key);
                        }catch (IOException e){
                            e.printStackTrace();
                            closeChannel(key.channel());
                        }
                    });
                }
            }

            keyIterator.remove();
        }


    }

    private static void handleAccept(SelectionKey key) throws IOException {
        ServerSocketChannel serverChannel = (ServerSocketChannel) key.channel();
        SocketChannel clientChannel = serverChannel.accept();

        clientChannel.configureBlocking(false);
        clientChannel.register(key.selector(), SelectionKey.OP_READ);
        System.out.println("New connection from " + clientChannel.getRemoteAddress());
    }

    private static void handleRead(SelectionKey key) throws IOException {
        SocketChannel clientChannel = (SocketChannel) key.channel();
        ByteBuffer buffer = ByteBuffer.allocate(BUFFER_SIZE);

        int bytesRead = clientChannel.read(buffer);

        if(bytesRead == -1){
            clientChannel.close();
            System.out.println("Connection closed by client");
            return;
        }

        buffer.flip();
        byte[] data=new byte[buffer.limit()];
        buffer.get(data);

        String rawRequest = new String(data);
        BufferedReader reader = new BufferedReader(new StringReader(rawRequest));

        HttpRequest request;
        try {
            request = HttpParser.parseRequest(reader);
        } catch (IOException e) {
            System.out.println("Failed to parse HTTP request: " + e.getMessage());
            clientChannel.close();
            return;
        }

        System.out.println("Parsed Request:\n" + request);

        // 요청 처리 로직 (간단히 URI 반영한 응답)
        String body = "You requested " + request.uri;
        HttpResponse response = new HttpResponse(200, "OK", body);

        // 응답 직렬화 후 전송
        ByteBuffer responseBuffer = ByteBuffer.wrap(response.toHttpString().getBytes());
        while (responseBuffer.hasRemaining()) {
            clientChannel.write(responseBuffer);
        }

        clientChannel.close();
        System.out.println("Responded and closed connection.");

    }

    private static void closeChannel(Channel channel){
        try{
            channel.close();
        }catch (IOException e){
            e.printStackTrace();
        }
    }

}
