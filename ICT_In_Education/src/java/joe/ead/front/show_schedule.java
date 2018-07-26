/**
 *
 * @author Joe O'Regan
 * Student Number: K00203642
 */
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
import joe.ead.abstracted.Menu; // 24/07/2018

@WebServlet(urlPatterns = {"/show_schedule"})
public class show_schedule extends HttpServlet {
    String title = "Schedule";
    String tableheading = "Times of Events";
    Connection conn = null; 
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
    int sc_count; // count workshops in schedule db
    int cust_sc_count;
    
    public void init() throws ServletException {
        try{
            Class.forName("com.mysql.jdbc.Driver");
            conn = (com.mysql.jdbc.Connection) DriverManager.getConnection (Connect.url+Connect.dbName,Connect.userName,Connect.password);
            stat = (com.mysql.jdbc.Statement) conn.createStatement();
        } catch (Exception e) {
            System.err.println(e);
        }
    } // end of init() method
    

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
        throws ServletException, IOException {
        
        Menu menu = new Menu();
            
        response.setContentType("text/html;charset=UTF-8");
        
        PrintWriter out = response.getWriter();
        String docType = "<!doctype html >";
            
        out.println(docType + "<html>" +
// Hide Non Printing Items
                "<style type=\"text/css\" media=\"print\">" +
                    ".dontprint" + "{ display: none; }" +
                "</style>" +

                "<link rel=\"stylesheet\" type=\"text/css\" href=\"css/CAstyle.css\">" +
                "<head ><title>" + title + "</title></head><body>");
            
        menu.heading(request, out, title); // Page Heading
        menu.navigationMenu(out, menu.SHOW_SCHEDULE, "navigation"); // Navigation menu

// Number of workshops in schedule
        try {
            sc_count = 0;
            java.sql.Statement stmt = conn.createStatement();
            ResultSet sc = stmt.executeQuery("SELECT count(*) AS sched_count FROM Schedule WHERE workshop_id != 1");
            while (sc.next()) {
            sc_count = sc.getInt("sched_count");
            }
        } catch (Exception e) {
            System.err.println(e);
        } 
// Cust schedule count
        try {
            cust_sc_count = 0;
            java.sql.Statement stmt = conn.createStatement();
            ResultSet csc = stmt.executeQuery("SELECT count(*) AS cust_sched_count FROM CustSched");
            while (csc.next()) {
            cust_sc_count = csc.getInt("cust_sched_count");
            }
        } catch (Exception e) {
            System.err.println(e);
        } 
            
// Show Schedule Info              
        if (sc_count == 0) {
            out.println("<div class=\"mainbody\"><br><h2>There are currently no workshops with a scheduled time.</h2>");
            out.println("<h3>Register for updates<h3>"
                    + "<form action=\"reg_attendee.html\" method=\"get\"><button name=\"buttonRegAttendee\" title=\"Attendee Registration Page (Alt + 6)\">Register</button></form></div>");
        } else {   
// Event Schedule                         
            out.println("<div class=\"mainbody\">" +
                            "<table align=\"center\">" +
                                "<tr><td class=\"mainhead dontprint\" colspan=\"3\">"+tableheading+"</td></tr>" +
                                "<tr class=\"dontprint\"><td class=\"mainbase\" colspan=\"3\"><b>On This Page:<br></b>The full schedule of events, with a print option. <br>Also a <a href=\"#cs_add\">custom schedule editor</a></td></tr>" +
                                "<tr class=\"dontprint\"><td colspan=\"3\">&nbsp;</td></tr>" +
                                "<tr><td class=\"thead\" colspan=\"3\">Workshop Times</td></tr>");
            
            try {
                Statement stmt = conn.createStatement();
                ResultSet result = stmt.executeQuery("SELECT schedule_time,ws_id,ws_name,schedule_location,ws_presenter1,ws_presenter2,ws_info FROM Schedule JOIN Workshops ON Schedule.workshop_id = Workshops.ws_id ORDER BY schedule_time;");

                checkboxno = 1;
                while(result.next()) {
                    scheduletime = result.getString("schedule_time");
                    schedulelocation = result.getString("schedule_location");
                    ws_id = result.getString("ws_id");
                    workshopname = result.getString("ws_name");
                    ws_pres1 = result.getString("ws_presenter1");
                    ws_pres2 = result.getString("ws_presenter2");
                    ws_info = result.getString("ws_info");
                    
                    out.println("<tr class=\"thead\"><td>" + scheduletime + "</td><td>" + workshopname + "</td></tr>");
                    
                    checkFormat = workshopname.contentEquals( "Break" );                                                // compare content of workshopname to "break"
                    
                    if (!checkFormat) {                                                                                  // don't output presenters for breaks
                        out.println("<tr><td><b>Location:</b></td><td>" + schedulelocation + "</td></tr>");
// Presenter output                      
                        if (ws_pres2.contentEquals( "" )) { 
                            out.println("<tr><td><b>Presenter:</b></td><td>"+ws_pres1+"</td></tr>");                    // output if only 1 presenter   
                            out.println("<tr><td><b>About:</b></td><td>"+ws_info+"</td></tr>");                           
                        } else {
                            out.println("<tr><td><b>Presenters:</b></td><td>"+ws_pres1+" and "+ws_pres2+"</td></tr>");  // output if 2 presenters 
                            out.println("<tr><td><b>About:</b></td><td>"+ws_info+"</td></tr>");                           
                        }
                    }
                    out.println("<tr><td class=\"dontprint\" colspan=\"2\">&nbsp;</td></tr>");                          // remove space from print
                }
// Print Full Schedule
                out.println("<tr><td class=\"tbase\" colspan=\"2\"><div class=\"dontprint\" align=\"center\">"
                + "<button onclick=\"myFunction()\">Print The Schedule</button>" +
                    "<script>"
                + "function myFunction() {window.print();} "
                + "</script>"
                + "</div></td></tr></table></div><br>");
            } catch(Exception e) {
                System.err.println(e);
            }

// Custom schedule
            out.println("<div class=\"mainbody dontprint\">" +
                    "<table align=\"center\" id=\"sp0\">" + // CS group table
                        "<tr><th colspan=\"3\" class=\"mainhead\" id=\"cs_add\">Edit Custom Schedule</th></tr>" +
                        "<tr class=\"tbody\"><td colspan=\"3\">"); // end form add to CS
            
/** Output the Custom Schedule DB */         
            if (cust_sc_count>0) { // Only show CS table if something in it
                out.println("<table align=\"center\" id=\"cs_table\" width=\"100%\">" + // CS table
                                //"<tr><td class=\"tbhead\" colspan=\"3\">Custom Schedule</td></tr>" +
                                "<tr><td class=\"thead\" colspan=\"3\">Custom Schedule</td></tr>" +
                                "<tr class=\"tbody\"><td colspan=\"3\">A list of the workshops in the custom schedule:</td></tr>" +
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
                } catch (Exception e) {
                    System.err.println(e);
                }

                out.println("<tr><td class=\"tbase\" colspan=\"3\"><form><a href=\"show_cust_sched\" title=\"View Full Custom Schedule\"><button name=\"btn_full_cust_sched\" value=\"OK\" type=\"button\">View Full Custom Schedule</button></a></form></td></tr></table>"); // end ouput CS table      end table 2
// Go To Full Custom Schedule
                //if(cust_sc_count>0) out.println("<form><a href=\"show_cust_sched\" title=\"View Full Custom Schedule\"><button name=\"btn_full_cust_sched\" value=\"OK\" type=\"button\">View Full Custom Schedule</button></a></form><br>");
                out.println("</td></tr><tr class=\"tbody\"><td colspan=\"3\" >"); // end table 1
            } // End nly show CS if something in custom schedule  
            
// Edit custom schedule
            out.println("<table align=\"center\">" +
                            "<tr><td class=\"thead\" colspan=\"3\">Add / Remove Workshops</td></tr>");
// Add to cs               
            out.println("<tr><td colspan=\"3\" id=\"cs_add\">Select a workshop from the list to add to your custom schedule</td></tr>" +
                        "<tr><td colspan=\"3\">&nbsp;</td></tr><form action=\"add_cust_sched\" method=\"POST\">" +     
                        "<tr><th>Add Workshop:</th><td><select name=\"custom_schedule\" title=\"Select A Workshop From The List\">");

            try {
                java.sql.Statement stmt = conn.createStatement();            
                ResultSet schedule = stmt.executeQuery("SELECT schedule_time, workshop_id,ws_name FROM Schedule JOIN Workshops ON Schedule.workshop_id = Workshops.ws_id WHERE workshop_id NOT IN (SELECT workshop_id FROM CustSched) AND workshop_id != 1 ORDER BY schedule_time;");

                while(schedule.next()) {
                    workshop_id = schedule.getString("workshop_id");
                    workshop_name = schedule.getString("ws_name");
                    list_item_name = schedule.getString("schedule_time") + " " +schedule.getString("ws_name");
                    checkFormat = workshop_name.contentEquals( "Break" );   // compare content of workshopname to "break"

                    if( !checkFormat ) {           
                        out.println("<option value=\""+workshop_id+"\">"+list_item_name+"</option>");
                    }
                }
            } catch(Exception e) {
                System.err.println(e);
            }
            
            out.println("</select>" // end drop down menu options
                        + "</td><td id=\"bt\"><input type=\"submit\" style=\"width:170px\" value=\"Add Workshop\" title=\"Add To Custom Schedule\"/></td>"
                        + "</form></tr>"); // end form add to CS            
// Delete from CS
            if (cust_sc_count>0) { // Only show delete CS option if something in table
                out.println("<tr class=\"tbody\"><td colspan=\"3\"><form action=\"delete_cust_sched\" method=\"POST\">" +
                            "<tr><td colspan=\"3\">&nbsp;</td></tr>" +
                            "<tr><td colspan=\"3\">Select a workshop from the list to remove from your custom schedule</td></tr>" +
                            "<tr><td colspan=\"3\">&nbsp;</td></tr>" +        
                            "<tr><th>Delete Workshop:</th><td><select name=\"cust_sched_delete\" title=\"Select A Workshop From The List\">");
                try {
                    java.sql.Statement stmt = conn.createStatement();            
                    ResultSet schedule = stmt.executeQuery("SELECT schedule_time,schedule.workshop_id,workshops.ws_name,schedule_location FROM Schedule JOIN CustSched ON Schedule.workshop_id = CustSched.workshop_id JOIN Workshops ON Schedule.workshop_id = Workshops.ws_id AND Workshops.ws_id = CustSched.workshop_id ORDER BY schedule_time ASC;");
                    
                    while(schedule.next()) {
                        workshop_id = schedule.getString("workshop_id");
                        workshop_name = schedule.getString("ws_name");
                        list_item_name = schedule.getString("schedule_time") + " " +schedule.getString("ws_name");
                        out.println("<option value=\""+workshop_id+"\">"+list_item_name+"</option>");
                    }
                } catch(Exception e) {
                    System.err.println(e);
                }

                out.println("</select></td>" +
                                "<td id=\"bt\"><input type=\"submit\" style=\"width:170px\" value=\"Delete Workshop\" title=\"Delete From Custom Schedule\"/></td></tr></form>" +
                            "<tr><td colspan=\"3\">&nbsp;</td></tr>"); // End Delete workshop from CS
    // Clear CS
                out.println("<tr><td colspan=\"2\">Clear The Custom Schedule</td><td><form action=\"clear_cust_sched\" method=\"POST\">" +
                                "<input type=\"submit\" style=\"width:170px\" value=\"Clear Custom Schedule\" title=\"Clear Custom Schedule\"/></form>");
            } // End only show delete CS option if something in table  

            out.println("<tr><td class=\"tbody\" colspan=\"3\">&nbsp;</td></tr>");  
            out.println("<tr><td class=\"tbase\" colspan=\"3\">&nbsp;</td></tr>");  
            out.println("</td></tr></table></td></tr>");
            out.println("<tr><td class=\"tbase\" colspan=\"3\"><form><a href=\"#top\" title=\"Top of page\"><button name=\"button\" value=\"OK\" type=\"button\">Top of Page</button></a></form>"
                        + "</td></tr></table></div>"); // end dont print div
        }   // End else (schedule details)         

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
