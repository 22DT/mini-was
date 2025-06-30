package mini_servlet_contrainer;

import java.util.HashMap;
import java.util.Map;

public class ServletRegistry {
    private static final Map<String, MyHttpServlet> servletMap = new HashMap<>();

    public static void register(String path, MyHttpServlet servlet){
        servletMap.put(path, servlet);
    }

    public static MyHttpServlet getServlet(String path){
        return servletMap.get(path);
    }
}
