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

@WebServlet(urlPatterns = {"/edit_ex_name"})
public class edit_ex_name extends HttpServlet {
    Connection conn;
    String ex_id; // exhibitor id to alter
    String new_ex_fname; // First name entered
    
    
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
        
        ex_id = request.getParameter("edit_name");
        new_ex_fname = request.getParameter("new_ex_fname");
        
        try {
            Class.forName("com.mysql.jdbc.Driver");
            Statement stat = conn.createStatement();
            
            //String command = "DELETE FROM Exhibitors WHERE exhibitor_id = '" + ex_id+ "'";
            String command = "UPDATE Exhibitors SET exhibitor_fname = '"+new_ex_fname+"' WHERE exhibitor_id = '"+ex_id+"'";
            
            stat.executeUpdate(command);
        } catch (Exception e) {
            System.err.println(e);
        }
        
        response.sendRedirect("manage_exhibitors");  // redirects back to schedule.html after form submitted
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
