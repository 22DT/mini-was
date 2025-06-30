package mini_servlet_contrainer;

import java.util.LinkedHashMap;
import java.util.Map;

public class HttpResponse {
    private int statusCode=200;
    private String reasonPhrase="OK";
    private final Map<String, String> headers=new LinkedHashMap<String, String>();
    private String body="";

    public void setStatusCode(int statusCode) {
        this.statusCode = statusCode;
    }

    public void setReasonPhrase(String reasonPhrase) {
        this.reasonPhrase = reasonPhrase;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public String toHttpString(){
        headers.put("Content-Length", String.valueOf(body.getBytes().length));
        headers.put("Content-Type", "text/plain; charset=UTF-8");

        StringBuilder sb = new StringBuilder();
        sb.append("HTTP/1.1 ").append(statusCode).append(" ").append(reasonPhrase).append("\r\n");
        headers.forEach((k, v)->sb.append(k).append(": ").append(v).append("\r\n"));
        sb.append("\r\n").append(body);
        return sb.toString();
    }
}
