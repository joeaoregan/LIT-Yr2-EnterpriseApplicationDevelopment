import com.mysql.jdbc.PreparedStatement;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet(urlPatterns = {"/show_schedule"})
public class show_schedule extends HttpServlet {
    String title = "Schedule";
    String tableheading = "Times of Events";
    Connection conn = null; 
    PreparedStatement prepStat;
    com.mysql.jdbc.Statement stat;    
    String scheduletime;
    String schedulelocation;
    String ws_id;
    String workshopname;
    String ws_pres1;
    String ws_pres2;
    String ws_info;
    String checkboxname;
    int checkboxno;
    String checkboxvisible;
    boolean checkFormat;
// Custom Schedule Variables
    String workshop_id;
    String list_item_name;
    String workshop_name;
// Output Custom Schedule
    String sched_time;
    String ws_name;
    String sched_location;
    
    public void init() throws ServletException
    {
        String url = "jdbc:mysql://localhost:3306/";
        String dbName = "JoeCA";
        String userName = "root";
        String password = "password";
        
        try{
            Class.forName("com.mysql.jdbc.Driver");
            conn = (Connection) DriverManager.getConnection
                    (url+dbName,userName,password);
            stat = (com.mysql.jdbc.Statement) conn.createStatement();
        } catch (Exception e) 
        {
            System.err.println(e);
        }
    } // end of init() method
    

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
            response.setContentType("text/html;charset=UTF-8");
        
            PrintWriter out = response.getWriter();
            String docType = "<!doctype html >";
            
            out.println(docType + "<html>" +
// Hide Non Printing Items
                    "<style type=\"text/css\" media=\"print\">" +
                        ".dontprint" + "{ display: none; }" +
                    "</style>" +
                    
                    "<link rel=\"stylesheet\" type=\"text/css\" href=\"CAstyle.css\">" +
                    "<head ><title>" + title + "</title></head>");
            
            out.println("<body>" +
                        "<div class=\"heading dontprint\">" +
                        "<table>" +
                            "<tr><td><a align=\"left\" href=\"index\" title=\"Return To Homepage (Alt + 7)\" accesskey=\"7\"><img src=\"http://s21.postimg.org/gyukaf1l3/Logo.png\" alt=\"Event Logo\" id=\"img150\"></a></td>" +
                            "<td><h1>" + title + "</h1></td></tr>" +
                        "</table>" +
                        "</div>");
// Navigation menu
            out.println("<div class=\"navigation dontprint\"><span>" +
                            "<form action=\"show_speakers\" method=\"get\"><button name=\"buttonSpeakers\" title=\"Event Speakers (Alt + 1)\">Speakers</button></form>" +
                            "<form action=\"show_workshops\" method=\"get\"><button name=\"buttonWorkshops\" title=\"Event Workshops (Alt + 2)\">Workshops</button></form>" +
                            "<form action=\"show_schedule\" method=\"get\"><button id=\"active\" name=\"buttonSchedule\" title=\"Event Schedule (Alt + 3)\">Schedule</button></form>" +
                            "<form action=\"show_exhibitors\" method=\"get\"><button name=\"buttonExhibitors\" title=\"Event Exhibitors (Alt + 4)\">Exhibitors</button></form>" +
                            "<form action=\"reg_admin\" method=\"get\"><button name=\"buttonRegAdmin\" title=\"Administrator Registration Page (Alt + 5)\">Administrator Registration</button></form>" +
                            "<form action=\"reg_attendee.html\" method=\"get\"><button name=\"buttonRegAttendee\" title=\"Attendee Registration Page (Alt + 6)\">Attendee Registration</button></form>" +
                            "<form action=\"index\" method=\"get\"><button name=\"buttonHome\" title=\"Return To Homepage (Alt + 7)\">Home</button></form>" +
                        "</span></div>");
// Info                    
            out.println("<div class=\"mainbody dontprint\">" +
                        "</div>");
// Event Schedule                         
            out.println("<div class=\"mainbody\">" +
                            "<table align=\"center\">" +
                                "<tr><td class=\"tbhead\" colspan=\"3\">"+tableheading+"</td></tr>" +
                                "<tr class=\"dontprint\"><td colspan=\"3\">&nbsp;</td></tr>" +
                                "<tr class=\"dontprint\"><td colspan=\"3\"><b>On This Page: </b>The full schedule of events, with a print option. Also a <a href=\"#cs_add\">custom schedule editor</a></p></td></tr>" +
                                "<tr class=\"dontprint\"><td colspan=\"3\">&nbsp;</td></tr>");
            
            try{
                Statement stmt = conn.createStatement();
                ResultSet result = stmt.executeQuery("SELECT schedule_time,ws_id,ws_name,schedule_location,ws_presenter1,ws_presenter2,ws_info FROM Schedule JOIN Workshops ON Schedule.workshop_id = Workshops.ws_id ORDER BY schedule_time;");

                checkboxno = 1;
                while(result.next())
                {
                    scheduletime = result.getString("schedule_time");
                    schedulelocation = result.getString("schedule_location");
                    ws_id = result.getString("ws_id");
                    workshopname = result.getString("ws_name");
                    ws_pres1 = result.getString("ws_presenter1");
                    ws_pres2 = result.getString("ws_presenter2");
                    ws_info = result.getString("ws_info");
                    
                    out.println("<tr class=\"thead\"><td>" + scheduletime + "</td><td>" + workshopname + "</td></tr>");
                    
                    checkFormat = workshopname.contentEquals( "Break" );                                                                  // compare content of workshopname to "break"
                    
                    if(!checkFormat)                                                                                                            // don't output presenters for breaks
                    {       
                        out.println("<tr><td><b>Location:</b></td><td>" + schedulelocation + "</td></tr>");
                        
                        if (ws_pres2.contentEquals( "" )) 
                        { 
                            out.println("<tr><td><b>Presenter:</b></td><td>"+ws_pres1+"</td></tr>");                                   // output if only 1 presenter   
                            out.println("<tr><td><b>About:</b></td><td>"+ws_info+"</td></tr>");                           
                        }
                        else 
                        {
                            out.println("<tr><td><b>Presenters:</b></td><td>"+ws_pres1+" and "+ws_pres2+"</td></tr>");        // output if 2 presenters 
                            out.println("<tr><td><b>About:</b></td><td>"+ws_info+"</td></tr>");                           
                        }
                    }
                    out.println("<td class=\"dontprint\" colspan=\"2\">&nbsp;</td>");  // remove space from print
                }
                out.println("</table></div>");
        } 
        catch(Exception e)
        {
            System.err.println(e);
        }
            
// Print Full Schedule
out.println("<div class=\"mainbody dontprint\" align=\"center\">"
                + "<button onclick=\"myFunction()\">Print The Schedule</button>" +
                    "<script>"
                + "function myFunction() {window.print();} "
                + "</script>"
            + "</div><br>");

// Choose custom schedule
            out.println("<div class=\"mainbody dontprint\">"
                        +"<h2 id=\"cs_add\">Choose Custom Schedule</h2>"
                        + "<p>Select a workshop from the list to add to your custom schedule</p>"
                        + "<form action=\"cust_schedule_add\" method=\"POST\">"
                        + "<table align=\"center\">"
                            + "<tr>" +
                                "<th>Select Workshop:</th>" +
                                "<td><select name=\"custom_schedule\" title=\"Select A Workshop From The List\">");
            
            try{
                java.sql.Statement stmt = conn.createStatement();            
                ResultSet schedule = stmt.executeQuery("SELECT schedule_time, workshop_id,ws_name FROM Schedule JOIN Workshops ON Schedule.workshop_id = Workshops.ws_id WHERE workshop_id NOT IN (SELECT workshop_id FROM CustSched) AND workshop_id != 1 ORDER BY schedule_time;");
                
                while(schedule.next())
                {
                    workshop_id = schedule.getString("workshop_id");
                    workshop_name = schedule.getString("ws_name");
                    list_item_name = schedule.getString("schedule_time") + " " +schedule.getString("ws_name");
                    checkFormat = workshop_name.contentEquals( "Break" );   // compare content of workshopname to "break"
                    
                    if( !checkFormat )
                    {           
                        out.println("<option value=\""+workshop_id+"\">"+list_item_name+"</option>");
                    }
                }
            }
            catch(Exception e) { System.err.println(e); }
            
            out.println("</select></td>" +
                                "<td id=\"bt\"><input type=\"submit\" value=\"Add To Custom Schedule\" title=\"Add To Custom Schedule\"/></td>" +
                        "</tr>" +
                    "</table>" +
                    "</form>" +
                "</div><br>");
                
/** Output the Custom Schedule Table */                 
            out.println("<div id=\"cs_table\" class=\"mainbody dontprint\">" + 
                            "<table align=\"center\">" +
                                "<tr><td class=\"tbhead\" colspan=\"3\">Custom Schedule</td></tr>" +
                                "<tr><td colspan=\"3\">&nbsp;</td></tr>" +
                                "<tr><td colspan=\"3\">A list of the workshops in the custom schedule:</td></tr>" +
                                "<tr><td colspan=\"3\">&nbsp;</td></tr>" +                 
                                "<tr class=\"thead\"><th>Time</th><th>Name</th><th>Location</th>" +
                                "</tr>");
                                try {
                                    java.sql.Statement stmt = conn.createStatement();
                                    ResultSet schedule = stmt.executeQuery("SELECT schedule_time,schedule.workshop_id,workshops.ws_name,schedule_location FROM Schedule JOIN CustSched ON Schedule.workshop_id = CustSched.workshop_id JOIN Workshops ON Schedule.workshop_id = Workshops.ws_id AND Workshops.ws_id = CustSched.workshop_id ORDER BY schedule_time ASC;");
                                    
                                    while (schedule.next()) {
                                        sched_time = schedule.getString("schedule_time");
                                        ws_name = schedule.getString("ws_name");
                                        sched_location = schedule.getString("schedule_location");
                                        out.println("<tr><td>" + sched_time + "</td><td>" + ws_name + "</td><td>" + sched_location + "</td></tr>");
                                    }
                                } catch (Exception e) { System.err.println(e); }
            out.println("</table><br>");
            
// Go To Full Custom Schedule
            out.println("<form><a href=\"show_cust_sched\" title=\"View Full Custom Schedule\"><button name=\"btn_full_cust_sched\" value=\"OK\" type=\"button\">View Full Custom Schedule</button></a></form>" +
                        "</div><br>");
            
// Edit custom schedule
            out.println("<div class=\"mainbody dontprint\">" +
                        "<form action=\"cust_schedule_delete\" method=\"POST\">" +
                        "<table align=\"center\">" +
                            "<tr><td class=\"tbhead\" colspan=\"3\">Edit Custom Schedule</td></tr>" +
                            "<tr><td colspan=\"3\">&nbsp;</td></tr>" +
                            "<tr><td colspan=\"3\">Select a workshop from the list to remove from your custom schedule</td></tr>" +
                            "<tr><td colspan=\"3\">&nbsp;</td></tr>" +        
                            "<tr><th>Select Workshop:</th><td><select name=\"cust_sched_delete\" title=\"Select A Workshop From The List\">");
            try{
                java.sql.Statement stmt = conn.createStatement();            
                ResultSet schedule = stmt.executeQuery("SELECT schedule_time,schedule.workshop_id,workshops.ws_name,schedule_location FROM Schedule JOIN CustSched ON Schedule.workshop_id = CustSched.workshop_id JOIN Workshops ON Schedule.workshop_id = Workshops.ws_id AND Workshops.ws_id = CustSched.workshop_id ORDER BY schedule_time ASC;");
                while(schedule.next())
                {
                    workshop_id = schedule.getString("workshop_id");
                    workshop_name = schedule.getString("ws_name");
                    list_item_name = schedule.getString("schedule_time") + " " +schedule.getString("ws_name");
                    out.println("<option value=\""+workshop_id+"\">"+list_item_name+"</option>");
                }
            }
            catch(Exception e) { System.err.println(e); }
            
            out.println("</select></td>" +
                            "<td id=\"bt\"><input type=\"submit\" value=\"Delete From Custom Schedule\" title=\"Delete From Custom Schedule\"/></td>" +
                        "</tr></table></form><br>");
            
// Clear The Custom Schedule
            out.println("<form action=\"custom_schedule_clear\" method=\"POST\">" +
                            "<input type=\"submit\" value=\"Clear Custom Schedule\" title=\"Clear Custom Schedule\"/>" +
                        "</form><br>");  
            
// Return To Top Of Page            
            out.println("<form align=\"center\"><a href=\"#top\" title=\"Top of page\"><button name=\"button\" value=\"OK\" type=\"button\">Top of Page</button></a></form></div>");
            
// Bottom Links                    
            out.println("<div id=\"bl\" class=\"bottomlinks dontprint\">\n" +
                            "<table align=\"center\">\n" +
                                "<tr><th>Display:</th><th>Register:</th><th>Other:</th><tr>\n" +
                                "<tr><td><a href=\"show_speakers\" title=\"Show Speakers (Alt + 1)\" accesskey=\"1\">1. Show Speakers</a></td><td><a href=\"reg_admin\" title=\"Administrator Registration Page (Alt + 5)\" accesskey=\"5\">5. Administrator Registration</a></td><td><a href=\"index\" title=\"Return To Homepage (Alt + 7)\" accesskey=\"7\">7. Home Page</a></td></tr>" +
                                "<tr><td><a href=\"show_workshops\" title=\"Show Workshops (Alt + 2)\" accesskey=\"2\">2. Show Workshops</a></td><td><a href=\"reg_attendee.html\" title=\"Attendee Registration Page (Alt + 6)\" accesskey=\"6\">6. Attendee Registration</a></td><td></td></tr>" +
                                "<tr><td><a href=\"show_schedule\" title=\"Show Schedule (Alt + 3)\" accesskey=\"3\">3. Show Schedule</a></td><td></td><td></td></tr>" +
                                "<tr><td><a href=\"show_exhibitors\" title=\"Show Exhibitors (Alt + 4)\" accesskey=\"4\">4. Show Exhibitors</a></td><td></td><td></td></tr>" +
                            "</table>" +
                        "</div>");
            out.println("</body></html>");
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>

}
