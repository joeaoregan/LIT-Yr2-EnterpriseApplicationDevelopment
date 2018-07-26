/**
 *
 * @author Joe O'Regan
 * Student Number: K00203642
 */
package joe.ead.front;

import com.mysql.jdbc.Connection;
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

@WebServlet(urlPatterns = {"/show_speakers"})
public class show_speakers extends HttpServlet {
    String tableheading = "Current Speakers";
    String title = "Speakers";
    String selectQuery = "id";
    Connection conn;
    
    String sp_id;
    String speak_name;
    String speak_bio;
    String speak_site;
    String speak_pic;
    int sp_count=0; // count speakers in d.b.
    int speak_num = 1; // Number each speaker read from d.b.
       
    public void init() throws ServletException {
        try {
            Class.forName("com.mysql.jdbc.Driver");
            conn = (com.mysql.jdbc.Connection) DriverManager.getConnection (Connect.url+Connect.dbName,Connect.userName,Connect.password);
        } catch(Exception e){
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
        //sched_location = request.getParameter("schedule_location");
        response.setContentType("text/html;charset=UTF-8");
        
        try (PrintWriter out = response.getWriter()) {
            
            Menu menu = new Menu();
            
            out.println("<!DOCTYPE html>" +
                        "<html>" +
                        "<head>" +
                            "<link rel=\"stylesheet\" type=\"text/css\" href=\"css/CAstyle.css\">" +
                            "<title>" + title + "</title>" +
                        "</head><body>");

            menu.heading(request, out, title); // Page Heading
            menu.navigationMenu(out, menu.SHOW_SPEAKERS, "navigation"); // Navigation menu
            
// Number of Speakers
            try {
                sp_count = 0;
                java.sql.Statement stmt = conn.createStatement();
                ResultSet speakers = stmt.executeQuery("SELECT count(*) AS speaker_count FROM Speakers");
                while (speakers.next()) {
                sp_count = speakers.getInt("speaker_count");
                }
            } catch (Exception e) {
                System.err.println(e);
            } 
            
// Show speakers              
            if(sp_count==0){
                out.println("<div class=\"mainbody\"><br><h2>There are currently "+sp_count+" speakers scheduled</h2>");
                out.println("<h3>Register for updates<h3>"
                        + "<form action=\"reg_attendee.html\" method=\"get\"><button name=\"buttonRegAttendee\" title=\"Attendee Registration Page (Alt + 6)\">Register</button></form></div>");
            } else{          
                out.println("<div class=\"mainbody\">" +
                                "<table align=\"center\" id=\"sp0\">" +
                                    "<tr><td class=\"mainhead\" colspan=\"3\">"+tableheading+"</td></tr>");
                // Format Output
                if(sp_count==1) out.println("<tr><td class=\"mainbase\" colspan=\"3\">There is "+sp_count+" keynote speaker currently scheduled:</td></tr>");
                else            out.println("<tr><td class=\"mainbase\" colspan=\"3\">A list of the "+sp_count+" keynote speakers currently scheduled:</td></tr>");
                out.println("<tr><td colspan=\"3\">&nbsp;</td></tr>");
                   
                try {
                    java.sql.Statement stmt = conn.createStatement();
                    ResultSet speakers = stmt.executeQuery("SELECT * FROM Speakers ORDER BY speaker_lname");

                    speak_num=1;
                    while (speakers.next()) {
                        sp_id = speakers.getString("speaker_id");
                        speak_name = speakers.getString("speaker_fname") + " " + speakers.getString("speaker_lname");
                        speak_bio = speakers.getString("speaker_bio");
                        speak_site = speakers.getString("speaker_website");
                        speak_pic = speakers.getString("speaker_pic");

                        out.println("<tr class=\"tbody\"><th colspan=\"3\" class=\"thead\">Keynote Speaker "+speak_num+": "+speak_name+"</th></tr>" +
                                    "<tr class=\"tbody\"><td rowspan=\"3\"><img src='"+ request.getContextPath() + speak_pic+"' alt=\"Speaker Picture For "+speak_name+"\" id=\"img200\"></td><td></td><td></td></tr>" +
                                    //"<tr><th>DB ID:</th><td>" + sp_id + "</td></tr>" +
                                    "<tr class=\"tbody\" valign=\"top\"><th>About:</th><td>" + speak_bio + "</td></tr>" +
                                    "<tr class=\"tbody\"><th>Website:</th><td><a href=\"" + speak_site + "\">" + speak_site + "</a></td></tr>");
                        out.println("<tr><td class=\"tbase\" colspan=\"3\">&nbsp;</td></tr>" + // bottom
                                    "<tr><td colspan=\"3\">&nbsp;</td></tr>"); // space
                        speak_num++;
                    }
                } catch (Exception e) {
                    System.err.println(e);
                }            
                out.println("</table></div>");
            } // else  
            
            menu.bottomMenu(request,out); // Bottom Links 
                        
            out.println("</body>" +
            "</html>");
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
