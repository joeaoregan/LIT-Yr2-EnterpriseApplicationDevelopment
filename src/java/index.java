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
//import java.util.*;

/**
 *
 * @author Joe O'Regan
 * Student Number: K00203642
 */
@WebServlet(urlPatterns = {"/index"})
public class index extends HttpServlet {
    String title = "Event Information";
    Connection conn;
    PreparedStatement prepStat;
    Statement stat;
    // Schedule
    String workshop_count;
    String sched_time;
    String sched_location;
    // Speakers
    int sp_num;
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
    String ex_count;
    String ex_name;
    String ex_bio;
    String ex_web;
    String ex_pic;
    // Random Speaker
    int random_speaker;
    double random_speaker_id;
    int anArray[];
    int test;
        
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
                            //"<META HTTP-EQUIV=\"Refresh\" CONTENT=\"5; URL=index\">" + // refresh every 5 seconds
                            "<link rel=\"stylesheet\" type=\"text/css\" href=\"CAstyle.css\">" +
                            "<title>"+ title +"</title>" +
                        "</head>");
// Admin Login
            out.println("<body>" +
                            "<div class=\"login\">" +
                                "<form action=\"login\" method=\"Get\">" +
                                    "<table>" +
                                        "<tr>" +
                                            "<td width=100% rowspan=\"2\"><a align=\"left\" href=\"index\" title=\"Return To Homepage (Alt + 7)\" accesskey=\"7\"><img src=\"http://s21.postimg.org/gyukaf1l3/Logo.png\" alt=\"Event Logo\" id=\"img50\"></a></td>" +
                                            "<th id=\"thc\">Administrator</th>" +
                                            "<td>Username:</td>" +
                                            "<td><input type=\"text\" name=\"username\" autofocus=\"autofocus\" title=\"Please enter username\" maxlength\"40\" required></td>" +
                                            "<td></td></tr>" +
                                        "<tr>" +
                                            "<th id=\"thc\">Login</th>" +
                                            "<td>Password:</td>" +
                                            "<td><input type=\"password\" name=\"password\" title=\"Please enter password\" maxlength\"40\" required></td>" +
                                            "<td id=\"bt\" ><input type=\"submit\" value=\"Submit\" title=\"Submit Details\"/></td></tr>" +
                                    "</table>" +
                                "</form>" +
                            "</div>");
// Heading
            out.println("<div class=\"heading\">" +
                        "<table>" +
                            "<tr><td>&nbsp;</td></tr>" +
                            "<tr><td>&nbsp;</td></tr>" +
                            "<tr><td><a align=\"left\" href=\"index\" title=\"Return To Homepage (Alt + 7)\" accesskey=\"7\"><img src=\"http://s21.postimg.org/gyukaf1l3/Logo.png\" alt=\"Event Logo\" id=\"img150\"></a></td>" +
                            "<td><h1>" + title + "</h1></td></tr>" +
                        "</table>" +
                    "</div>");
            
// Navigation menu (Home Highlighted)
            out.println("<div class=\"navigation\"><span>" +
                            "<form action=\"show_speakers\" method=\"get\"><button name=\"buttonSpeakers\" title=\"Event Speakers (Alt + 1)\">Speakers</button></form>" +
                            "<form action=\"show_workshops\" method=\"get\"><button name=\"buttonWorkshops\" title=\"Event Workshops (Alt + 2)\">Workshops</button></form>" +
                            "<form action=\"show_schedule\" method=\"get\"><button name=\"buttonSchedule\" title=\"Event Schedule (Alt + 3)\">Schedule</button></form>" +
                            "<form action=\"show_exhibitors\" method=\"get\"><button name=\"buttonExhibitors\" title=\"Event Exhibitors (Alt + 4)\">Exhibitors</button></form>" +
                            "<form action=\"reg_admin\" method=\"get\"><button name=\"buttonRegAdmin\" title=\"Administrator Registration Page (Alt + 5)\">Administrator Registration</button></form>" +
                            "<form action=\"reg_attendee.html\" method=\"get\"><button name=\"buttonRegAttendee\" title=\"Attendee Registration Page (Alt + 6)\">Attendee Registration</button></form>" +
                            "<form action=\"index\" method=\"get\"><button title=\"Return To Homepage (Alt + 7)\" id=\"active\">Home</button></form>" +
                        "</span></div>");
            
// Count the number of workshops scheduled (from Workshops table)
            try {
                java.sql.Statement stmt = conn.createStatement();
                ResultSet schedule = stmt.executeQuery("SELECT COUNT(*) AS counter FROM Workshops WHERE ws_id != 1;");
                schedule.next();        
                workshop_count = schedule.getString("counter");
            } catch (Exception e) {System.err.println(e);}
            
// Info and schedule            
            out.println("<div class=\"mainbody\">" +
                "<table align=\"center\">" +
                    "<tr><td colspan=\"4\"><table id=\"t100\">" +  // 1st table
            //2nd table (left)
            "<tr><td><table style=\"height:100%\">" +
                "<tr><td class=\"tbhead\">Random ICT Event 2016</td></tr>" +
                "<tr><td style=\"vertical-align:top; font-size: 150%;\">" +
                    "<p>There are currently "+workshop_count+" workshops scheduled</p>" +
                    "<p>Events will take place on Monday the 9th of May, 2016</p>" +
                    "<p>A number of keynote speakers, and exhibitors are also already scheduled</p>" +
                    "<p>Register online for information on tickets. Or as an administrator to add a workshop, keynote speaker, or exhibitor.</p>" +
                "</td></tr>" +
            "</table></td>" + // close 2nd table
            
            "<td style=\"vertical-align:top;\"><table>" +// 3rd table (right)
            "<tr><td  class=\"thead\"colspan=\"2\">Event Schedule</td></tr>" +
            "<tr class=\"tshead\"><th>Time</th><th style=\"min-width: 200px\">Name</th></tr>");
                    try {
                        java.sql.Statement stmt = conn.createStatement();
                        ResultSet schedule = stmt.executeQuery("SELECT schedule_time,ws_id,ws_name,schedule_location,ws_presenter1,ws_presenter2,ws_info FROM Schedule JOIN Workshops ON Schedule.workshop_id = Workshops.ws_id WHERE ws_name NOT LIKE '%Break%' ORDER BY schedule_time;");

                        while (schedule.next()) {
                            sched_time = schedule.getString("schedule_time");
                            ws_name = schedule.getString("ws_name");
                        out.println("<tr><td>" + sched_time + "</td><td>" + ws_name + "</td></tr>");
                        }
                    } catch (Exception e) { System.err.println(e); }
            out.println("<tr><td colspan=\"4\">&nbsp;</td></tr>" +
                        "<tr><td id=\"bt\" colspan=\"2\">" +
                            "<form><a href=\"show_schedule\" title=\"Go To Schedule Page\"><button name=\"btn_schedule\" value=\"OK\" type=\"button\">View Detailed Schedule</button></a>"+
                            "</form></td></tr>");                
            out.println("</table></td></tr>"); // close 3rd table                
            out.println("</table>"); // close 1st table


            out.println("<tr><td colspan=\"4\">&nbsp;</td></tr>");
        
// Count the number of Speakers
            try {
                java.sql.Statement stmt = conn.createStatement();
                ResultSet speaker_count = stmt.executeQuery("SELECT COUNT(*) AS counter FROM Speakers;");
                speaker_count.next();
                sp_num = speaker_count.getInt("counter");
            } catch (Exception e) { System.err.println(e); }   
            
            anArray = new int[sp_num]; // array sized as the amount of speakers
            
// Add speaker ids to array            
            try {
                java.sql.Statement stmt = conn.createStatement();    
                ResultSet speakers = stmt.executeQuery("SELECT * FROM Speakers;");
                int i=0; // 1st element of array
                while (speakers.next()) {   
                    anArray[i] = speakers.getInt("speaker_id");
                    i++;
                }    
            } catch (Exception e) { System.err.println(e); }
            // Show A Random Speaker
            random_speaker_id = Math.random();
            random_speaker = (int) (random_speaker_id * 10 ) % sp_num;
            
// Randomly Show Speakers
            out.println("<tr><th colspan=\"4\" class=\"thead\">Keynote Speakers</th></tr>" + 
                        "<tr><td colspan=\"4\">&nbsp;</td></tr>" +
                        "<tr><td colspan=\"4\">There will be "+sp_num+" keynote speakers on the day. These will include:</td></tr>" +
                        "<tr><td colspan=\"4\">&nbsp;</td></tr>");  
            try {
                java.sql.Statement stmt = conn.createStatement();
                ResultSet speakers = stmt.executeQuery("SELECT * FROM Speakers WHERE speaker_id = "+anArray[random_speaker]+""); // Show random Speaker
                while (speakers.next()) {
                    speak_name = speakers.getString("speaker_fname") + " " + speakers.getString("speaker_lname");
                    speak_bio = speakers.getString("speaker_bio");
                    speak_pic = speakers.getString("speaker_pic");
                    
                out.println("<tr><td rowspan=\"2\"><img src="+speak_pic+" alt=\"Speaker Picture For "+speak_name+"\" id=\"img150\"></td><td class=\"tshead\" colspan=\"3\">"+speak_name+"</td></tr>" +
                            "<tr valign=\"top\"><th height=\"100\">About:</th><td colspan=\"2\" ><div class=\"scrollsp\">" + speak_bio + "</div></td></tr>");
                }
            }
            catch (Exception e){ System.err.println(e); }
            out.println("<tr><td class=\"tablebutton\" colspan=\"4\"><form><a href=\"show_speakers\" title=\"Go To Speakers Page\"><button name=\"btn_speakers\" value=\"OK\" type=\"button\">View All Speakers</button></a></form></td></tr>" +
            "<tr><td colspan=\"4\">&nbsp;</td></tr>");
            
// Show Workshops
            out.println("<tr><th colspan=\"4\" class=\"thead\">Workshops</th></tr>" + 
                        "<tr><td colspan=\"4\">&nbsp;</td></tr>" +
                        "<tr><td colspan=\"4\">"+workshop_count+" workshops have been scheduled for the event including: </td></tr>" +
                        "<tr><td colspan=\"4\">&nbsp;</td></tr>" +
                        "<tr><td colspan=\"4\"><div class=\"scrollws\"><table id=\"t100\">");  // Table in a table 
            try {
                java.sql.Statement stmt = conn.createStatement();
                ResultSet speakers = stmt.executeQuery("SELECT * FROM Workshops WHERE ws_name NOT LIKE 'Break'");
                
                while (speakers.next()) {
                    ws_id = speakers.getString("ws_id");
                    ws_name = speakers.getString("ws_name");
                    ws_pres1 = speakers.getString("ws_presenter1");
                    ws_pres2 = speakers.getString("ws_presenter2");
                    ws_info = speakers.getString("ws_info");
                 
                    out.println("<tr><th colspan=\"4\" class=\"tshead\">"+ws_name+"</th></tr>");    // heading
                        if (ws_pres2.contentEquals( "" )) out.println("<tr><th>Presenter:</th><td>"+ws_pres1+"</td><th></th><td></td></tr>");   // 2 presenters  
                        else out.println("<tr><th>Presenter 1:</th><td>"+ws_pres1+"</td><th>Presenter 2:</th><td>"+ws_pres2+"</td></tr>");  // 1 presenter  
                    
                    out.println("<tr><th>About:</th><td colspan=\"3\">"+ws_info+"</td></tr>" +  // line
                                "<tr><td colspan=\"4\">&nbsp;</td></tr>"); 
                }
            }
            catch (Exception e){ System.err.println(e); }
            out.println("</table></div></td></tr>" +
                            "<tr><td colspan=\"4\">&nbsp;</td></tr>" +
                            "<tr><td class=\"tablebutton\" colspan=\"4\">" +
                                "<form><a href=\"show_workshops\" title=\"Go To Workshops Page\"><button name=\"btn_ws\" value=\"OK\" type=\"button\">View All Workshops</button></a>" +
                                "</form></td></tr>" +
                            "<tr><td colspan=\"4\">&nbsp;</td></tr>");
            
// Count the number of Exhibitors
            try {
                java.sql.Statement stmt = conn.createStatement();
                ResultSet count_exhibitors = stmt.executeQuery("SELECT COUNT(*) AS ex_counter FROM Exhibitors;");
                count_exhibitors.next();        
                ex_count = count_exhibitors.getString("ex_counter");
            } catch (Exception e) { System.err.println(e); }
            
// Show Exhibitors
            out.println("<tr><th colspan=\"4\" class=\"thead\">Exhibitors</th></tr>" +
                        "<tr><td colspan=\"4\">&nbsp;</td></tr>" +
                        "<tr><td colspan=\"4\">There will also be "+ex_count+" exhibitors present at the event. Including the following: </td></tr>" +
                        "<tr><td colspan=\"4\">&nbsp;</td></tr>");  // Table in a table
                            try {
                                java.sql.Statement stmt = conn.createStatement();
                                ResultSet exhibitors = stmt.executeQuery("SELECT * FROM exhibitors ORDER BY exhibitor_lname LIMIT 2;"); // Select in ascending alphabetical order by last name
                                while (exhibitors.next()) {
                                    ex_name = exhibitors.getString("exhibitor_fname") + " " + exhibitors.getString("exhibitor_lname");
                                    ex_bio = exhibitors.getString("exhibitor_bio");
                                    ex_pic = exhibitors.getString("exhibitor_pic");

                                    out.println("<tr><td rowspan=\"3\"><img src="+ex_pic+" alt=\"Picture of "+ex_name+"\" id=\"img100\"></td><th class=\"tshead\" colspan=\"3\">"+ex_name+"</th></tr>" +                                             
                                                "<tr><th>About:</th><td colspan=\"2\">" + ex_bio + "</td></tr>" +
                                                "<tr><td colspan=\"4\">&nbsp;</td></tr>"); 
                                }
                            } 
                            catch (Exception e) { System.err.println(e); }
            out.println("<tr><td class=\"tablebutton\" colspan=\"4\">" +
                            "<form><a href=\"show_exhibitors\" title=\"Go To Exhibitors Page\"><button name=\"btn_exhibitors\" value=\"OK\" type=\"button\">View All Exhibitors</button></a>" +
                            "</form></td></tr>" +
                        "<tr><td colspan=\"4\">&nbsp;</td></tr>" +
                    "</table></div><br>");
 
// Bottom Links                    
            out.println("<div id=\"bl\" class=\"bottomlinks\">" +
                "<table align=\"center\">" +
                    "<tr><th>Display:</th><th>Register:</th><td rowspan=\"5\"><a align=\"left\" href=\"index\" title=\"Return To Homepage (Alt + 7)\" accesskey=\"7\"><img src=\"http://s21.postimg.org/gyukaf1l3/Logo.png\" alt=\"Event Logo\" id=\"img100\"></a></td></tr>" +
                    "<tr><td><a href=\"show_speakers\" title=\"Show Speakers (Alt + 1)\" accesskey=\"1\">1. Show Speakers</a></td><td><a href=\"reg_admin\" title=\"Administrator Registration Page (Alt + 5)\" accesskey=\"5\">5. Administrator Registration</a></td></tr>" +
                    "<tr><td><a href=\"show_workshops\" title=\"Show Workshops (Alt + 2)\" accesskey=\"2\">2. Show Workshops</a></td><td><a href=\"reg_attendee.html\" title=\"Attendee Registration Page (Alt + 6)\" accesskey=\"6\">6. Attendee Registration</a></td><td></td></tr>" +
                    "<tr><td><a href=\"show_schedule\" title=\"Show Schedule (Alt + 3)\" accesskey=\"3\">3. Show Schedule</a></td><td></td><td></td></tr>" +
                    "<tr><td><a href=\"show_exhibitors\" title=\"Show Exhibitors (Alt + 4)\" accesskey=\"4\">4. Show Exhibitors</a></td><td></td><td></td></tr>" +
                "</table>" +
            "</div>");
                                  
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
