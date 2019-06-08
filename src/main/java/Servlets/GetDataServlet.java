package Servlets;

import DB.DBConnection;

import javax.servlet.AsyncContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;

public class GetDataServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        try{
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

                      String data =  DBConnection.getInstance().getData(jsonBuff.toString());
                        System.out.println("Request JSON string :" + data);
                        ctxt.getResponse().getWriter().println(data);

                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    finally {
                        ctxt.complete();
                    }
                }
            });
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }
}
