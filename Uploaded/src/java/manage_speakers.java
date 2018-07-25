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
@WebServlet(urlPatterns = {"/manage_speakers"})
public class manage_speakers extends HttpServlet {
    Connection conn;
    PreparedStatement prepStat;
    Statement stat;
    
    String title = "Manage Speakers";
    String sp_id;
    String speak_name;
    String speak_bio;
    String speak_site;
    String speak_pic;
    int speak_num = 1; // give each speaker a number for output
    int sp_count; // number of speakers in the db
       
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
        //sched_location = request.getParameter("schedule_location");
        response.setContentType("text/html;charset=UTF-8");
        
        try (PrintWriter out = response.getWriter()) {
            out.println("<!DOCTYPE html>" +
                        "<html>" +
                        "<head>" +
                            "<link rel=\"stylesheet\" type=\"text/css\" href=\"CAstyle.css\">" +
                        "<title>"+title+"</title>" +
                        "</head>");
            out.println("<body>");
// Heading
            out.println("<div class=\"heading\">" +
                        "<table>" +
                            "<tr><td><div class=\"logo\"><a align=\"left\" href=\"index\" title=\"Return To Homepage (Alt + 7)\" accesskey=\"7\">" + 
                                "<img src='" + request.getContextPath() + "/images/logoT.png' alt=\"Event Logo\" id=\"img150\"></a></div></td>" +
                                "<td><h1>" + title + "</h1></td></tr>" +
                        "</table>" +
                    "</div>");
            
// Navigation menu (Speakers Highlighted)
            out.println("<div class=\"navigation\"><span>" +
                            "<form action=\"show_schedule\" method=\"get\"><button name=\"buttonEventSchedule\" title=\"Event Schedule (Alt + 3)\">Event Schedule</button></form>" +
                            "<form action=\"manage_speakers\" method=\"get\"><button name=\"buttonSpeaker\" id=\"active\" title=\"Add Speaker Details (Alt + h)\">Manage Speakers</button></form>" +
                            "<form action=\"manage_workshops\" method=\"get\"><button name=\"buttonWorkshop\" title=\"Add Workshop Details (Alt + j)\">Manage Workshops</button></form>" +
                            "<form action=\"manage_schedule\" method=\"get\"><button name=\"buttonSchedule\" title=\"Add Schedule Details (Alt + k)\">Manage Schedule</button></form>" +
                            "<form action=\"manage_exhibitors\" method=\"get\"><button name=\"buttonExhibitor\" title=\"Add Exhibitor Details (Alt + l)\">Manage Exhibitors</button></form>" +
                            "<form action=\"index\" method=\"get\"><button name=\"buttonHome\" title=\"Return To Homepage (Alt + 7)\">Home</button></form>" +
                        "</span></div>");
// Count the speakers
            try {
                sp_count=0;
                java.sql.Statement stmt = conn.createStatement();
                ResultSet sp = stmt.executeQuery("SELECT COUNT(*) AS sp_counter FROM Speakers;");
                sp.next();
                sp_count = sp.getInt("sp_counter");
            } catch (Exception e) { System.err.println(e); }  
            
            out.println("<div class=\"mainbody\">" +
                          "<form action=\"add_speaker\" method=\"POST\"><br>" +
                            "<table align=\"center\">" +
                                "<tr><td class=\"tbhead\" colspan=\"2\">Enter Speaker Details</td></tr>");
// Show number of speakers
            switch (sp_count) {
                case 1:
                    out.println("<tr><td colspan=\"2\">There is currently "+sp_count+" speaker registered</td></tr>");
                    break;
                case 0:
                    out.println("<tr><td colspan=\"2\">There is no Speakers registered yet</td></tr>");
                    break;
                default:
                    out.println("<tr><td colspan=\"2\">There are currently "+sp_count+" speakers registered</td></tr>");
                    break;
            }
            
// Enter Speaker Details
            out.println("<tr><td colspan=\"2\">&nbsp;</td></tr>" +
                                "<tr>" +
                                    "<th>First Name:</th>" +
                                    "<td><input type=\"text\" name=\"speaker_fname\" autofocus=\"autofocus\" title=\"Enter speakers first name\" maxlength=\"40\" placeholder=\"Required Field\" required></td>" +
                                "</tr>" +
                                "<tr>" +
                                    "<th>Last Name:</th>" +
                                    "<td><input type=\"text\" name=\"speaker_lname\" title=\"Enter speakers last name\" maxlength=\"40\" placeholder=\"Required Field\" required></td>" +
                                "</tr>" +
                                "<tr>" +
                                    "<th>Biography:</th>" +
                                    "<td><textarea rows=\"15\" cols=\"50\" name=\"speaker_bio\" title=\"Enter speakers background info\" placeholder=\"Required Field\" required></textarea></td>" +
                                "</tr>" +
                                "<tr>" +
                                    "<th>Website:</th>" +
                                    "<td><input type=\"text\" name=\"speaker_website\" title=\"Enter speakers website\" maxlength=\"60\"></td>" +
                                "</tr>" +
                                "<tr>" +
                                    "<th>Picture URL:</th>" +
                                    "<td><input type=\"text\" name=\"speaker_pic\" title=\"Enter speaker picture url\"maxlength=\"60\" ></td>" +
                                "</tr>" +
                                "<tr><td></td><td>e.g \"/images/example.png\"</td></tr>" +
                                "<tr>" +
                                    "<td></td><td id=\"bt\"><input type=\"submit\" value=\"Submit\" title=\"Submit Details\"/></td>" +
                                "</tr>" +
                            "</table>" +
                          "</form>" +
                        "</div>");
            
// Show speakers (only show edit if there are speakers in the DB)
            if (sp_count > 0)
            {
            out.println("<br><div class=\"mainbody\">" +
                            "<table align=\"center\">" +
                                "<tr><td class=\"tbhead\" colspan=\"3\">Current Speakers</td></tr>" +
                                "<tr><td colspan=\"3\">&nbsp;</td></tr>" +                 
                                "<tr><td colspan=\"3\">A list of the keynote speakers currently scheduled:</td></tr>" +         
                                "<tr><td colspan=\"3\">&nbsp;</td></tr>");
                            try {
                                java.sql.Statement stmt = conn.createStatement();
                                ResultSet speakers = stmt.executeQuery("SELECT * FROM Speakers");

                                speak_num=1;
                                while (speakers.next()) {
                                    sp_id = speakers.getString("speaker_id");
                                    speak_name = speakers.getString("speaker_fname") + " " + speakers.getString("speaker_lname");
                                    speak_bio = speakers.getString("speaker_bio");
                                    speak_site = speakers.getString("speaker_website");
                                    speak_pic = speakers.getString("speaker_pic");

                                out.println("<tr><td rowspan=\"6\"><img src='"+ request.getContextPath() + speak_pic+"' alt=\""+speak_name+" Picture\" id=\"img200\"></td><td></td><td></td></tr>" +
                                            "<tr><th colspan=\"2\" id=\"thc\">Keynote Speaker "+speak_num+"</th></tr>" +
                                            "<tr><th>DB ID:</th><td>" + sp_id + "</td></tr>" +
                                            "<tr><th>Name:</th><td>" + speak_name + "</td></tr>" +
                                            "<tr valign=\"top\"><th>Biography:</th><td>" + speak_bio + "</td></tr>" +
                                            "<tr><th>Website:</th><td><a href=\"" + speak_site + "\">" + speak_site + "</a></td></tr>" +
                                            "<tr><td colspan=\"3\"><hr></td></tr>");
                                speak_num++;
                                }
                            }
                            catch (Exception e)
                            {
                                System.err.println(e);
                            }            
                            out.println("</table></div><br>");
            
// Edit Speakers           
            out.println("<div class=\"mainbody\">"
                    + "<form action=\"delete_speaker\" method=\"POST\">"
                    + "<table align=\"center\">" +
                        "<tr><td class=\"tbhead\" colspan=\"3\">Edit Speaker Details</td></tr>" +
                        "<tr><td colspan=\"3\">&nbsp;</td></tr>" +
                        "<tr><td colspan=\"3\">Select speaker to delete</td></tr>" +
                        "<tr><td colspan=\"3\">&nbsp;</td></tr>" +                 
                        "<tr>" +
                            "<th>Speaker To Delete:</th>" +
                            "<td><select name=\"delete_speaker\" title=\"Select A Name From The List\">");
            try{
                java.sql.Statement stmt = conn.createStatement();            
                ResultSet Speaker = stmt.executeQuery("SELECT speaker_id,speaker_fname,speaker_lname FROM Speakers");
                
                while(Speaker.next())
                {
                    speak_name = Speaker.getString("speaker_fname") + " " + Speaker.getString("speaker_lname");
                    sp_id = Speaker.getString("speaker_id"); 
                    out.println("<option value=\""+sp_id+"\">" + sp_id + ". " + speak_name + "</option>");
                }
            }
            catch(Exception e)
            {
                System.err.println(e);
            } 
            
            out.println("</select></td>" +
                                "<td id=\"bt\"><input type=\"submit\" value=\"Delete\" title=\"Delete Time\"/></td>" +
                "           </tr>"
                    + "</table>" +
                        "</form>" +
                "</div>");
        } // End if (only show if more than 1 speaker)    
            
// Bottom Links (Manage)             
            out.println("<div  id=\"bl\" class=\"bottomlinks\">" +
                            "<table align=\"center\">" +
                                "<tr><th>Manage:</th><th>Display:</th><th>Register:</th><th>Other:</th><tr>" +
                                "<tr><td><a href=\"manage_speakers\" title=\"Manage Speakers (Alt + h)\" accesskey=\"h\">h. Manage Speakers</a></td>"
                                    + "<td><a href=\"show_speakers\" title=\"Show Speakers (Alt + 1)\" accesskey=\"1\">1. Show Speakers</a></td>"
                                    + "<td><a href=\"reg_admin\" title=\"Administrator Registration Page (Alt + 5)\" accesskey=\"5\">5. Administrator Registration</a></td>"
                                    + "<td><a href=\"index\" title=\"Return To Homepage (Alt + 7)\" accesskey=\"7\">7. Home Page</a></td></tr>" +
                                "<tr><td><a href=\"manage_workshops\" title=\"Manage Workshops (Alt + j)\" accesskey=\"j\">j. Manage Workshops</a></td>"
                                    + "<td><a href=\"show_workshops\" title=\"Show Workshops (Alt + 2)\" accesskey=\"2\">2. Show Workshops</a></td>"
                                    + "<td><a href=\"reg_attendee.html\" title=\"Attendee Registration Page (Alt + 6)\" accesskey=\"6\">6. Attendee Registration</a></td>"
                                    + "<td></td></tr>" +
                                "<tr><td><a href=\"manage_schedule\" title=\"Manage Schedule (Alt + k)\" accesskey=\"k\">k. Manage Schedule</a></td>"
                                    + "<td><a href=\"show_schedule\" title=\"Show Schedule (Alt + 3)\" accesskey=\"3\">3. Show Schedule</a></td>"
                                    + "<td></td>"
                                    + "<td></td></tr>" +
                                "<tr><td><a href=\"manage_exhibitors\" title=\"Manage Exhibitors (Alt + l)\" accesskey=\"l\">l. Manage Exhibitors</a></td>"
                                    + "<td><a href=\"show_exhibitors\" title=\"Show Exhibitors (Alt + 4)\" accesskey=\"4\">4. Show Exhibitors</a></td>"
                                    + "<td></td>"
                                    + "<td></td></tr>" +
                            "</table>" +
                        "</div>" +                    
                    "</body></html>");
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
