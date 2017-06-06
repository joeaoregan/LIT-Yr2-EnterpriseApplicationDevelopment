/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import com.mysql.jdbc.Connection;
import com.mysql.jdbc.PreparedStatement;
import com.mysql.jdbc.Statement;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.DriverManager;
import java.sql.ResultSet;
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
@WebServlet(urlPatterns = {"/show_workshops"})
public class show_workshops extends HttpServlet {
    String title = "Workshops";
    String tableheading = "Current Workshops";
    Connection conn;
    PreparedStatement prepStat;
    Statement stat;
    
    String ws_id;
    String ws_name;
    String ws_pres1;
    String ws_pres2;;
    String ws_info;
    int ws_num = 1;
    int workshop_count;
       
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
                    
                    java.sql.Statement stmt = conn.createStatement();  
                }
                catch(Exception e){System.err.println(e);}
                
    } // end init
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
        response.setContentType("text/html;charset=UTF-8");
        
        try (PrintWriter out = response.getWriter()) {
            out.println("<!DOCTYPE html>" +
                        "<html>" +
                        "<head>" +
"                           <link rel=\"stylesheet\" type=\"text/css\" href=\"CAstyle.css\">" +
                    "<title>" + title + "</title>" +
                    "</head>");
            
            out.println("<body>"
                        + "<div class=\"heading\">" +
                            "<table>" +
                                "<tr><td><a align=\"left\" href=\"index\" title=\"Return To Homepage (Alt + 7)\" accesskey=\"7\"><img src=\"http://s21.postimg.org/gyukaf1l3/Logo.png\" alt=\"Event Logo\" style=\"width:150px;height:150px;\"></a></td>" +
                                "<td><h1 style=\"text-align:center\">" + title + "</h1></td></tr>" +
                            "</table>" +
                        "</div>");
// Navigation menu
            out.println("<div class=\"navigation\">" +
                            "<form id=\"navm\" action=\"show_speakers\" method=\"get\"><button name=\"buttonSpeakers\" title=\"Event Speakers (Alt + 1)\">Event Speakers</button></form>" +
                            "<form id=\"navm\" action=\"show_workshops\" method=\"get\"><button style=\"color: blue; background-color: white;\" name=\"buttonWorkshops\" title=\"Event Workshops (Alt + 2)\">Event Workshops</button></form>" +
                            "<form id=\"navm\" action=\"show_schedule\" method=\"get\"><button name=\"buttonSchedule\" title=\"Event Schedule (Alt + 3)\">Event Schedule</button></form>" +
                            "<form id=\"navm\" action=\"show_exhibitors\" method=\"get\"><button name=\"buttonExhibitors\" title=\"Event Exhibitors (Alt + 4)\">Event Exhibitors</button></form>" +
                            "<form id=\"navm\" action=\"reg_admin\" method=\"get\"><button name=\"buttonRegAdmin\" title=\"Administrator Registration Page (Alt + 5)\">Administrator Registration</button></form>" +
                            "<form id=\"navm\" action=\"reg_attendee.html\" method=\"get\"><button name=\"buttonRegAttendee\" title=\"Attendee Registration Page (Alt + 6)\">Attendee Registration</button></form>" +
                            "<form id=\"navm\" action=\"index\" method=\"get\"><button name=\"buttonHome\" title=\"Return To Homepage (Alt + 7)\">Home</button></form>" +
                        "</div>");

// Count the number of workshops scheduled (from Workshops table)
            try {
                java.sql.Statement stmt = conn.createStatement();
                workshop_count = 0;
                ResultSet schedule = stmt.executeQuery("SELECT COUNT(*) AS counter FROM Workshops WHERE ws_id != 1;");
                schedule.next();        
                workshop_count = schedule.getInt("counter");
            } catch (Exception e) {System.err.println(e);}
            
// Current Workshops            
            out.println("<div class=\"mainbody\">" +
                            "<table align=\"center\">" +
                                "<tr><td class=\"tbhead\" colspan=\"4\">"+tableheading+"</td></tr>" +
                                "<tr><td colspan=\"4\">&nbsp;</td></tr>");
                                if (workshop_count < 1) out.println("<tr><td colspan=\"4\">There are no workshops currently scheduled:</td></tr>");
                                else if (workshop_count == 1) out.println("<tr><td colspan=\"4\">There is "+workshop_count+" workshops currently scheduled:</td></tr>");
                                else out.println("<tr><td colspan=\"4\">There are "+workshop_count+" workshops currently scheduled:</td></tr>");
                                        
            out.println("<tr><td colspan=\"4\">&nbsp;</td></tr>");                    
            try {
                java.sql.Statement stmt = conn.createStatement();
                ResultSet speakers = stmt.executeQuery("SELECT * FROM Workshops WHERE ws_name NOT LIKE 'Break'");
                
                ws_num=1;                                                                                                                  // workshop number
                while (speakers.next()) {
                    ws_id = speakers.getString("ws_id");
                    ws_name = speakers.getString("ws_name");
                    ws_pres1 = speakers.getString("ws_presenter1");
                    ws_pres2 = speakers.getString("ws_presenter2");
                    ws_info = speakers.getString("ws_info");
                 
                out.println("<tr><th colspan=\"4\" class=\"thead\">Workshop "+ws_num+": "+ws_name+"</th></tr>");                           // heading
                        if (ws_pres2.contentEquals( "" )) 
                        { 
                            out.println("<tr><th>Presenter:</th><td>"+ws_pres1+"</td><th></th><td></td></tr>");                          // 2 presenters  
                        }
                        else 
                        {   
                            out.println("<tr><th>Presenter 1:</th><td>"+ws_pres1+"</td><th>Presenter 2:</th><td>"+ws_pres2+"</td></tr>");  // 1 presenter                               
                        }
                out.println("<tr><th>About:</th><td colspan=\"3\">"+ws_info+"</td></tr>" +                                                 // line
                            "<tr><td colspan=\"3\">&nbsp;</td></tr>");
                ws_num++;
                }
            }
            catch (Exception e)
            {
                System.err.println(e);
            }            
            out.println("</table></div>");

// Bottom Links                    
            out.println("<div id=\"bl\" class=\"bottomlinks\">" +
                            "<table align=\"center\">" +
                                "<tr><th>Display:</th><th>Register:</th><th>Other:</th><tr>" +
                                "<tr><td><a href=\"show_speakers\" title=\"Show Speakers (Alt + 1)\" accesskey=\"1\">1. Show Speakers</a></td><td><a href=\"reg_admin\" title=\"Administrator Registration Page (Alt + 5)\" accesskey=\"5\">5. Administrator Registration</a></td><td><a href=\"index\" title=\"Return To Homepage (Alt + 7)\" accesskey=\"7\">7. Home Page</a></td></tr>" +
                                "<tr><td><a href=\"show_workshops\" title=\"Show Workshops (Alt + 2)\" accesskey=\"2\">2. Show Workshops</a></td><td><a href=\"reg_attendee.html\" title=\"Attendee Registration Page (Alt + 6)\" accesskey=\"6\">6. Attendee Registration</a></td><td></td></tr>" +
                                "<tr><td><a href=\"show_schedule\" title=\"Show Schedule (Alt + 3)\" accesskey=\"3\">3. Show Schedule</a></td><td></td><td></td></tr>" +
                                "<tr><td><a href=\"show_exhibitors\" title=\"Show Exhibitors (Alt + 4)\" accesskey=\"4\">4. Show Exhibitors</a></td><td></td><td></td></tr>" +
                            "</table>" +
                        "</div>");
                        
            out.println("</body></html>");
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

}
