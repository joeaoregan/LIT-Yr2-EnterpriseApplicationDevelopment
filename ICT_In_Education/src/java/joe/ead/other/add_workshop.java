/**
 *
 * @author Joe O'Regan
 * Student Number: K00203642
 */
package joe.ead.other;

import joe.ead.abstracted.Connect;
import java.sql.DriverManager;
import com.mysql.jdbc.Connection;
import com.mysql.jdbc.PreparedStatement;
import com.mysql.jdbc.Statement;
import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet(urlPatterns = {"/add_workshop"})
public class add_workshop extends HttpServlet {
    String workshop_name;
    String workshop_presenter1;
    String workshop_presenter2;
    String workshop_info;
    
    Connection conn;
    PreparedStatement prepStat;
    Statement stat;
        
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
        
        workshop_name = request.getParameter("ws_name");
        workshop_presenter1 = request.getParameter("ws_presenter1");
        workshop_presenter2 = request.getParameter("ws_presenter2");
        workshop_info = request.getParameter("ws_info");
        
        try {
            String query = "INSERT INTO Workshops(ws_name,ws_presenter1,ws_presenter2,ws_info) VALUES (?,?,?,?)";
            prepStat = (PreparedStatement) conn.prepareStatement(query);
            prepStat.setString(1, workshop_name);
            prepStat.setString(2, workshop_presenter1);
            prepStat.setString(3, workshop_presenter2);
            prepStat.setString(4, workshop_info);
            prepStat.executeUpdate();
        } catch (Exception e) {
            System.err.println(e);
        }
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
        response.sendRedirect("manage_workshops");  // redirects back to workshops.html after form submitted
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
        try {
            Class.forName("com.mysql.jdbc.Driver");
           // conn = (Connection) DriverManager.getConnection (Connect.url+Connect.dbName,Connect.userName,Connect.password);
            conn = Connect.getConnection();
            stat = (Statement) conn.createStatement();
            //stat.execute("DROP TABLE Workshops");
            stat.execute("CREATE TABLE IF NOT EXISTS Workshops(ws_id INT PRIMARY KEY AUTO_INCREMENT, ws_name VARCHAR(60) NOT NULL, ws_presenter1 CHAR(40) NOT NULL, ws_presenter2 CHAR(40), ws_info TEXT NOT NULL)");
        } catch (Exception e) {
            System.err.println(e);
        }
    } // end of init() method
}
