/**
 *
 * @author Joe O'Regan
 * Student Number: K00203642
 */
package joe.ead.front;

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
import joe.ead.abstracted.Connect; // 24/07/2018
import joe.ead.abstracted.Menu; // 24/07/2018

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
        try {
            Class.forName("com.mysql.jdbc.Driver");
            conn = (com.mysql.jdbc.Connection) DriverManager.getConnection (Connect.url+Connect.dbName,Connect.userName,Connect.password);
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
        
        Menu menu = new Menu();
            
        response.setContentType("text/html;charset=UTF-8");

        try (PrintWriter out = response.getWriter()) {
            out.println("<!DOCTYPE html>"
                + "<html>"
                + "<head>"
                + "<link rel=\"stylesheet\" type=\"text/css\" href=\"CAstyle.css\">"
                + "<title>" + title + "</title>"
                + "</head><body>");

            menu.heading(request, out, title); // Page Heading
            menu.navigationMenu(out, menu.SHOW_EXHIBITORS, "navigation"); // Navigation menu
           
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
            
            menu.bottomMenu(request,out); // Bottom Links 

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
