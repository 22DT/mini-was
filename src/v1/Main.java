package v1;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

public class Main {
    public static void main(String[] args) {
        int port = 8080;

        try (ServerSocket serverSocket = new ServerSocket(port)) {
            System.out.println("Server started on port " + port);

            while (true) {
                Socket clientSocket = serverSocket.accept(); // 연결 수락
                System.out.println("New connection from " + clientSocket.getInetAddress());

                handleClient(clientSocket);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void handleClient(Socket clientSocket) {
        try (
                BufferedReader in = new BufferedReader(
                        new InputStreamReader(clientSocket.getInputStream())
                );
                BufferedWriter out = new BufferedWriter(
                        new OutputStreamWriter(clientSocket.getOutputStream())
                )
        ) {
            // 요청 로그 출력
            String line;
            while (!(line = in.readLine()).isBlank()) {
                System.out.println("line: " + line);
            }

            // 간단한 HTTP 응답 작성
            String responseBody = "Hello, world!";
            String httpResponse =
                    "HTTP/1.1 200 OK\r\n" +
                            "Content-Length: " + responseBody.length() + "\r\n" +
                            "Content-Type: text/plain\r\n" +
                            "\r\n" +
                            responseBody;

            out.write(httpResponse);
            out.flush();

            System.out.println("Responded with Hello, world!");

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
