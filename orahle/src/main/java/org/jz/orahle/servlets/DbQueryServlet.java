package org.jz.orahle.servlets;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.jz.orahle.ConnectionServiceImpl;
import org.jz.orahle.DbConnection;
import org.jz.orahle.DbSession;
import org.jz.orahle.DebuggingDbSession;

/**
 *
 * @author jz
 */
public class DbQueryServlet extends HttpServlet
{

    /**
     * Processes requests for both HTTP
     * <code>GET</code> and
     * <code>POST</code> methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException
    {
        response.setContentType("text/html;charset=UTF-8");
        try (PrintWriter out = response.getWriter())
        {
            String dbAlias = request.getParameter("dbalias");
            String userName = request.getParameter("username");
            String userPassword = request.getParameter("password");
            HttpSession session = request.getSession();
            ConnectionServiceImpl connectionService = (ConnectionServiceImpl)session.getAttribute("connectionList");
            if (connectionService == null)
            {
                response.sendRedirect("db-list.jspx");
            } 
            else
            {
                DbConnection dbConnection = connectionService.getConnections(dbAlias);
                if (dbConnection == null)
                {
                    response.sendRedirect("db-list.jspx");
                }
                else
                {
                    DbSession dbSession = new DebuggingDbSession(dbAlias, dbConnection.getUrl(), userName, userPassword);
                    Map<String, DbSession> activeSessions = (Map<String, DbSession>)session.getAttribute("activeSessions");
                    if (activeSessions == null)
                    {
                        activeSessions = new HashMap<>();
                        session.setAttribute("activeSessions", activeSessions);
                    }
                    activeSessions.put(dbAlias, dbSession);
                    response.sendRedirect("db-welcome.jspx?db-alias=" + dbAlias);
                }
            }
        }
    }

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /**
     * Handles the HTTP
     * <code>GET</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException
    {
        processRequest(request, response);
    }

    /**
     * Handles the HTTP
     * <code>POST</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException
    {
        processRequest(request, response);
    }

    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo()
    {
        return "Short description";
    }// </editor-fold>
}
