package v2;

import java.io.BufferedReader;
import java.io.IOException;

public class HttpParser {
    public static HttpRequest parseRequest(BufferedReader in) throws IOException {
        HttpRequest request = new HttpRequest();

        // 요청 줄
        String requestLine=in.readLine();
        if(requestLine==null ||requestLine.isBlank()){
            throw new IOException("Empty request line");
        }

        String[] parts=requestLine.split(" ");
        request.method=parts[0];
        request.uri=parts[1];
        request.version=parts[2];

        // 헤더 파싱
        String line;
        while(!(line=in.readLine()).isBlank()){
            String[] header=line.split(":", 2);
            if(header.length==2){
                request.headers.put(header[0],header[1]);
            }
        }

        // 바디 읽기(Content-Length 기반)
        if(request.headers.containsKey("Content-Length")){
            int length=Integer.parseInt(request.headers.get("Content-Length"));
            char[] bodyChars=new char[length];
            in.read(bodyChars);
            request.body=new String(bodyChars);
        }

        return request;
    }
}
