package jettyServer;
import com.google.inject.Guice;
import com.google.inject.Injector;
import io.logz.guice.jersey.JerseyServer;


class Main {


    public static void main(String[] args) throws Exception{
        ResourceConfig config = new ResourceConfig();
        config.packages("jettyServer");
        ServletHolder servlet = new ServletHolder(new ServletContainer(config));


        Server server = new Server(8001);
        ServletContextHandler context = new ServletContextHandler(server, "/*");
        context.addServlet(servlet, "/*");


        try {
            server.start();
            server.join();
        }
        finally {
            server.destroy();
        }
    }
//
//    public static void main(String[] args) throws Exception {
//
//        HttpServer server = HttpServer.create(new InetSocketAddress(8001), 0);
//        MyHandler myHandle= new MyHandler();
//        server.createContext("/boot-bootcamp", myHandle);
//        server.setExecutor(null); // creates a default executor
//        server.start();
//    }
//
//    static class MyHandler implements HttpHandler {
//        private int i =0;
//        private Double containerKey = Math.random();
//        @Override
//        public void handle(HttpExchange t) throws IOException {
//            Logger logger = LogManager.getLogger(MyHandler.class);
//            logger.warn("boot boot "+(++i) +"   "+containerKey.toString());
//            String response = containerKey.toString();
//            t.sendResponseHeaders(200, response.length());
//            OutputStream os = t.getResponseBody();
//            os.write(response.getBytes());
//            os.close();
//        }
//    }
}
