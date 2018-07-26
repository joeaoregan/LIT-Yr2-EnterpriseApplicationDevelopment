/**
 *
 * @author Joe O'Regan
 * Student Number: K00203642
 */
package joe.ead.other;
import joe.ead.abstracted.Connect;

import java.sql.Statement;
import java.sql.DriverManager;
import com.mysql.jdbc.Connection;
import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet(urlPatterns = {"/init_ws"})
public class init_ws extends HttpServlet {
    
    Connection conn;
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
        
        response.sendRedirect("manage_workshops");  // redirects back to schedule.html after form submitted
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

    public void init() throws ServletException {
        try {
            Class.forName("com.mysql.jdbc.Driver");
            conn = (Connection) DriverManager.getConnection (Connect.url+Connect.dbName,Connect.userName,Connect.password);
            stat = (Statement) conn.createStatement();
            // Create Schedule & Workshop Tables
            System.out.println("here");
            stat.execute("CREATE TABLE IF NOT EXISTS Workshops(ws_id INT PRIMARY KEY AUTO_INCREMENT, ws_name VARCHAR(60) NOT NULL, ws_presenter1 CHAR(40) NOT NULL, ws_presenter2 CHAR(40), ws_info TEXT NOT NULL)");
            stat.execute("CREATE TABLE IF NOT EXISTS Schedule(schedule_time TIME PRIMARY KEY, workshop_id INT NOT NULL, schedule_location CHAR(40), CONSTRAINT fk_shedule_workshop FOREIGN KEY (workshop_id) REFERENCES workshops (ws_id))");
            
            // Insert Break Data
            stat.execute("INSERT INTO Workshops VALUES(1, 'Break','none','none','Break Times:\n8am Begin\n10 a.m. - 10.30 a.m. Break\n1 p.m. - 2 p.m. Lunch\n4 p.m. - 4.30 p.m. Break');");
            
            stat.execute("INSERT INTO schedule VALUES('100000', 1, 'Break')");
            stat.execute("INSERT INTO schedule VALUES('130000', 1, 'Break')");
            stat.execute("INSERT INTO schedule VALUES('160000', 1, 'Break')");
            
            stat.execute("CREATE TABLE IF NOT EXISTS CustSched(workshop_id INT PRIMARY KEY, CONSTRAINT fk_custsched_workshop FOREIGN KEY (workshop_id) REFERENCES workshops (ws_id));");
            //stat.execute("INSERT INTO Workshops(ws_id,ws_name,ws_presenter1,ws_info) VALUES(1,'Break','none','Break Times:\n8am Begin\n10 a.m. - 10.30 a.m. Break\n1 p.m. - 2 p.m. Lunch\n4 p.m. - 4.30 p.m. Break')");
            //stat.execute("INSERT INTO Workshops VALUES(1, 'Break','none','none','Break Times:\n8am Begin\n10 a.m. - 10.30 a.m. Break\n1 p.m. - 2 p.m. Lunch\n4 p.m. - 4.30 p.m. Break');");
            
            //stat.execute("INSERT INTO Workshops VALUES(1, 'Break','none','Break Times:\n8am Begin\n10 a.m. - 10.30 a.m. Break\n1 p.m. - 2 p.m. Lunch\n4 p.m. - 4.30 p.m. Break')");
        } catch (Exception e) {
            System.err.println(e);
        }
    } // end of init() method
}
