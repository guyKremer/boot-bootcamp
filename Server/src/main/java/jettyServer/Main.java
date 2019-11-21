package jettyServer;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Random;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.servlet.ServletContainer;

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
