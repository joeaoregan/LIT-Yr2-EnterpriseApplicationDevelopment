/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import com.mysql.jdbc.Connection;
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
 * @author Joe O'Regan Student Number: K00203642
 */
@WebServlet(urlPatterns = {"/show_exhibitors"})
public class show_exhibitors extends HttpServlet {

    String title = "Exhibitors";
    String tableheading = "Current Exhibitors";
    String ex_id;
    String ex_fname;
    String ex_lname;
    String ex_bio;
    String ex_web;
    String ex_pic;
    int ex_num; // number each displayed exhibitor
    int ex_count; // count the exhibitors in the db
    String ex_pic_name;

    Connection conn;
    Statement stat;

    public void init() throws ServletException {
        String url = "jdbc:mysql://localhost:3306/";
        String dbName = "JoeCA";
        String userName = "root";
        String password = "password";
        try {
            Class.forName("com.mysql.jdbc.Driver");
            conn = (Connection) DriverManager.getConnection(url + dbName, userName, password);
            stat = (Statement) conn.createStatement();
            java.sql.Statement stmt = conn.createStatement();
        } catch (Exception e) {
            System.err.println(e);
        }

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
            out.println("<!DOCTYPE html>"
                    + "<html>"
                    + "<head>"
                    + "<link rel=\"stylesheet\" type=\"text/css\" href=\"CAstyle.css\">"
                    + "<title>" + title + "</title>"
                    + "</head><body>");
// Heading
            out.println("<div class=\"heading\">" +
                        "<table>" +
                            "<tr><td><div class=\"logo\"><a align=\"left\" href=\"index\" title=\"Return To Homepage (Alt + 7)\" accesskey=\"7\">" +
                                "<img src='" + request.getContextPath() + "/images/logoT.png' alt=\"Event Logo\" id=\"img150\"></a></div></td>" +
                                "<td><h1>" + title + "</h1></td></tr>" +
                        "</table>" +
                    "</div>");
// Navigation menu
            out.println("<div class=\"navigation\"><span>"
                    + "<form action=\"show_speakers\" method=\"get\"><button name=\"buttonSpeakers\" title=\"Event Speakers (Alt + 1)\">Speakers</button></form>"
                    + "<form action=\"show_workshops\" method=\"get\"><button name=\"buttonWorkshops\" title=\"Event Workshops (Alt + 2)\">Workshops</button></form>"
                    + "<form action=\"show_schedule\" method=\"get\"><button name=\"buttonSchedule\" title=\"Event Schedule (Alt + 3)\">Schedule</button></form>"
                    + "<form action=\"show_exhibitors\" method=\"get\"><button id=\"active\" name=\"buttonExhibitors\" title=\"Event Exhibitors (Alt + 4)\">Exhibitors</button></form>"
                    + "<form action=\"reg_admin\" method=\"get\"><button name=\"buttonRegAdmin\" title=\"Administrator Registration Page (Alt + 5)\">Administrator Registration</button></form>"
                    + "<form action=\"reg_attendee.html\" method=\"get\"><button name=\"buttonRegAttendee\" title=\"Attendee Registration Page (Alt + 6)\">Attendee Registration</button></form>"
                    + "<form action=\"index\" method=\"get\"><button name=\"buttonHome\" title=\"Return To Homepage (Alt + 7)\">Home</button></form>"
                    + "</span></div>");
// Count the number of exhibitors
            try {
                java.sql.Statement stmt = conn.createStatement();
                ex_count = 0;
                ResultSet ex = stmt.executeQuery("SELECT COUNT(*) AS counter FROM Exhibitors;");
                ex.next();
                ex_count = ex.getInt("counter");
            } catch (Exception e) {
                System.err.println(e);
            }

// Current Exhibitors
            if (ex_count == 0) {
                out.println("<div class=\"mainbody\"><br><h2>There are no exhibitors currently registered</h2>");
                out.println("<h3>Register for updates<h3>"
                        + "<form action=\"reg_attendee.html\" method=\"get\"><button name=\"buttonRegAttendee\" title=\"Attendee Registration Page (Alt + 6)\">Register</button></form></div>");
            }else {
                out.println("<div class=\"mainbody\">"
                        + "<table align=\"center\" id=\"sp0\">"
                        + "<tr><td class=\"mainhead\" colspan=\"3\">" + tableheading + "</td></tr>");
                if(ex_count==1) out.println("<tr><td class=\"mainbase\" colspan=\"3\">There is "+ex_count+" Exhibitor currently registered:</td></tr>");
                else            out.println("<tr><td class=\"mainbase\" colspan=\"3\">A list of the "+ex_count+" Exhibitors currently booked:</td></tr>");
                out.println("<tr><td colspan=\"3\">&nbsp;</td></tr>");

                try {
                    java.sql.Statement stmt = conn.createStatement();
                    ResultSet exhibitors = stmt.executeQuery("SELECT * FROM exhibitors ORDER BY exhibitor_lname;"); // Select in ascending alphabetical order by last name
                    ex_num = 1;
                    while (exhibitors.next()) {
                        ex_fname = exhibitors.getString("exhibitor_fname");
                        ex_lname = exhibitors.getString("exhibitor_lname");
                        ex_bio = exhibitors.getString("exhibitor_bio");
                        ex_web = exhibitors.getString("exhibitor_website");
                        ex_pic = exhibitors.getString("exhibitor_pic");
                        ex_pic_name = ex_fname + " " + ex_lname;

                        out.println("<tr><th class=\"thead\" colspan=\"3\">Exhibitor " + ex_num + ": " + ex_pic_name + "</th></tr>"
                                + "<tr class=\"tbody\"><td><img src='"+ request.getContextPath() + ex_pic+"' alt=\"Picture of " + ex_pic_name + "\" id=\"img100\"></td><td>" + ex_bio + "");
                        if(ex_web=="")  out.println("<tr><td class=\"tbase\" colspan=\"3\" style=\"text-align: center;\">&nbsp;</td></tr>"); // bottom (no website)
                        else            out.println("<tr><td class=\"tbase\" colspan=\"3\" style=\"text-align: center;\"><a href=\"" + ex_web + "\">" + ex_web + "</a></td></tr>"); // bottom (website)
                        out.println("<tr><td colspan=\"3\">&nbsp;</td></tr>"); // space between
                        ex_num++;
                    }
                } catch (Exception e) { System.err.println(e); }
                out.println("</table></div><br>");
            } // End else (output exhibitors)
            
// Bottom Links                    
            out.println("<div id=\"bl\" class=\"bottomlinks\">"
                    + "<table align=\"center\">"
                    + "<tr><th>Display:</th><th>Register:</th><th>Other:</th><tr>"
                    + "<tr><td><a href=\"show_speakers\" title=\"Show Speakers (Alt + 1)\" accesskey=\"1\">1. Show Speakers</a></td><td><a href=\"reg_admin\" title=\"Administrator Registration Page (Alt + 5)\" accesskey=\"5\">5. Administrator Registration</a></td><td><a href=\"index\" title=\"Return To Homepage (Alt + 7)\" accesskey=\"7\">7. Home Page</a></td></tr>"
                    + "<tr><td><a href=\"show_workshops\" title=\"Show Workshops (Alt + 2)\" accesskey=\"2\">2. Show Workshops</a></td><td><a href=\"reg_attendee.html\" title=\"Attendee Registration Page (Alt + 6)\" accesskey=\"6\">6. Attendee Registration</a></td><td></td></tr>"
                    + "<tr><td><a href=\"show_schedule\" title=\"Show Schedule (Alt + 3)\" accesskey=\"3\">3. Show Schedule</a></td><td></td><td></td></tr>"
                    + "<tr><td><a href=\"show_exhibitors\" title=\"Show Exhibitors (Alt + 4)\" accesskey=\"4\">4. Show Exhibitors</a></td><td></td><td></td></tr>"
                    + "</table>"
                    + "</div>");

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
