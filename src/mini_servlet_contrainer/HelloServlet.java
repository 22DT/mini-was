package mini_servlet_contrainer;


public class HelloServlet extends MyHttpServlet{

    @Override
    public void doGet(HttpRequest request, HttpResponse response){
        response.setStatusCode(200);
        response.setReasonPhrase("OK");
        response.setBody("Hello from HelloServlet!");
    }
}
