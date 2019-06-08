package Servlets;

import DB.DBConnection;

import javax.servlet.AsyncContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;

public class AddDataServlet extends HttpServlet {


    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        try {
            resp.setContentType("text/html");
            resp.setStatus(HttpServletResponse.SC_OK);
            final AsyncContext ctxt = req.startAsync();
            ctxt.start(new Runnable() {
                public void run() {
                    String line = null;
                    StringBuilder jsonBuff = new StringBuilder();
                    BufferedReader reader = null;
                    try {
                        reader = ctxt.getRequest().getReader();
                        while ((line = reader.readLine()) != null)
                            jsonBuff.append(line);
                        DBConnection.getInstance().addUserData(jsonBuff.toString());

//                        System.out.println("Request JSON string :" + jsonBuff.toString());
//                        System.out.println("Current Thread" + Thread.currentThread().getName());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    finally {
                        ctxt.complete();
                    }
                }
            });
            ctxt.complete();
        } catch (Exception io) {
            System.out.println("Exception Exception" + io.getMessage());
        } finally {
            resp.setContentType("text/html;charset=UTF-8");
            resp.getWriter().println("");
            resp.getWriter().close();
        }
    }
}
