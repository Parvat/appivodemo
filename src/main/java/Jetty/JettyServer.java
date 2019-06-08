package Jetty;

import Servlets.AddDataServlet;
import Servlets.GetDataServlet;
import org.eclipse.jetty.server.Connector;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.ServerConnector;
import org.eclipse.jetty.servlet.ServletContextHandler;

public class JettyServer {
    private static Server server;

    public static void start() {
        try {
            server = new Server();
            ServerConnector connector = new ServerConnector(server);
            connector.setPort(8090);
            server.setConnectors(new Connector[]{connector});
            ServletContextHandler context = new ServletContextHandler(
                    ServletContextHandler.SESSIONS);
            context.setContextPath("/");
            context.addServlet(AddDataServlet.class, "/add");
            context.addServlet(GetDataServlet.class, "/get/");
            server.setHandler(context);
            server.start();
            server.join();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
