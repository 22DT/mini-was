package mini_servlet_contrainer;

import java.util.HashMap;
import java.util.Map;

public class HttpRequest {
    public String method;
    public String uri;
    public String version;
    public Map<String, String> headers=new HashMap<>();
    public String body;

    @Override
    public String toString() {
        return method + " " + uri + " " + version + "\n" + headers + "\n\n" + body;
    }
}
