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

@WebServlet(urlPatterns = {"/index"})
public class Index extends HttpServlet {
    String title = "Event Information";
    Connection conn;
    // Counters
    int sp_count=0,ws_count=0,sc_count=0,ex_count=0; // number of speakers, workshops, or exhibitors in D.B.
    // Schedule
    String sched_time;
    String sched_location;
    // Speakers
    String sp_id;
    String speak_name;
    String speak_bio;
    String speak_pic;
    // Workshops
    String ws_id;
    String ws_name;
    String ws_pres1;
    String ws_pres2;;
    String ws_info;
    // Exhibitors
    String ex_name;
    String ex_bio;
    String ex_web;
    String ex_pic;
    // Random Speaker
    int random_speaker;
    String test = "blah blah";
    double random_speaker_id;
    int[] arrRandomSpeaker; // Put Speaker names in array, and select Randomly
    
    public void init() throws ServletException {
        try {
            Class.forName("com.mysql.jdbc.Driver");
            conn = (Connection) DriverManager.getConnection (Connect.url+Connect.dbName,Connect.userName,Connect.password);
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
        response.setContentType("text/html;charset=UTF-8");
        
        try (PrintWriter out = response.getWriter()) {            
            Menu menu = new Menu();

            out.println("<!DOCTYPE html>" +
                        "<html>" +
                        "<head>" +
                            //"<META HTTP-EQUIV=\"Refresh\" CONTENT=\"5; URL=index\">" + // refresh every 5 seconds
                            "<link rel=\"stylesheet\" type=\"text/css\" href=\"css/CAstyle.css\">" +
                            "<title>"+ title +"</title>" +
                        "</head>");

// Admin Login
            out.println("<body>" +
                            "<div class=\"login\">" +
                                "<form action=\"login\" method=\"Get\">" +
                                    "<table align=\"center\"><tr><td>"+ // icons table (left)
                                        "<table><tr id=\"container\">"+
                                            "<td width=100% rowspan=\"2\"><div><a align=\"left\" href=\"index\" title=\"Return To Homepage (Alt + 7)\" accesskey=\"7\"><img src='" + request.getContextPath() + "/images/logo50.png' alt=\"Event Logo\" id=\"img50\"></a>" +
                                                "&nbsp<a align=\"left\" href=\"show_speakers\" title=\"Event Speakers (Alt + 1)\" accesskey=\"1\"><img src='" + request.getContextPath() + "/images/icon_b50_sp.png' alt=\"Speaker Image\" id=\"img50\"></a>" +
                                                "&nbsp<a align=\"left\" href=\"show_workshops\" title=\"Event Speakers (Alt + 2)\" accesskey=\"2\"><img src='" + request.getContextPath() + "/images/icon_b50_ws.png' alt=\"Workshop Image\" id=\"img50\"></a>" +
                                                "&nbsp<a align=\"left\" href=\"show_schedule\" title=\"Event Speakers (Alt + 3)\" accesskey=\"3\"><img src='" + request.getContextPath() + "/images/icon_b50_sc.png' alt=\"Schedule Image\" id=\"img50\"></a>" +
                                                "&nbsp<a align=\"left\" href=\"show_exhibitors\" title=\"Event Speakers (Alt + 4)\" accesskey=\"4\"><img src='" + request.getContextPath() + "/images/icon_b50_ex.png' alt=\"Exhibitor Image\" id=\"img50\"></a>" +
                                                "&nbsp<a align=\"left\" href=\"reg_attendee.html\" title=\"Register To Attend (Alt + 6)\" accesskey=\"6\"><img src='" + request.getContextPath() + "/images/icon_b50_reg.png' alt=\"Register Image\" id=\"img50\"></a>" +
                                                "&nbsp<img src='" + request.getContextPath() + "/images/icon_b50_log.png' alt=\"Administrator Login\" style=\"width:50px; height:50px;\" onclick=\"hide()\">" +
                                            "</div></td></tr></table></td><td ></td>" + // close left table
                    
                                            "<td><div style=\"display:none;\" id=\"log\"><table class=\"tpad0\"><tr>"+ // login table (right)
                                                    "<th id=\"thc\">Administrator</th>" +
                                                    "<td>Username:</td>" +
                                                    "<td><input type=\"text\" name=\"username\" autofocus=\"autofocus\" title=\"Please enter username\" maxlength\"40\" required></td>" +
                                                    "<td></td></tr>" +
                                                "<tr><th id=\"thc\">Login</th>" +
                                                    "<td>Password:</td>" +
                                                    "<td><input type=\"password\" name=\"password\" title=\"Please enter password\" maxlength\"40\" required></td>" +
                                                    "<td id=\"bt\" ><input type=\"submit\" value=\"Submit\" title=\"Submit Details\"/></td>" +
                                                "</tr></table></div>" + // close right table
                                    "</td></tr></table>" +
                                "</form>" +
                            "</div>");
            
// Javascript doesn't work 100% the same in servlets (not that I know much about it)
           // out.println("<script>function textHome() { document.getElementById(\"log\").innerHTML = \""+js2+"\"; resetLogin(); }</script>");
           // out.println("<script>function textHome() { document.getElementById(\"log\")[0].setAttribute(\"class\", \"tpad1\"); }</script>");
            //out.println("<script>function hide() { document.getElementById(\"log\").style.visibility = \"hidden\";}</script>");
            out.println("<script>function hide() { document.getElementById(\"log\").style.display = \"block\";}</script>");

            menu.heading(request, out, title); // Page Heading
            menu.navigationMenu(out, menu.INDEX, "navigation"); // Navigation menu (Home Highlighted)
            
// Count the number of Speakers, Workshops, Scheduled Workshops, and Exhibitors in each database
sp_count=0; // Number of speakers
ws_count=0; // Number of workshops
sc_count=0; // Number of workshops scheduled
ex_count=0; // Number of exhibitors

// Have to separate try - as some objects don't appear on website otherwise
            try {
                java.sql.Statement stmt = conn.createStatement();
                ResultSet sp = stmt.executeQuery("SELECT COUNT(*) AS sp_counter FROM Speakers;");
                sp.next();
                sp_count = sp.getInt("sp_counter");
            } catch (Exception e) { 
                System.err.println(e); 
            }   
            try {                
                java.sql.Statement stmt = conn.createStatement();
                ResultSet ws = stmt.executeQuery("SELECT COUNT(*) AS ws_counter FROM Workshops WHERE ws_id != 1;");
                ws.next();        
                ws_count = ws.getInt("ws_counter");
            } catch (Exception e) { System.err.println(e); }   
            try {
                java.sql.Statement stmt = conn.createStatement();
                ResultSet sc = stmt.executeQuery("SELECT COUNT(*) AS sc_counter FROM Schedule WHERE workshop_id != 1;");
                sc.next();        
                sc_count = sc.getInt("sc_counter");
            } catch (Exception e) { System.err.println(e); }   
            try {
                java.sql.Statement stmt = conn.createStatement();
                ResultSet ex = stmt.executeQuery("SELECT COUNT(*) AS ex_counter FROM Exhibitors;");
                ex.next();        
                ex_count = ex.getInt("ex_counter");
            } catch (Exception e) { System.err.println(e); }   
            
// Show A Random Speaker  
            if (sp_count>0) {
                        arrRandomSpeaker = new int[sp_count]; // array sized as the amount of speakers         
                        try {
                            java.sql.Statement stmt = conn.createStatement();    
                            ResultSet speakers = stmt.executeQuery("SELECT * FROM Speakers;");
                            int i=0; // 1st element of array
                            while (speakers.next()) {   
                                arrRandomSpeaker[i++] = speakers.getInt("speaker_id");
                            }    
                        } catch (Exception e) { 
                            System.err.println(e);
                        }
                        random_speaker_id = Math.random();
                        random_speaker = (int) (random_speaker_id * 10 ) % sp_count;
            } else {
                random_speaker=1;  // Just incase there's no info in d.b.
            }
            
// Event Information            
            out.println("<div class=\"mainbody\">" +
                    "<tr><td colspan=\"4\"><table id=\"t100\" align=\"center\">" +  // 1st table
                        "<tr><td class=\"tbtop\"><table>" +  // 2nd table (left)
                            "<tr><td class=\"mainhead\">Random ICT Event 2016</td></tr>" +
                            "<tr><td class=\"tbody\">" +
                                "<p>Events will take place on Monday the 9th of May, 2016. Beginning at 8 am and finishing at 6 pm.<br><br>");

            displayCalendar(out); // Calendar
            
            if (sp_count > 0 || ws_count > 0 || ex_count > 0) out.println("There are currently:</p><ul>");
// output number of each speaker, workshop, exhibitor in a list
            if(sp_count==1)     out.println("<li>"+sp_count+" speaker scheduled</li>");
            else if(sp_count>1) out.println("<li>"+sp_count+" speakers scheduled</li>");
            if(ws_count==1)     out.println("<li>"+ws_count+" workshop scheduled</li>");
            else if(ws_count>1) out.println("<li>"+ws_count+" workshops scheduled</li>");
            if(ex_count==1)     out.println("<li>"+ex_count+" exhibitor scheduled</li>");
            else if(ex_count>1) out.println("<li>"+ex_count+" exhibitors scheduled</li>");
            out.println("</ul><p>Register online for information on tickets, or as an administrator to add a workshop, keynote speaker, or exhibitor.</p>" +
                "</td></tr>" +
                "<tr><td class=\"tbase\" style=\"text-align:right\">" +
                    "<form><a href=\"reg_attendee.html\" title=\"Register To Attend\"><button name=\"btn_regat\" value=\"OK\" type=\"button\">Register</button></a>" +
                    "</form></td></tr>" +
                "<tr><td>&nbsp;</td></tr>" +
            "</table></td>"); // close 2nd table
    // Show schedule        
            if (sc_count!=0){            
                out.println("<td style=\"vertical-align:top;\"><table>" + // 3rd table (right)
                            "<tr><td class=\"thead\"colspan=\"2\">Event Schedule</td></tr>" +
                            "<tr class=\"tshead\"><th>Time</th><th style=\"min-width: 200px\">Name</th></tr>");

                try {
                    java.sql.Statement stmt = conn.createStatement();
                    ResultSet schedule = stmt.executeQuery("SELECT schedule_time,ws_id,ws_name,schedule_location,ws_presenter1,ws_presenter2,ws_info FROM Schedule JOIN Workshops ON Schedule.workshop_id = Workshops.ws_id WHERE ws_name NOT LIKE '%Break%' ORDER BY schedule_time;");

                    while (schedule.next()) {
                        sched_time = schedule.getString("schedule_time");
                        ws_name = schedule.getString("ws_name");
                    out.println("<tr class=\"tbody\"><td>" + sched_time + "</td><td>" + ws_name + "</td></tr>");
                    }
                } catch (Exception e) {
                    System.err.println(e);
                }

                out.println("<tr><td class=\"tbase\" id=\"bt\" colspan=\"2\">" +
                                "<form><a href=\"show_schedule\" title=\"Go To Schedule Page\"><button name=\"btn_schedule\" value=\"OK\" type=\"button\">View Detailed Schedule</button></a>"+
                                "</form></td></tr>");
            } else {  // Nothing in schedule            
                out.println("<td style=\"vertical-align:top;\"><table id=\"sp0\">" +    // 3rd table (right)
                            "<tr><td class=\"thead\"colspan=\"2\" style=\"min-width: 200px\">Event Schedule</td></tr>");
                out.println("<tr class=\"tbody\"><td colspan=\"2\">&nbsp;</td></tr>");
                out.println("<tr class=\"tbody\"><td colspan=\"2\">The schedule will update</td></tr>");
                out.println("<tr class=\"tbody\"><td colspan=\"2\">As workshops are added</td></tr>");
                out.println("<tr class=\"tbody\"><td colspan=\"2\">&nbsp;</td></tr>");
                out.println("<tr><td class=\"tbase\" colspan=\"2\">&nbsp;</td></tr>"); // bottom of table
            }
    // Marquee (speakers)
            if (sp_count>0) {
                out.println("<tr><td class=\"thead\" colspan=\"2\">Speakers</td></tr>"
                        + "<tr class=\"tbody\"><td align=\"center\" colspan=\"2\"><table align=\"center\"><tr><td><a href=\"show_speakers\"><div style=\"max-height: 200px;\"><marquee height=\"200px\" direction=\"up\">");
                try {
                    java.sql.Statement stmt = conn.createStatement();
                    ResultSet speakers = stmt.executeQuery("SELECT * FROM Speakers"); // Show random Speaker
                    while (speakers.next()) {
                        speak_name = speakers.getString("speaker_fname") + " " + speakers.getString("speaker_lname");
                        speak_pic = speakers.getString("speaker_pic");

                        out.println("<img src='"+ request.getContextPath() + speak_pic+"' align=\"middle\" alt=\"Speaker Picture For "+speak_name+"\" align=\"middle\" width=\"300px\" height=\"300px\"/>"
                               // + "<p><a href=\"show_speakers\">Go To Speakers Page</a>");
                               + "<br><br><figcaption><b>Keynote Speaker: </b>"+speak_name+"</figcaption><br>");
                    }
                    out.println("</a></div></marquee></td></tr></table></td></tr><tr><td class=\"tbase\" colspan=\"2\">&nbsp</td></tr>");
                } catch (Exception e) {
                    System.err.println(e);
                }
            }
            out.println("</table></td></tr>" +                          // close 3rd table
                            "<tr><td colspan=\"4\">&nbsp;</td></tr>" +  // Leave Space
                        "</table>");                                    // close 1st table
            out.println("<table align=\"center\">");                    // open main body table
        
// Show Speakers (Randomly)
            if (sp_count!=0) {
                out.println("<table id=\"sp0\" width=\"100%\">" +       // speaker table
                            "<tr><th class=\"thead\"colspan=\"4\">Keynote Speakers</th></tr>");
                if (sp_count==1)out.println("<tr><td class=\"tbody\" colspan=\"4\">There will be "+sp_count+" keynote speaker on the day:</td></tr>");
                else           out.println("<tr><td class=\"tbody\" colspan=\"4\">There will be "+sp_count+" keynote speakers on the day. These will include:</td></tr>");
                try {
                    java.sql.Statement stmt = conn.createStatement();
                    ResultSet speakers = stmt.executeQuery("SELECT * FROM Speakers WHERE speaker_id = "+arrRandomSpeaker[random_speaker]+""); // Show random Speaker
                    while (speakers.next()) {
                        speak_name = speakers.getString("speaker_fname") + " " + speakers.getString("speaker_lname");
                        speak_bio = speakers.getString("speaker_bio");
                        speak_pic = speakers.getString("speaker_pic");

                        out.println("<tr class=\"tbody\"><td rowspan=\"2\"><img src='"+ request.getContextPath() + speak_pic+"' alt=\"Speaker Picture For "+speak_name+"\" id=\"img150\"></td>" +
                                        "<td class=\"tshead\" colspan=\"3\">"+speak_name+"</td></tr>" +
                                    "<tr class=\"tbody\" valign=\"top\"><th height=\"100\">About:</th><td colspan=\"2\" ><div class=\"scrollsp\">" + speak_bio + "</div></td></tr>");
                    }
                }  catch (Exception e){
                    System.err.println(e);
                }
                out.println("<tr><td class=\"tbase\" colspan=\"4\"><form><a href=\"show_speakers\" title=\"Go To Speakers Page\">" +
                                "<button name=\"btn_speakers\" value=\"OK\" type=\"button\">View All Speakers</button></a></form></td></tr>" +
                            "</td></tr><tr><td colspan=\"4\">&nbsp;</td></tr></table>"); // End speaker table + leave space
            } // end if (show speakers) 
            
// Show Workshops
            if (ws_count!=0) {
                out.println("<table id=\"sp0\" width=\"100%\">" + // WS table
                                "<tr><th colspan=\"4\" class=\"thead\">Workshops</th></tr>");
                if (ws_count==1) out.println("<tr class=\"tbody\"><td colspan=\"4\">"+ws_count+" workshop has been scheduled for the event: </td></tr>");
                else out.println("<tr class=\"tbody\"><td colspan=\"4\">"+ws_count+" workshops have been scheduled for the event including: </td></tr>");
                out.println("<tr class=\"tbody\"><td colspan=\"4\"><div class=\"scrollws\"><table id=\"t100\">");  // Table in a table for workshops
                try {
                    java.sql.Statement stmt = conn.createStatement();
                    ResultSet speakers = stmt.executeQuery("SELECT * FROM Workshops WHERE ws_name NOT LIKE 'Break'");

                    while (speakers.next()) {
                        ws_id = speakers.getString("ws_id");
                        ws_name = speakers.getString("ws_name");
                        ws_pres1 = speakers.getString("ws_presenter1");
                        ws_pres2 = speakers.getString("ws_presenter2");
                        ws_info = speakers.getString("ws_info");

                        out.println("<tr class=\"tbody\"><th colspan=\"4\" class=\"tshead\">"+ws_name+"</th></tr>");    // heading
                        if (ws_pres2.contentEquals( "" )) out.println("<tr class=\"tbody\"><th>Presenter:</th><td>"+ws_pres1+"</td><th></th><td></td></tr>");   // 2 presenters  
                        else out.println("<tr class=\"tbody\"><th>Presenter 1:</th><td>"+ws_pres1+"</td><th>Presenter 2:</th><td>"+ws_pres2+"</td></tr>");  // 1 presenter  

                        out.println("<tr class=\"tbody\"><th>About:</th><td colspan=\"3\">"+ws_info+"</td></tr>" +  // line
                                    "<tr class=\"tbody\"><td colspan=\"4\">&nbsp;</td></tr>");
                    }
                } catch (Exception e){
                    System.err.println(e); 
                }
                out.println("</table></div></td></tr><tr class=\"tbody\"><td colspan=\"4\">&nbsp;</td></tr>" +
                                "<tr><td class=\"tbase\" colspan=\"4\"><form><a href=\"show_workshops\" title=\"Go To Workshops Page\">" +
                                        "<button name=\"btn_ws\" value=\"OK\" type=\"button\">View All Workshops</button></a></form></td></tr>" +
                                "</td></tr><tr><td colspan=\"4\">&nbsp;</td></tr></table>"); // End WS table + leave space
            } // end if (show workshops)    
        
// Show Exhibitors
            if (ex_count!=0) {
                out.println("<table id=\"sp0\" width=\"100%\">" + // EX table
                                "<tr><th colspan=\"4\" class=\"thead\">Exhibitors</th></tr>" +
                                "<tr class=\"tbody\"><td colspan=\"4\">&nbsp;</td></tr>");
                if (ex_count==1) out.println("<tr class=\"tbody\"><td colspan=\"4\">There will also be "+ex_count+" exhibitor present at the event:</td></tr>");
                else out.println("<tr class=\"tbody\"><td colspan=\"4\">There will also be "+ex_count+" exhibitors present at the event. Including the following:</td></tr>" +
                                "<tr><td colspan=\"4\">&nbsp;</td></tr>");
                try {
                    java.sql.Statement stmt = conn.createStatement();
                    ResultSet exhibitors = stmt.executeQuery("SELECT * FROM exhibitors ORDER BY exhibitor_lname LIMIT 2;"); // Select in ascending alphabetical order by last name
                    while (exhibitors.next()) {
                        ex_name = exhibitors.getString("exhibitor_fname") + " " + exhibitors.getString("exhibitor_lname");
                        ex_bio = exhibitors.getString("exhibitor_bio");
                        ex_pic = exhibitors.getString("exhibitor_pic");

                        out.println("<tr class=\"tbody\"><td rowspan=\"3\"><img src='"+ request.getContextPath() + ex_pic+"' alt=\"Picture of "+ex_name+"\" id=\"img100\">" +
                                        "</td><th class=\"tshead\" colspan=\"3\">"+ex_name+"</th></tr>" +                                             
                                    "<tr class=\"tbody\"><th>About:</th><td colspan=\"2\">" + ex_bio + "</td></tr>" +
                                    "<tr class=\"tbody\"><td colspan=\"4\">&nbsp;</td></tr>"); 
                    }
                } catch (Exception e) {
                    System.err.println(e);
                }
                out.println("<tr><td class=\"tbase\" colspan=\"4\">" +
                                "<form><a href=\"show_exhibitors\" title=\"Go To Exhibitors Page\"><button name=\"btn_exhibitors\" value=\"OK\" type=\"button\">View All Exhibitors</button></a>" +
                                "</form></td></tr>" +
                            "<tr><td colspan=\"4\">&nbsp;</td></tr></table>"); // End EX table + leave space
            } // end if (show exhibitors)
        
            out.println("</table></div>"); // End main body div + Main table
                    
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

    private void displayCalendar(PrintWriter out){ 
        out.println("<div style=\"max-width: 250px;\"><div class=\"month\">" +
                    "<ul>" +
                        "<li class=\"prev\">❮</li>" +
                        "<li class=\"next\">❯</li>" +
                        "<li style=\"text-align:center\">May<br>" +
                        "<span style=\"font-size:18px\">2016</span>" +
                        "</li>" +
                    "</ul>" +
                "</div>" +
                    "<ul class=\"weekdays\">" +
                        "<li>Mo</li>" +
                        "<li>Tu</li>" +
                        "<li>We</li>" +
                        "<li>Th</li>" +
                        "<li>Fr</li>" +
                        "<li>Sa</li>" +
                        "<li>Su</li>" +
                    "</ul>"  +
                    "<ul class=\"days\">" +
                        "<li></li>" +
                        "<li></li>" +
                        "<li></li>" +
                        "<li></li>" +
                        "<li></li>" +
                        "<li></li>" +
                        "<li>1</li>" +
                        "<li>2</li>" +
                        "<li>3</li>" +
                        "<li>4</li>" +
                        "<li>5</li>" +
                        "<li>6</li>" +
                        "<li>7</li>" +
                        "<li>8</li>" +
                        "<li><span class=\"active\">9</span></li>" +
                        "<li>10</li>" +
                        "<li>11</li>" +
                        "<li>12</li>" +
                        "<li>13</li>" +
                        "<li>14</li>" +
                        "<li>15</li>" +
                        "<li>16</li>" +
                        "<li>17</li>" +
                        "<li>18</li>" +
                        "<li>19</li>" +
                        "<li>20</li>" +
                        "<li>21</li>" +
                        "<li>22</li>" +
                        "<li>23</li>" +
                        "<li>24</li>" +
                        "<li>25</li>" +
                        "<li>26</li>" +
                        "<li>27</li>" +
                        "<li>28</li>" +
                        "<li>29</li>" +
                        "<li>30</li>" +
                        "<li>31</li>" +
                    "</ul></div><br>");  // end calendar
    }
}
