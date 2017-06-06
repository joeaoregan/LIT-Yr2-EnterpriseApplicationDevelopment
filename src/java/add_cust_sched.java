/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;
import com.mysql.jdbc.PreparedStatement;
import java.io.IOException;
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
@WebServlet(urlPatterns = {"/add_cust_sched"})
public class add_cust_sched extends HttpServlet {
    String workshop_id; // Workshop to add to custom schedule
    
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
        workshop_id = request.getParameter("custom_schedule");
        
        try {
            Class.forName("com.mysql.jdbc.Driver");
            Statement stat = conn.createStatement();
                       
            java.sql.Statement stmt = conn.createStatement();
            String command = "INSERT INTO CustSched VALUES(" + workshop_id + ");";
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

    public void init() throws ServletException
    {
        String url = "jdbc:mysql://localhost:3306/";
        String dbName = "JoeCA";
        String userName = "root";
        String password = "password";
        
        try{
            Class.forName("com.mysql.jdbc.Driver");
            conn = (Connection) DriverManager.getConnection(url+dbName,userName,password);
            stat = (Statement) conn.createStatement();
            
            stat.execute("CREATE TABLE IF NOT EXISTS CustSched(workshop_id INT PRIMARY KEY, CONSTRAINT fk_custsched_workshop FOREIGN KEY (workshop_id) REFERENCES schedule (workshop_id));");
        }
        catch (Exception e) 
        {
            System.err.println(e);
        }
    } // end of init() method
}
