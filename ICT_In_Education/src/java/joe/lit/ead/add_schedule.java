package joe.lit.ead;
import joe.ait.cse.Connect;

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
@WebServlet(urlPatterns = {"/add_schedule"})
public class add_schedule extends HttpServlet {
    String schedule_time;
    String workshop_id;
    String schedule_location;
    
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
        
        schedule_time = request.getParameter("schedule_time");
        workshop_id = request.getParameter("workshop_name");
        schedule_location = request.getParameter("schedule_location");
        
        try {
            String query = "INSERT INTO Schedule(schedule_time,workshop_id,schedule_location) VALUES (?,?,?)";
            prepStat = (PreparedStatement) conn.prepareStatement(query);
            prepStat.setString(1, schedule_time);
            prepStat.setString(2, workshop_id);
            prepStat.setString(3, schedule_location);
            prepStat.executeUpdate();
            }
        catch (Exception e)
        {
            System.err.println(e);
        }
        
        response.sendRedirect("manage_schedule#addws");  // redirects back to schedule.html after form submitted
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
        try{
            Class.forName("com.mysql.jdbc.Driver");
            conn = (Connection) DriverManager.getConnection (Connect.url+Connect.dbName,Connect.userName,Connect.password);
            stat = (Statement) conn.createStatement();
            stat.execute("CREATE TABLE IF NOT EXISTS Schedule(schedule_time TIME PRIMARY KEY, workshop_id INT NOT NULL, schedule_location CHAR(40), CONSTRAINT fk_shedule_workshop FOREIGN KEY (workshop_id) REFERENCES workshops (ws_id))");
        } catch (Exception e) 
        {
            System.err.println(e);
        }
    } // end of init() method
}
