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

@WebServlet(urlPatterns = {"/delete_cust_sched"})
public class delete_cust_sched extends HttpServlet {
    Connection conn;
    String workshop_id; // The workshop to delete from custom schedule
    
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
        workshop_id = request.getParameter("cust_sched_delete");
        
        try {
            Class.forName("com.mysql.jdbc.Driver");
            Statement stat = conn.createStatement();    
            
            String command = "DELETE FROM CustSched WHERE workshop_id = '" + workshop_id+ "'";
            stat.executeUpdate(command);
        }
        catch (Exception e)
        {
            System.err.println(e);
        }
        response.sendRedirect("show_schedule#cs_add");  // redirects back to schedule.html after form submitted
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
            //conn = (com.mysql.jdbc.Connection) DriverManager.getConnection (Connect.url+Connect.dbName,Connect.userName,Connect.password);
            conn = Connect.getConnection();
        } catch (Exception e) {
            System.err.println(e);
        }
    } // end of init() method
}
