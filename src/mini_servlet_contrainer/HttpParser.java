package mini_servlet_contrainer;

import java.io.BufferedReader;
import java.io.IOException;


public class HttpParser {
    public static HttpRequest parseRequest(BufferedReader reader) throws IOException{
        HttpRequest request = new HttpRequest();

        String requestLine = reader.readLine();
        if(requestLine ==null || requestLine.isEmpty()){
            throw new IOException("Empty request line");
        }

        String[] parts=requestLine.split(" ");
        if(parts.length!=3){
            throw new IOException("Invalid request line: "+requestLine);
        }

        request.method=parts[0];
        request.uri=parts[1];
        request.body=parts[2];

        // Parse headers
        String line;
        while((line=reader.readLine())!=null && !line.isEmpty()){
            int colonIndex=line.indexOf(":");

            if(colonIndex!=-1){
                String name=line.substring(0, colonIndex).trim();
                String value=line.substring(colonIndex+1).trim();
                request.headers.put(name, value);
            }
        }

        // Read body if present
        if("POST".equalsIgnoreCase(request.method)){
            String contentLengthStr = request.headers.get("Content-Type");
            if(contentLengthStr!=null){
                int contentLength = Integer.parseInt(contentLengthStr);
                char[] bodyChars = new char[contentLength];
                reader.read(bodyChars, 0, contentLength);
                request.body=new String(bodyChars);
            }
        }

        return request;
    }
}
