/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
import java.sql.DriverManager;              // added
import com.mysql.jdbc.Connection;           // added
import com.mysql.jdbc.PreparedStatement;    // added
import com.mysql.jdbc.Statement;            // added
import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author student
 */
@WebServlet(urlPatterns = {"/AdminRegister"})
public class AdminRegister extends HttpServlet {
    String customer_name;
    String customer_email;
    String customer_phone;
    String customer_addr1;
    String customer_addr2;
    String customer_addr3;
    String customer_addr4;
    
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
        
        customer_name = request.getParameter("customer_name");
        customer_email = request.getParameter("customer_email");
        customer_phone = request.getParameter("customer_phone");
        customer_addr1 = request.getParameter("customer_addr1");
        customer_addr2 = request.getParameter("customer_addr2");
        customer_addr3 = request.getParameter("customer_addr3");
        customer_addr4 = request.getParameter("customer_addr4");
        
        try {
            String query = "INSERT INTO customers VALUES (?,?,?,?,?,?,?)";
            prepStat = (PreparedStatement) conn.prepareStatement(query);
            prepStat.setString(1, customer_name);
            prepStat.setString(2, customer_email);
            prepStat.setString(3, customer_phone);
            prepStat.setString(4, customer_addr1);
            prepStat.setString(5, customer_addr2);
            prepStat.setString(6, customer_addr3);
            prepStat.setString(7, customer_addr4);
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
        String dbName = "JoeDB";
        String userName = "root";
        String password = "password";
        
        try{
            Class.forName("com.mysql.jdbc.Driver");
            conn = (Connection) DriverManager.getConnection
                    (url+dbName,userName,password);
            stat = (Statement) conn.createStatement();
            stat.execute("DROP TABLE customers");
            stat.execute("CREATE TABLE IF NOT EXISTS customers " + 
                    "(customer_name CHAR(40), customer_email CHAR(40), customer_phone CHAR(40), customer_addr1 CHAR(40), customer_addr2 CHAR(40), customer_addr3 CHAR(40), customer_addr4 CHAR(40))");
        } catch (Exception e) 
        {
            System.err.println(e);
        }
    } // end of init() method
}
