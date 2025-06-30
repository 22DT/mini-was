package v2;

import java.util.HashMap;
import java.util.Map;

public class HttpResponse {
    private int statusCode;
    private String reasonPhrase;
    private Map<String, String> headers=new HashMap<>();
    private String body="";

    public HttpResponse(int code, String phrase, String body){
        this.statusCode=code;
        this.reasonPhrase=phrase;
        this.body=body;

        headers.put("Content-Length", String.valueOf(body.getBytes().length));
        headers.put("Content-Type", "text/plain");
    }

    public String toHttpString(){
        StringBuilder sb=new StringBuilder();
        sb.append("HTTP/1.1 ").append(statusCode).append(" ").append(reasonPhrase).append("\r\n");

        for(var entry:headers.entrySet()){
            sb.append(entry.getKey()).append(": ").append(entry.getValue()).append("\r\n");
        }
        sb.append("\r\n");
        sb.append(body);

        return sb.toString();
    }



}
