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

@WebServlet(urlPatterns = {"/eventschedule"})
public class eventschedule extends HttpServlet {
    String URL = "jdbc:mysql://localhost:3306/";                            // JDBC URL (sometimes 3307)
    String DB = "JoeCA";                                                    // DB must be created first
    String USERNAME = "root";                                               // Passwords different each machine
    String PASSWORD = "password";
    Connection conn = null;
    String scheduletime;
    String scheduletitle;
    String schedulelocation;
    String speakername;
    String speakersite;
    String checkboxname;

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
            response.setContentType("text/html;charset=UTF-8");
        
            PrintWriter out = response.getWriter();
            String title = "Event Schedule";
            String docType = "<!doctype html >";
            
            out.println(docType + "<html>\n" +
                    "<link rel=\"stylesheet\" type=\"text/css\" href=\"CAstyle.css\">" +
                    "<head class=\"heading\"><title>" + title + "</title></head>\n" +
                    "<body><h1 style=\"text-align:center\">" + title + "</h1>" +
                    "<div class=\"navigation\">\n" +
                        "<form style=\"display: inline\" action=\"eventschedule\" method=\"get\"><button name\"buttonEventAdmin\" title=\"Schedule (Alt + 1)\">Event Schedule</button></form>\n" +
                        "<form style=\"display: inline\" action=\"reg_admin.html\" method=\"get\"><button name=\"buttonRegAdmin\" title=\"Administrator Registration Page (Alt + 2)\">Administrator Registration</button></form>\n" +
                        "<form style=\"display: inline\" action=\"reg_attendee.html\" method=\"get\"><button name=\"buttonRegAttendee\" title=\"Attendee Registration Page (Alt + 3)\">Attendee Registration</button></form>\n" +
                        "<form style=\"display: inline\" action=\"index.html\" method=\"get\"><button name=\"buttonHome\" title=\"Return To Home Page (Alt + 5)\">Return To Home Page</button></form>\n" +
                    "</div>"
                    + "<h2 style=\"text-align:center\">Times of Events</h2>" +
                        "<form action=\"out_cust_schedule.html\" method=\"GET\"><br><table>");
            
            try{
            Class.forName("com.mysql.jdbc.Driver");
            conn = DriverManager .getConnection(URL + DB,USERNAME,PASSWORD);    // Establish/request a connection to a database
            Statement stmt = conn.createStatement(); 
            //ResultSet result = stmt.executeQuery("SELECT * FROM Schedule ORDER BY schedule_time");  
            //ResultSet result = stmt.executeQuery("SELECT * FROM Schedule JOIN Speakers ON Schedule.schedule_id = Speakers.speaker_event_id GROUP BY schedule.schedule_time");  
            //ResultSet result = stmt.executeQuery("SELECT distinct schedule_time, schedule_location, schedule_title, speaker_fname,speaker_lname FROM Schedule JOIN Speakers");  
            ResultSet result = stmt.executeQuery("SELECT * FROM Speakers join schedule \n" +
                                                 "where speakers.speaker_id = schedule.schedule_speaker_id\n" +
                                                 "group by schedule_time, schedule_title;");  
            
                out.println("<tr style=\"font-size:20px\"><td><b>Event Time</b></td><td><b>Event Title</b></td><td><b>Location</b></td><td><b>Attend</b></td></tr><tr><td></td></tr>");
                out.println("<tr><td><hr></td><td><hr></td><td><hr></td><td><hr></td></tr>");
            while(result.next())
            {
                scheduletime = result.getString("schedule_time");
                scheduletitle = result.getString("schedule_title");
                schedulelocation = result.getString("schedule_location");
                speakername = result.getString("speaker_fname") + " " + result.getString("speaker_lname");
                speakersite = result.getString("speaker_website1");
                checkboxname = result.getString("schedule_id");
                
                out.println("<tr style=\"font-size:20px\"><td>" + scheduletime + "</td><td>" + scheduletitle + "</td><td>" + schedulelocation + "</td><td><input type=\"checkbox\"/ name=" + checkboxname + "></td></tr>");
                out.println("<tr><td colspan=\"2\"><b>Speaker: </b>" + speakername + "</td><td></td><td></td></tr>");
                out.println("<tr><td colspan=\"2\"><b>Website: </b><a href=" + speakersite + ">" + speakersite + "</a></td><td></td><td></td></tr>");
                out.println("<tr><td><hr></td><td><hr></td><td><hr></td><td><hr></td></tr>");
            }
                out.println("<tr><td colspan=\"4\" style=\"text-align:right\"><input type=\"submit\" value=\"Submit Custom Time Table\" title=\"Submit Custom Table\"/></td></tr>");
                out.println("</table></form>"
                        + "<br><br><a href=\"eventschedule\" title=\"Event Schedule (Alt + 1)\" accesskey=\"1\">1. Event Schedule</a><br>" +
                            "<a href=\"reg_admin.html\" title=\"Administrator Registration Page (Alt + 2)\" accesskey=\"2\">2. Administrator Registration</a><br>" +
                            "<a href=\"reg_attendee.html\" title=\"Attendee Registration Page (Alt + 3)\" accesskey=\"3\">3. Attendee Registration</a><br>" +
                            "<a href=\"index.html\" title=\"Return To Homepage (Alt + 4)\" accesskey=\"4\">4. Return To Home Page</a><br>"
                        + "</body></html>");
        }
        catch(Exception e)
        {
            System.err.println(e);
        } 
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
