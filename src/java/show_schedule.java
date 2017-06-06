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
    String title = "Event Schedule";
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
            
            out.println(docType + "<html>\n" +
                    "<style>" +
                        "a:hover {background-color: activecaption;}" +
                        "button:hover {color: blue; background-color: highlight;}" +
                    "</style>" +
                    "<link rel=\"stylesheet\" type=\"text/css\" href=\"CAstyle.css\">" +
                    "<head ><title>" + title + "</title></head>" +
                    "<body>" +
                        "<div class=\"heading\">" +
                            "<br><h1 style=\"text-align:center\">" + title + "</h1><br>" +
                        "</div>");
// Navigation menu
            out.println("<div class=\"navigation\">" +
                        "<form style=\"display: inline\" action=\"show_speakers\" method=\"get\"><button name=\"buttonSpeakers\" title=\"Event Speakers (Alt + 1)\">Event Speakers</button></form>" +
                        "<form style=\"display: inline\" action=\"show_workshops\" method=\"get\"><button name=\"buttonWorkshops\" title=\"Event Workshops (Alt + 2)\">Event Workshops</button></form>" +
                        "<form style=\"display: inline\" action=\"show_schedule\" method=\"get\"><button style=\"color: blue; background-color: white;\" name=\"buttonSchedule\" title=\"Event Schedule (Alt + 3)\">Event Schedule</button></form>" +
                        "<form style=\"display: inline\" action=\"show_exhibitors\" method=\"get\"><button name=\"buttonExhibitors\" title=\"Event Exhibitors (Alt + 4)\">Event Exhibitors</button></form>" +
                        "<form style=\"display: inline\" action=\"reg_admin.html\" method=\"get\"><button name=\"buttonRegAdmin\" title=\"Administrator Registration Page (Alt + 5)\">Administrator Registration</button></form>" +
                        "<form style=\"display: inline\" action=\"reg_attendee.html\" method=\"get\"><button name=\"buttonRegAttendee\" title=\"Attendee Registration Page (Alt + 6)\">Attendee Registration</button></form>" +
                        "<form style=\"display: inline\" action=\"index.html\" method=\"get\"><button name=\"buttonHome\" title=\"Return To Homepage (Alt + 7)\">Home</button></form>" +
                    "</div>");
                    
            out.println("<div class=\"mainbody\">" +
                            "<h2 style=\"text-align:center\">Times of Events</h2>" +
                            "<form action=\"cust_schedule\" method=\"POST\"><br>"
                    + "<table align=\"center\">");
            
            try{
                Statement stmt = conn.createStatement();
                ResultSet result = stmt.executeQuery("SELECT schedule_time,ws_id,ws_name,schedule_location,ws_presenter1,ws_presenter2,ws_info FROM Schedule JOIN Workshops ON Schedule.workshop_id = Workshops.ws_id ORDER BY schedule_time;");

                //out.println("<tr style=\"font-size:20px\"><td><b>Event Time</b></td><td><b>Event Title</b></td><td><b>Location</b></td><td><b>Attend</b></td></tr><tr><td></td></tr>");
                //out.println("<tr><td><hr></td><td><hr></td><td><hr></td><td><hr></td></tr>");
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
                    checkboxname = "btn" + checkboxno;
                    
                    out.println("<tr class=\"thead\"><td>" + scheduletime + "</td><td>" + workshopname + "</td>");
                    
                    checkFormat = workshopname.contentEquals( "Break" );                                                                  // compare content of workshopname to "break"
                    
                    if( checkFormat )
                    {
                        //checkboxvisible = "<td></td><td></td>";                                                                               // don't output checkbox for breaks
                        checkboxvisible = "<td></td>";
                    out.println(""+ checkboxvisible +"</tr>");                    
                    //out.println("<tr><td></td><td>" + schedulelocation + "</td><td></td></tr>");
                    }
                    else 
                    {
                        //checkboxvisible ="<td>" + schedulelocation + "</td><td><input type=\"checkbox\"/ name=" + checkboxname + "></td>";
                        checkboxvisible ="<td><input type=\"checkbox\"/ name=" + checkboxname + "></td>";
                    out.println(""+ checkboxvisible +"</tr>");                    
                    out.println("<tr><td><b>Location:</b></td><td>" + schedulelocation + "</td><td></td></tr>");
                    }
                    
                    if(!checkFormat)                                                                                                            // don't output presenters for breaks
                    {
                        if (ws_pres2.contentEquals( "" )) 
                        {
                            //out.println("<tr><td><b>Presenter:</b></td><td>"+ws_pres1+"</td><td></td><td></td></tr>");                        // output if only 1 presenter   
                            //out.println("<tr><td><b>About:</b></td><td colspan=\"3\">"+ws_info+"</td></tr>");    
                            out.println("<tr><td><b>Presenter:</b></td><td>"+ws_pres1+"</td><td></td></tr>");                                   // output if only 1 presenter   
                            out.println("<tr><td><b>About:</b></td><td colspan=\"2\">"+ws_info+"</td></tr>");                           
                        }
                        else 
                        {
                            //out.println("<tr><td><b>Presenters:</b></td><td>"+ws_pres1+" and "+ws_pres2+"</td><td></td><td></td></tr>");      // output if 2 presenters 
                            //out.println("<tr><td><b>About:</b></td><td colspan=\"3\">"+ws_info+"</td></tr>"); 
                            out.println("<tr><td><b>Presenters:</b></td><td>"+ws_pres1+" and "+ws_pres2+"</td><td></td></tr>");        // output if 2 presenters 
                            out.println("<tr><td><b>About:</b></td><td colspan=\"2\">"+ws_info+"</td></tr>");                           
                        }
                    }
                    //out.println("<tr><td colspan=\"4\">"+checkboxname+"</td></tr>"); checkboxno++; //test
                    //out.println("<tr><td><b>Location:<\b></td><td>" + schedulelocation + "</td><td></td><td></td></tr>");
                    //out.println("<tr><td colspan=\"4\"><hr></td></tr>");
                    out.println("<td colspan=\"3\">&nbsp;</td>");
                }
                out.println("<tr><td colspan=\"4\" style=\"text-align:right\"><input type=\"submit\" value=\"Submit Custom Time Table\" title=\"Submit Custom Table\"/></td></tr>");
                out.println("</table></form><br>" +
                        "</div>");
// Navigation menu
            out.println("<div class=\"navigation\">" +
                        "<form style=\"display: inline\" action=\"show_speakers\" method=\"get\"><button name=\"buttonSpeakers\" title=\"Event Speakers (Alt + 1)\">Event Speakers</button></form>" +
                        "<form style=\"display: inline\" action=\"show_workshops\" method=\"get\"><button name=\"buttonWorkshops\" title=\"Event Workshops (Alt + 2)\">Event Workshops</button></form>" +
                        "<form style=\"display: inline\" action=\"show_schedule\" method=\"get\"><button style=\"color: blue; background-color: white;\" name=\"buttonSchedule\" title=\"Event Schedule (Alt + 3)\">Event Schedule</button></form>" +
                        "<form style=\"display: inline\" action=\"show_exhibitors\" method=\"get\"><button name=\"buttonExhibitors\" title=\"Event Exhibitors (Alt + 4)\">Event Exhibitors</button></form>" +
                        "<form style=\"display: inline\" action=\"reg_admin.html\" method=\"get\"><button name=\"buttonRegAdmin\" title=\"Administrator Registration Page (Alt + 5)\">Administrator Registration</button></form>" +
                        "<form style=\"display: inline\" action=\"reg_attendee.html\" method=\"get\"><button name=\"buttonRegAttendee\" title=\"Attendee Registration Page (Alt + 6)\">Attendee Registration</button></form>" +
                        "<form style=\"display: inline\" action=\"index.html\" method=\"get\"><button name=\"buttonHome\" title=\"Return To Homepage (Alt + 7)\">Home</button></form>" +
                    "</div>");
// Bottom Links                    
            out.println("<div id=\"bl\" class=\"bottomlinks\">\n" +
                "<table align=\"center\">\n" +
                    "<tr><th>Display:</th><th>Register:</th><th>Other:</th><tr>\n" +
                    "<tr><td><a href=\"show_speakers\" title=\"Show Speakers (Alt + 1)\" accesskey=\"1\">1. Show Speakers</a></td><td><a href=\"reg_admin.html\" title=\"Administrator Registration Page (Alt + 5)\" accesskey=\"5\">5. Administrator Registration</a></td><td><a href=\"index.html\" title=\"Return To Homepage (Alt + 7)\" accesskey=\"7\">7. Home Page</a></td></tr>" +
                    "<tr><td><a href=\"show_workshops\" title=\"Show Workshops (Alt + 2)\" accesskey=\"2\">2. Show Workshops</a></td><td><a href=\"reg_attendee.html\" title=\"Attendee Registration Page (Alt + 6)\" accesskey=\"6\">6. Attendee Registration</a></td><td></td></tr>" +
                    "<tr><td><a href=\"show_schedule\" title=\"Show Schedule (Alt + 3)\" accesskey=\"3\">3. Show Schedule</a></td><td></td><td></td></tr>" +
                    "<tr><td><a href=\"show_exhibitors\" title=\"Show Exhibitors (Alt + 4)\" accesskey=\"4\">4. Show Exhibitors</a></td><td></td><td></td></tr>" +
                "</table>" +
            "</div>");
            
            out.println("</body></html>");
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
