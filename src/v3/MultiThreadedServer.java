package v3;

import v2.HttpParser;
import v2.HttpRequest;
import v2.HttpResponse;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class MultiThreadedServer {
    public static void main(String[] args) throws IOException, InterruptedException {
        int port=8080;
        ServerSocket serverSocket = new ServerSocket(port);

        System.out.println("Server started on port "+port);

        ExecutorService threadPool = Executors.newFixedThreadPool(10);

        while(true){
            Socket clientSocket = serverSocket.accept();
            System.out.println("new connection from "+clientSocket.getInetAddress());

            threadPool.execute(() -> {
                try {
                    handleClient(clientSocket);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            });
        }
    }


    private static void handleClient(Socket clientSocket) throws InterruptedException {
        long threadId=Thread.currentThread().getId();
        String threadName=Thread.currentThread().getName();



        System.out.printf("[Thread-%d] 요청 시작 from %s%n", threadId, clientSocket.getInetAddress());

        Thread.sleep(new Random().nextInt(1000) + 500);  // 0.5초 ~ 1.5초


        try (
                BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
                BufferedWriter out = new BufferedWriter(new OutputStreamWriter(clientSocket.getOutputStream()))
        ) {
            HttpRequest request = HttpParser.parseRequest(in);
//            System.out.println("Parsed Request:\n" + request);

            HttpResponse response = new HttpResponse(200, "OK", "You requested " + request.uri);
            out.write(response.toHttpString());
            out.flush();


            System.out.printf("[Thread-%d] 응답 완료%n", threadId);

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                clientSocket.close();
                System.out.println("Connection closed.");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
