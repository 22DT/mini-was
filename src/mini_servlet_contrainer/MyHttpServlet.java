package mini_servlet_contrainer;

import java.io.IOException;

public abstract class MyHttpServlet {
    public void doGet(HttpRequest request, HttpResponse response)throws IOException {}
    public void doPosts(HttpRequest request, HttpResponse response)throws IOException {}
}
