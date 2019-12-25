/**
 *
 * @author Joe O'Regan
 * Student Number: K00203642
 */
package joe.ead.other;
import joe.ead.abstracted.Connect;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
import java.sql.DriverManager;
import com.mysql.jdbc.Connection;
import com.mysql.jdbc.PreparedStatement;
import com.mysql.jdbc.Statement;
import java.io.IOException;
import java.sql.SQLException;
import javax.servlet.ServletException;
import javax.servlet.UnavailableException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet(urlPatterns = {"/add_attendee"})
public class add_attendee extends HttpServlet {
    String attendee_fname;
    String attendee_lname;
    String attendee_email;
    String attendee_phone;
    String attendee_addr1;
    String attendee_addr2;
    String attendee_town;
    String attendee_county;
    
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
        
        attendee_fname = request.getParameter("attendee_fname");
        attendee_lname = request.getParameter("attendee_lname");
        attendee_email = request.getParameter("attendee_email");
        attendee_phone = request.getParameter("attendee_phone");
        attendee_addr1 = request.getParameter("attendee_addr1");
        attendee_addr2 = request.getParameter("attendee_addr2");
        attendee_town = request.getParameter("attendee_town");
        attendee_county = request.getParameter("attendee_county");
        
        try {
            String query = "INSERT INTO Attendees VALUES (?,?,?,?,?,?,?,?)";
            prepStat = (PreparedStatement) conn.prepareStatement(query);
            prepStat.setString(1, attendee_fname);
            prepStat.setString(2, attendee_lname);
            prepStat.setString(3, attendee_email);
            prepStat.setString(4, attendee_phone);
            prepStat.setString(5, attendee_addr1);
            prepStat.setString(6, attendee_addr2);
            prepStat.setString(7, attendee_town);
            prepStat.setString(8, attendee_county);
            prepStat.executeUpdate();
        } 
        catch (Exception e) {
            System.err.println(e);
        } 
//        catch (SQLException e) {
//            throw new UnavailableException(this, "Could not connect to db");
//        }
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
        response.sendRedirect("reg_attendee.html");  // redirects back to RegAttendee.html after form submitted
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
            //conn = (Connection) DriverManager.getConnection (Connect.url+Connect.dbName,Connect.userName,Connect.password);
            conn = Connect.getConnection();
            stat = (Statement) conn.createStatement();
            stat.execute("CREATE TABLE IF NOT EXISTS Attendees(" +
                         "attendee_fname CHAR(40) NOT NULL, " +
                         "attendee_lname CHAR(40) NOT NULL, " +
                         "attendee_email VARCHAR(40) PRIMARY KEY, " +
                         "attendee_phone VARCHAR(18), " +
                         "attendee_addr1 CHAR(40), " +
                         "attendee_addr2 CHAR(40), " +
                         "attendee_town CHAR(40), " +
                         "attendee_county CHAR(40))");
        } catch (Exception e) {
            System.err.println(e);
        }
    } // end of init() method
}
