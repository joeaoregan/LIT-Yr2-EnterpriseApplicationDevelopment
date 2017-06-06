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
@WebServlet(urlPatterns = {"/add_speaker"})
public class add_speaker extends HttpServlet {
    String speaker_fname;
    String speaker_lname;
    String speaker_bio;
    String speaker_website;
    String speaker_pic;
    
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
        
        speaker_fname = request.getParameter("speaker_fname");
        speaker_lname = request.getParameter("speaker_lname");
        speaker_bio = request.getParameter("speaker_bio");
        speaker_website = request.getParameter("speaker_website");
        speaker_pic = request.getParameter("speaker_pic");
        
        try {
            String query = "INSERT INTO Speakers (speaker_fname, speaker_lname, speaker_bio, speaker_website,speaker_pic) VALUES (?,?,?,?,?)";
            prepStat = (PreparedStatement) conn.prepareStatement(query);
            prepStat.setString(1, speaker_fname);
            prepStat.setString(2, speaker_lname);
            prepStat.setString(3, speaker_bio);
            prepStat.setString(4, speaker_website);
            prepStat.setString(5, speaker_pic);
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
        response.sendRedirect("in_speakers");                                   // redirects back to speakers.html after form submitted
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
            stat.execute("CREATE TABLE IF NOT EXISTS Speakers(speaker_id INT PRIMARY KEY AUTO_INCREMENT, speaker_fname CHAR(40),speaker_lname CHAR(40),speaker_bio TEXT,speaker_website VARCHAR(60),speaker_pic VARCHAR(60));");
        } catch (Exception e) 
        {
            System.err.println(e);
        }
    } // end of init() method
}


