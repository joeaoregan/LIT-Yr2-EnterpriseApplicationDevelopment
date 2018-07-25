package joe.lit.ead;
import joe.ait.cse.Connect;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

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

/**
 *
 * @author Joe O'Regan
 * Student Number: K00203642
 */
@WebServlet(urlPatterns = {"/manage_schedule"})
public class manage_schedule extends HttpServlet {
    String title = "Manage Schedule";
    String[] times = {"08:00:00", "08:30:00", "09:00:00", "09:30:00", "10:00:00", "10:30:00", "11:00:00", "11:30:00", "12:00:00", "12:30:00",
                      "13:00:00", "13:30:00", "14:00:00", "14:30:00", "15:00:00", "15:30:00", "16:00:00", "16:30:00", "17:00:00", "17:30:00", "18:00:00"};
    // set list option as enabled / disabled
    String[] status = new String[21]; // time slot to add
    String[] status2 = new String[20]; // time slot to delete
    // Add to schedule list items
    //String[] Array_WS_names = {"","","","","","","","","","","","","","","","","","","",""};
    //String[] Array_WS_values = {"","","","","","","","","","","","","","","","","","","",""};
    String[] rooms = {"Room A101","Room A102","Room A103","Room B101","Room B102","Room B103","Room C101","Room C102","Room C103"}; // schedule_location
    String breaktime = "disabled"; // make breaks times unselectable - added to Schedule table when initialised
    // Output table
    String sched_time;
    String ws_name;
    String sched_location;
// Workshop details    
    String workshop_id;
    String workshop_name;
    String wsn;
    int wsi;
    
    // available timeslot count
    int time_slots = 0;
    int slots_remaining =19;
    int sc_count; // number of workshops in schedule database
    
    Connection conn;
    Statement stat;
       
    public void init() throws ServletException
    {
        try{
            Class.forName("com.mysql.jdbc.Driver");
            conn = (Connection) DriverManager.getConnection (Connect.url+Connect.dbName,Connect.userName,Connect.password);
            stat = (Statement) conn.createStatement();

            stat.execute("CREATE TABLE IF NOT EXISTS Workshops(ws_id INT PRIMARY KEY AUTO_INCREMENT, ws_name VARCHAR(60) NOT NULL, ws_presenter1 CHAR(40) NOT NULL, ws_presenter2 CHAR(40), ws_info TEXT NOT NULL)");
            stat.execute("CREATE TABLE IF NOT EXISTS Schedule(schedule_time TIME PRIMARY KEY, workshop_id INT NOT NULL, schedule_location CHAR(40), CONSTRAINT fk_shedule_workshop FOREIGN KEY (workshop_id) REFERENCES workshops (ws_id))");
            stat.execute("INSERT INTO Workshops VALUES(1, 'Break','none','none','Break Times:\n8am Begin\n10 a.m. - 10.30 a.m. Break\n1 p.m. - 2 p.m. Lunch\n4 p.m. - 4.30 p.m. Break');");
            stat.execute("INSERT INTO schedule VALUES('100000', 1, 'Break')");
            stat.execute("INSERT INTO schedule VALUES('130000', 1, 'Break')");
            stat.execute("INSERT INTO schedule VALUES('160000', 1, 'Break')");
            stat.execute("CREATE TABLE IF NOT EXISTS CustSched(workshop_id INT PRIMARY KEY, CONSTRAINT fk_custsched_workshop FOREIGN KEY (workshop_id) REFERENCES workshops (ws_id));");
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
                            "<link rel=\"stylesheet\" type=\"text/css\" href=\"CAstyle.css\">" +
                            "<title>"+title+"</title>" +
                        "</head>" +
                    "<body>");
// Heading
            out.println("<div class=\"heading\">" +
                        "<table>" +
                            "<tr><td><div class=\"logo\"><a align=\"left\" href=\"index\" title=\"Return To Homepage (Alt + 7)\" accesskey=\"7\">" +
                                "<img src='" + request.getContextPath() + "/images/logoT.png' alt=\"Event Logo\" id=\"img150\"></a></div></td>" +
                                "<td><h1>" + title + "</h1></td></tr>" +
                        "</table>" +
                    "</div>");
            
// Navigation menu (Schedule Highlighted)
            out.println("<div class=\"navigation\"><span>" +
                            "<form action=\"show_schedule\" method=\"get\"><button name=\"buttonEventSchedule\" title=\"Event Schedule (Alt + 3)\">Event Schedule</button></form>" +
                            "<form action=\"manage_speakers\" method=\"get\"><button name=\"buttonSpeaker\" title=\"Add Speaker Details (Alt + h)\">Manage Speakers</button></form>" +
                            "<form action=\"manage_workshops\" method=\"get\"><button name=\"buttonWorkshop\" title=\"Add Workshop Details (Alt + j)\">Manage Workshops</button></form>" +
                            "<form action=\"manage_schedule\" method=\"get\"><button name=\"buttonSchedule\" id=\"active\" title=\"Add Schedule Details (Alt + k)\">Manage Schedule</button></form>" +
                            "<form action=\"manage_exhibitors\" method=\"get\"><button name=\"buttonExhibitor\" title=\"Add Exhibitor Details (Alt + l)\">Manage Exhibitors</button></form>" +
                            "<form action=\"index\" method=\"get\"><button name=\"buttonHome\" title=\"Return To Homepage (Alt + 7)\">Home</button></form>" +
                        "<span></div>");
// Number of workshops in schedule
            try {
                sc_count = 0;
                time_slots = 0;
                
                java.sql.Statement stmt = conn.createStatement();
                ResultSet sc = stmt.executeQuery("SELECT count(*) AS sched_count FROM Schedule WHERE workshop_id != 1");
                while (sc.next()) {
                sc_count = sc.getInt("sched_count");
                }
                
                ResultSet schedule = stmt.executeQuery("SELECT count(*) AS count FROM Schedule");   
                while(schedule.next()){
                    time_slots = schedule.getInt("count");
                }   
            }
            catch(Exception e) { System.err.println(e); }  
            //out.println("<div>WS count: "+time_slots+"</div>");
            
            if(time_slots==0) // nothing in schedule
            {
                out.println("<div class=\"mainbody\">" +
                                "<h2 class=\"tbhead\">Initialise Workshop And Schedule Table</h2>" +
                                "<p>Sets up the workshops, schedule, and custom schedule tables, by 1st creating the tables, and then adding the break times" +
                                "<form action=\"init_sched\" method=\"get\"><button name=\"buttonEventSchedule\" title=\"Initialise the schedule table\">Initialise Schedule Table</button></form>" +
                            "</div><br>");
            }
// Add to schedule
// Select the workshop from a drop down list to add to schedule, only the workshops not in the schedule can be selected
            if(time_slots > 0)
            {
            try{
                java.sql.Statement stmt = conn.createStatement(); 
                ResultSet schedule = stmt.executeQuery("SELECT schedule_time,schedule_location FROM Schedule");  
                for(int i = 0; i <20; i++) // initialise / reset all list items to enabled
                {
                    status[i]="";
                }
                while(schedule.next())
                {
                    sched_time = schedule.getString("schedule_time");  
                    sched_location = schedule.getString("schedule_location");
                    for(int i = 0; i <21; i++)
                    {
                        if (sched_time.contentEquals(times[i])) status[i] = "disabled"; // If the time is already in the schedule, disable to prevent double booking
                    }
                }   
            }
            catch(Exception e) { System.err.println(e); } 
            
            slots_remaining = 19 - time_slots; // total slots (19 incl 1 hour lunch) - slots already in schedule table
            out.println("<div id=\"addws\" class=\"mainbody\">" +
                            "<h2 class=\"tbhead\">Add Workshop To Schedule</h2>" +
                            "<p>There are currently "+slots_remaining+" time slots available"); 
            out.println("<form action=\"add_schedule\" method=\"POST\"><br>" +
                            "<table align=\"center\">");
                
// Schedule Time (shedule_time)
            out.println("<tr><th>Time:</th>" +
                            "<td><select autofocus=\"autofocus\" name=\"schedule_time\" title=\"Select A Time From The List\" id=\"schedule_time\">");
                                // List that makes only unscheduled time slots available to select, but lets you see all time slots including breaktimes
                                // Break times are added to schedule when it is initialised (Click button on manage_schedule page)
                                for(int i=0;i<21;i++){
                                    if(i !=4 & i!=10 & i!=11 & i!=16 & i!=20) 
                                        out.println("<option value=\""+times[i]+"\" " + status[i] + ">"+times[i]+"</option>");
                                    if(i==4 || i==10 || i==16) 
                                        out.println("<option value=\""+times[i]+"\" disabled>"+times[i]+" Break</option>"); // make breaktimes visibile but not selectable in list
                                    if(i==20) out.println("<option value=\""+times[i]+"\" disabled>"+times[i]+" Event Finished</option>");
                                }
                                    
            out.println("</select></td></tr>");
                
// Schedule Workshop Name (workshop_name)
                out.println("<tr><th>Workshop:</th><td><select id=\"workshop_name\" name=\"workshop_name\" title=\"Select A Workshop From The List\" required>");
                try{
                    java.sql.Statement stmt = conn.createStatement(); 
                    ResultSet workshop = stmt.executeQuery("select ws_id,ws_name from workshops where ws_id not in(select workshop_id from schedule) AND ws_id != 1"); // workshop ids not already in schedule
                        int nameCounter = 0;  
                        while(workshop.next())
                        {
                            //Array_WS_names[nameCounter] = workshop.getString("ws_name");  // Add each workshop name to array list
                            //Array_WS_values[nameCounter] = workshop_id = workshop.getString("ws_id");  // Add each workshop id to array list
                            wsn = workshop.getString("ws_name");
                            wsi = workshop.getInt("ws_id");
                            
                            //out.println("<option value=\"" + Array_WS_values[nameCounter] + "\">" + Array_WS_names[nameCounter] + "</option>");  // Output each list item
                            out.println("<option value=\"" + wsi + "\">" + wsn + "</option>");  // Output each list item
                            nameCounter++;
                        }
                }
                catch(Exception e)
                {
                    System.err.println(e);
                }  
                out.println("</select></td></tr>"); 
                
// Schedule Location Row (schedule_location)     
                out.println("<tr><th>Location:</th>" +
                                "<td><select id=\"schedule_location\" name=\"schedule_location\" title=\"Select A Location From The List\">");
                                    for (int i = 0; i<9; i++) // output each element of the room array
                                    {
                                        out.println("<option value=\"" + rooms[i] +"\">" + rooms[i] +"</option>");
                                    }
                                    out.println("</select></td></tr>");
// Submit Button Row
                out.println("<tr><td colspan=\"2\"  id=\"bt\"><input type=\"submit\" value=\"Submit\" title=\"Submit Details\"/></td></tr>" +
                        "</table>" +
                    "</form><br>");
                
                out.println("<p>* Only <b>available</b> time slots can be selected</p>" +
                            "</div><br>");
                
/** Output the Schedule Table */                 
            out.println("<div class=\"mainbody\">" + 
                            "<h2 class=\"tbhead\">Current Schedule</h2>" +
                            "<p>A list of the workshops currently scheduled:" +
                            "<table align=\"center\">" +
                                "<tr><th>Time</th><th>Name</th><th>Location</th></tr>");
                                try {
                                    java.sql.Statement stmt = conn.createStatement();
                                    ResultSet schedule = stmt.executeQuery("SELECT schedule_time,ws_name,schedule_location FROM Schedule JOIN Workshops ON Schedule.workshop_id = Workshops.ws_id ORDER BY schedule_time ASC");
                                    
                                    while (schedule.next()) {
                                        sched_time = schedule.getString("schedule_time");
                                        ws_name = schedule.getString("ws_name");
                                        sched_location = schedule.getString("schedule_location");
                                        if (ws_name.contentEquals("Break")) sched_location = ""; // Leave location blank for break
                                    out.println("<tr><td>" + sched_time + "</td><td>" + ws_name + "</td><td>" + sched_location + "</td></tr>");
                                    }
                                } catch (Exception e) {
                                    System.err.println(e);
                                }
            out.println("</table></div><br>");
            
// Select record to delete
            try{ // Initialise the list
                java.sql.Statement stmt = conn.createStatement();            
                ResultSet schedule = stmt.executeQuery("SELECT schedule_time from Schedule");
                
                for (int i=0;i<20;i++){ status2[i]="disabled";} // set each array element to disabled (unable to select)
                while(schedule.next())
                {
                    sched_time = schedule.getString("schedule_time");  // get each record
                    for(int i = 0; i <20; i++)
                        {
                            if (sched_time.contentEquals(times[i])) status2[i] = "";
                        }
                }
            }
            catch(Exception e)
            {
                System.err.println(e);
            }   
            out.println("<div class=\"mainbody\">" + // Display the list of times to delete
                            "<h2 class=\"tbhead\">Edit Schedule Details</h2>" +
                            "<p>Select time slot to delete" +
                            "<form action=\"delete_schedule\" method=\"POST\">" +
                            "<table align=\"center\">" +
                                "<tr><th>Time To Delete:</th>" +
                                    "<td><select name=\"deletetime\" title=\"Select A Time From The List\">" +
                                        // List option without a loop (obviously)
                                        "<option value=\"08:00:00\" "+status2[0]+">8.00 am</option>" +
                                        "<option value=\"08:30:00\" "+status2[1]+">8.30 am</option>" +
                                        "<option value=\"09:00:00\" "+status2[2]+">9.00 am</option>" +
                                        "<option value=\"09:30:00\" "+status2[3]+">9.30 am</option>" +
                                        "<option value=\"10:00:00\" " + breaktime + ">10 am Break</option>" + // break times already initialised
                                        "<option value=\"10:30:00\" "+status2[5]+">10.30 am</option>" +
                                        "<option value=\"11:00:00\" "+status2[6]+">11.00 am</option>" +
                                        "<option value=\"11:30:00\" "+status2[7]+">11.30 am</option>" +
                                        "<option value=\"12:00:00\" "+status2[8]+">12.00 pm</option>" +
                                        "<option value=\"12:30:00\" "+status2[9]+">12.30 pm</option>" +
                                        "<option value=\"13:00:00\" " + breaktime + ">1 pm Lunch</option>" + // 1 hour for lunch
                                        "<option value=\"14:00:00\" "+status2[12]+">2.00 pm</option>" +
                                        "<option value=\"14:30:00\" "+status2[13]+">2.30 pm</option>" +
                                        "<option value=\"15:00:00\" "+status2[14]+">3.00 pm</option>" +
                                        "<option value=\"15:30:00\" "+status2[15]+">3.30 pm</option>" +
                                        "<option value=\"16:00:00\" " + breaktime + ">4 pm Break</option>" +
                                        "<option value=\"16:30:00\" "+status2[17]+">4.30 pm</option>" +
                                        "<option value=\"17:00:00\" "+status2[18]+">5.00 pm</option>" +
                                        "<option value=\"17:30:00\" "+status2[19]+">5.30 pm</option>" +
                                    "</select></td>" +
                                    "<td id=\"bt\"><input type=\"submit\" value=\"Delete\" title=\"Delete Time\"/></form></td></tr>" +
                                    "<tr><td colspan=\"2\">&nbsp</td></tr>"); // end of delete form
// Clear The Schedule            
                    out.println("<tr><th colspan=\"2\">Clear The Schedule</th><td><form action=\"clear_schedule\" method=\"POST\">" +
                                    "<input type=\"submit\" value=\"Clear Schedule\" title=\"Clear Schedule\"/></form>"); // end of clear form
                    out.println("</table>" +
                            "</form>" +
                            "<p>* Only <b>booked</b> time slots can be deleted</p>" +
                        "</div>");
            } // Display workshops if workshops in table   
            
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
                                    + "<td></tr>" +
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
