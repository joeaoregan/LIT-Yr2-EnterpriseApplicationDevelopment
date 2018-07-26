package joe.ead.front;

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
import joe.ead.abstracted.Connect; // 24/07/2018
import joe.ead.abstracted.Menu; // 26/07/2018

@WebServlet(urlPatterns = {"/show_cust_sched"})
public class show_cust_sched extends HttpServlet {
    String title = "Custom Schedule";
    String tableheading = "Selected Event Times";
    Connection conn = null; 
    com.mysql.jdbc.Statement stat;    
    private String scheduletime;
    private String schedulelocation;
    private String ws_id;
    private String workshopname;
    private String ws_pres1;
    private String ws_pres2;
    private String ws_info;
    private boolean checkFormat;
// Custom Schedule Variables
    String workshop_id;
    String list_item_name;
    String workshop_name;
// Output Custom Schedule
    String sched_time;
    String ws_name;
    String sched_location;
    
    public void init() throws ServletException {        
        try {
            Class.forName("com.mysql.jdbc.Driver");
            conn = (com.mysql.jdbc.Connection) DriverManager.getConnection (Connect.url+Connect.dbName,Connect.userName,Connect.password);
            stat = (com.mysql.jdbc.Statement) conn.createStatement();
        } catch (Exception e) {
            System.err.println(e);
        }
    } // end of init() method
    
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        PrintWriter out = response.getWriter();
        String docType = "<!doctype html >";
        Menu menu = new Menu();
        
        out.println(docType + "<html>" +
// Hide Non Printing Items
                "<style type=\"text/css\" media=\"print\">" +
                    ".dontprint" + "{ display: none; }" +
                "</style>" +

                "<link rel=\"stylesheet\" type=\"text/css\" href=\"CAstyle.css\">" +
                "<head ><title>" + title + "</title></head>"); 
        
        menu.headingDontPrint(request, out, title); // Heading (doesn't print)
        menu.navigationMenu(out, menu.HIGHLIGHT_CUST_SCHDULE, "navigation dontprint"); // Navigation menu

// Custom Event Schedule                    
        out.println("<div class=\"mainbody\">" +
                        "<form action=\"cust_schedule\" method=\"POST\"><br>" +
                            "<table align=\"center\">" +
                                "<tr><td class=\"tbhead\" colspan=\"2\">"+tableheading+"</td></tr>");

        try {
            Statement stmt = conn.createStatement();
            ResultSet custom_schedule = stmt.executeQuery("SELECT schedule_time,schedule.workshop_id,workshops.ws_name,schedule_location,ws_presenter1,ws_presenter2,ws_info FROM Schedule JOIN CustSched ON Schedule.workshop_id = CustSched.workshop_id JOIN Workshops ON Schedule.workshop_id = Workshops.ws_id AND Workshops.ws_id = CustSched.workshop_id ORDER BY schedule_time ASC;");

            while(custom_schedule.next()) {
                scheduletime = custom_schedule.getString("schedule_time");
                schedulelocation = custom_schedule.getString("schedule_location");
                ws_id = custom_schedule.getString("schedule.workshop_id");
                workshopname = custom_schedule.getString("workshops.ws_name");
                ws_pres1 = custom_schedule.getString("ws_presenter1");
                ws_pres2 = custom_schedule.getString("ws_presenter2");
                ws_info = custom_schedule.getString("ws_info");

                out.println("<tr class=\"thead\"><td>" + scheduletime + "</td><td>" + workshopname + "</td></tr>");

                checkFormat = workshopname.contentEquals( "Break" );                                                     // compare content of workshopname to "break"
                if (!checkFormat) {       
                    out.println("<tr><td><b>Location:</b></td><td>" + schedulelocation + "</td></tr>");
// presenter output
                    if (ws_pres2.contentEquals( "" )) { 
                        out.println("<tr><td><b>Presenter:</b></td><td>"+ws_pres1+"</td></tr>");                         // output if only 1 presenter   
                        out.println("<tr><td><b>About:</b></td><td>"+ws_info+"</td></tr>");                           
                    } else {
                        out.println("<tr><td><b>Presenters:</b></td><td>"+ws_pres1+" and "+ws_pres2+"</td></tr>");       // output if 2 presenters 
                        out.println("<tr><td><b>About:</b></td><td>"+ws_info+"</td></tr>");                           
                    }
                }
                out.println("<td colspan=\"2\">&nbsp;</td>");
            }
            out.println("</table></form>" +
                    "</div>");
        } catch(Exception e) {
            System.err.println(e);
        }

// Print Custom Schedule
        out.println("<div class=\"mainbody dontprint\" align=\"center\">" +
                        "<table align=\"center\"><tr>"
                        + "<td><button onclick=\"myFunction()\">Print this page</button>" +
                            "<script>" +
                                "function myFunction() {window.print();}"
                        + "</script></td>" +

// Return To Schedule
            "<td><form>" +
                "<a href=\"show_schedule#cs_add\" title=\"Return To Edit Schedule\"><button name=\"button\" value=\"OK\" type=\"button\">Return To Schedule</button></a>" +
            "</form>" +
            "</tr></table></td>" +
        "</div>");

        menu.bottomMenu(request,out); // Bottom Links 
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
