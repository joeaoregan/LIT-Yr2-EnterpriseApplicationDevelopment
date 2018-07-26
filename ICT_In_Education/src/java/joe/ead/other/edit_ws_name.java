/**
 *
 * @author Joe O'Regan
 * Student Number: K00203642
 */
package joe.ead.other;

import joe.ead.abstracted.Connect;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;
import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet(urlPatterns = {"/edit_ws_name"})
public class edit_ws_name extends HttpServlet {
    Connection conn;
    Statement stat;
    String ws_id; // Alter this workshop id
    String new_ws_name; // name to change to
    
    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
     * methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {        
    }

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /**
     * Handles the HTTP <code>GET</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Handles the HTTP <code>POST</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
        
        ws_id = request.getParameter("edit_workshop");
        new_ws_name = request.getParameter("new_wsname");
        
        try {
            Class.forName("com.mysql.jdbc.Driver");
            Statement stat = conn.createStatement();
            
            String command = "UPDATE Workshops SET ws_name = '"+new_ws_name+"' WHERE ws_id = '"+ws_id+"'";
            
            stat.executeUpdate(command);
        } catch (Exception e) {
            System.err.println(e);
        }
        
        response.sendRedirect("manage_workshops");  // redirects back to schedule.html after form submitted
    }

    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>

    public void init() throws ServletException {
        try{
            Class.forName("com.mysql.jdbc.Driver");
            conn = (com.mysql.jdbc.Connection) DriverManager.getConnection (Connect.url+Connect.dbName,Connect.userName,Connect.password);
            stat = (Statement) conn.createStatement();
        } catch (Exception e) {
            System.err.println(e);
        }
    } // end of init() method
}
