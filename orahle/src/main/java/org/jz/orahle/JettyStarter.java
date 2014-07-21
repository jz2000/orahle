package org.jz.orahle;

import org.apache.jasper.servlet.JspServlet;
import org.eclipse.jetty.server.Handler;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.ServerConnector;
import org.eclipse.jetty.server.handler.ContextHandler;
import org.eclipse.jetty.server.handler.DefaultHandler;
import org.eclipse.jetty.server.handler.HandlerCollection;
import org.eclipse.jetty.server.handler.ResourceHandler;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import org.jz.orahle.servlets.DbConnectServlet;
import org.jz.orahle.servlets.DbQueryServlet;

/**
 *
 * @author sergey.zheznyakovskiy
 */
public class JettyStarter 
{
    private Server server;

    public static void main(String[] args) 
    {
            JettyStarter jettyRunner = new JettyStarter();
            jettyRunner.start();
    }

    private void start() 
    {
       try 
       {
           server = new Server();
         
           ServerConnector connector = new ServerConnector(server);
           connector.setPort(9080);
           server.addConnector(connector);
           
           ResourceHandler staticResourceHandler = new ResourceHandler();
           staticResourceHandler.setResourceBase("./src/main/webapp");
           staticResourceHandler.setDirectoriesListed(true);
           
           ContextHandler staticContextHandler = new ContextHandler();
           staticContextHandler.setContextPath("/");
           staticContextHandler.setHandler(staticResourceHandler);
           
           ServletContextHandler context = new ServletContextHandler(ServletContextHandler.SESSIONS);
           context.setContextPath("/orahle");
           context.setResourceBase("./src/main/webapp");
           context.setClassLoader(Thread.currentThread().getContextClassLoader());
           ServletHolder jspServlet = context.addServlet(/*org.apache.jasper.servlet.*/JspServlet.class, "*.jspx");
           //jspServlet.setName("jsp");
           //jspServlet.setInitParameter("compilerClassName", "debug");
           jspServlet.setInitParameter("development", "true");
           jspServlet.setInitParameter("displaySourceFragment", "true");
           jspServlet.setInitParameter("errorOnUseBeanInvalidClassAttribute", "true");
                   
           jspServlet.setInitParameter("logVerbosityLevel", "debug");
           jspServlet.setInitParameter("fork", "false");
           jspServlet.setInitParameter("keepgenerated", "true");
           context.addServlet(DbConnectServlet.class, "/dbconnect");
           context.addServlet(DbQueryServlet.class, "/dbquery");
           context.getResource("/db-connect.jspx");
           HandlerCollection handlers = new HandlerCollection();
           handlers.setHandlers(new Handler[] { context, staticContextHandler, new DefaultHandler() });
           server.setHandler(handlers);
           server.start();
           server.join();
       } 
       catch (Exception e) 
       {
           e.printStackTrace();
       }
    }

    // http://tomcat.apache.org/tomcat-8.0-doc/jasper-howto.html
    // http://www.eclipse.org/jetty/documentation/9.2.1.v20140609/configuring-jsp.html

}
