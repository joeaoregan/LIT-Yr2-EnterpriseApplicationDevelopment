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
import java.io.PrintWriter;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author Joe O'Regan
 * Student Number: K00203642
 */
@WebServlet(urlPatterns = {"/RegisterAdmin"})
public class RegisterAdmin extends HttpServlet {
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
        
        admin_fname = request.getParameter("admin_fname");
        admin_lname = request.getParameter("admin_lname");
        admin_email = request.getParameter("admin_email");
        admin_phone = request.getParameter("admin_phone");
        admin_addr1 = request.getParameter("admin_addr1");
        admin_addr2 = request.getParameter("admin_addr2");
        admin_town = request.getParameter("admin_town");
        admin_county = request.getParameter("admin_county");
        
        try {
            String query = "INSERT INTO Administrators VALUES (?,?,?,?,?,?,?,?)";
            prepStat = (PreparedStatement) conn.prepareStatement(query);
            prepStat.setString(1, admin_fname);
            prepStat.setString(2, admin_lname);
            prepStat.setString(3, admin_email);
            prepStat.setString(4, admin_phone);
            prepStat.setString(5, admin_addr1);
            prepStat.setString(6, admin_addr2);
            prepStat.setString(7, admin_town);
            prepStat.setString(8, admin_county);
            prepStat.executeUpdate();
            }
        catch (Exception e)
        {
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
        response.sendRedirect("RegAdmin.html");  // redirects back to RegAdmin.html after form submitted
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

    public void init() throws ServletException
    {
        String url = "jdbc:mysql://localhost:3306/";
        String dbName = "K00203642";
        String userName = "root";
        String password = "K00203642";
        
        try{
            Class.forName("com.mysql.jdbc.Driver");
            conn = (Connection) DriverManager.getConnection
                    (url+dbName,userName,password);
            stat = (Statement) conn.createStatement();
            //stat.execute("DROP TABLE Administrators");
            stat.execute("CREATE TABLE IF NOT EXISTS Administrators " + 
                    "(admin_fname CHAR(40), admin_lname CHAR(40), admin_email CHAR(40), admin_phone CHAR(40), admin_addr1 CHAR(40), admin_addr2 CHAR(40), admin_town CHAR(40), admin_county CHAR(40))");
        } catch (Exception e) 
        {
            System.err.println(e);
        }
    } // end of init() method
}
