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

/**
 *
 * @author Joe O'Regan
 * Student Number: K00203642
 */
@WebServlet(urlPatterns = {"/add_exhibitor"})
public class add_exhibitor extends HttpServlet {
    String exhibitor_fname;
    String exhibitor_lname;
    String exhibitor_bio;
    String exhibitor_website;
    String exhibitor_pic;
    
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
        
        exhibitor_fname = request.getParameter("exhibitor_fname");
        exhibitor_lname = request.getParameter("exhibitor_lname");
        exhibitor_bio = request.getParameter("exhibitor_bio");
        exhibitor_website = request.getParameter("exhibitor_website");
        exhibitor_pic = request.getParameter("exhibitor_pic");
        
        try {
            String query = "INSERT INTO Exhibitors (exhibitor_fname, exhibitor_lname, exhibitor_bio, exhibitor_website, exhibitor_pic) VALUES (?,?,?,?,?)";
            prepStat = (PreparedStatement) conn.prepareStatement(query);
            prepStat.setString(1, exhibitor_fname);
            prepStat.setString(2, exhibitor_lname);
            prepStat.setString(3, exhibitor_bio);
            prepStat.setString(4, exhibitor_website);
            prepStat.setString(5, exhibitor_pic);
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
        
        response.sendRedirect("manage_exhibitors");  // redirects back to manage_exhibitors after form submitted
        
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
        try{
            Class.forName("com.mysql.jdbc.Driver");
            conn = (Connection) DriverManager.getConnection (Connect.url+Connect.dbName,Connect.userName,Connect.password);
            stat = (Statement) conn.createStatement();
            //stat.execute("DROP TABLE Exhibitors");
            stat.execute("CREATE TABLE IF NOT EXISTS Exhibitors(exhibitor_id INT PRIMARY KEY AUTO_INCREMENT, exhibitor_fname CHAR(40) NOT NULL, exhibitor_lname CHAR(40) NOT NULL, exhibitor_bio TEXT NOT NULL, exhibitor_website VARCHAR(60), exhibitor_pic VARCHAR(60));");
        } catch (Exception e) 
        {
            System.err.println(e);
        }
    } // end of init() method
    
    
}


