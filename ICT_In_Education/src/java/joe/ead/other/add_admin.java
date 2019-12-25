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
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet(urlPatterns = {"/add_admin"})
public class add_admin extends HttpServlet {  
    String admin_username;
    String admin_password;
    String admin_fname;
    String admin_lname;
    String admin_email;
    String admin_phone;
    String admin_addr1;
    String admin_addr2;
    String admin_town;
    String admin_county;
    
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
        
        admin_username = request.getParameter("admin_username");
        admin_password = request.getParameter("admin_password");
        admin_fname = request.getParameter("admin_fname");
        admin_lname = request.getParameter("admin_lname");
        admin_email = request.getParameter("admin_email");
        admin_phone = request.getParameter("admin_phone");
        admin_addr1 = request.getParameter("admin_addr1");
        admin_addr2 = request.getParameter("admin_addr2");
        admin_town = request.getParameter("admin_town");
        admin_county = request.getParameter("admin_county");
        
        try {
            String query = "INSERT INTO Administrators VALUES (?,?,?,?,?,?,?,?,?,?)";
            prepStat = (PreparedStatement) conn.prepareStatement(query);
            prepStat.setString(1, admin_username);
            prepStat.setString(2, admin_password);
            prepStat.setString(3, admin_fname);
            prepStat.setString(4, admin_lname);
            prepStat.setString(5, admin_email);
            prepStat.setString(6, admin_phone);
            prepStat.setString(7, admin_addr1);
            prepStat.setString(8, admin_addr2);
            prepStat.setString(9, admin_town);
            prepStat.setString(10, admin_county);
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
        response.sendRedirect("reg_admin");  // redirects back to RegAdmin after form submitted
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
            stat.execute("CREATE TABLE IF NOT EXISTS Administrators(admin_username CHAR(40) PRIMARY KEY, admin_password CHAR(40) NOT NULL, admin_fname CHAR(40), admin_lname CHAR(40), admin_email VARCHAR(60) NOT NULL, admin_phone VARCHAR(18), " +
                         "admin_addr1 CHAR(40), " +
                         "admin_addr2 CHAR(40), " +
                         "admin_town CHAR(40), " +
                         "admin_county CHAR(40))");
        } catch (Exception e) {
            System.err.println(e);
        }
    } // end of init() method
}
