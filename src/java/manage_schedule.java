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
@WebServlet(urlPatterns = {"/manage_schedule"})
public class manage_schedule extends HttpServlet {
    String[] times = {"08:00:00", "08:30:00", "09:00:00", "09:30:00", "10:00:00", "10:30:00", "11:00:00", "11:30:00", "12:00:00", "12:30:00", "13:00:00", "13:30:00", "14:00:00", "14:30:00", "15:00:00", "15:30:00", "16:00:00", "16:30:00", "17:00:00", "17:30:00"};
    //String[] status = {"","","","","disabled","","","","","","disabled","","","","","","disabled","","",""};
    //String[] status2 = {"disabled","disabled","disabled","disabled","disabled","disabled","disabled","disabled","disabled","disabled","disabled","disabled","disabled","disabled","disabled","disabled","disabled","disabled","disabled","disabled"};
    String[] status = new String[20];
    String[] status2 = new String[20];
    String[] status3 = new String[20];

    String[] Array_WS_names = {"","","","","","","","","","","","","","","","","","","",""};
    String[] Array_WS_values = {"","","","","","","","","","","","","","","","","","","",""};
    String[] workshopids = {"","","","","","","","","","","","","","","","","","","",""};
    String[] rooms = {"Room A101","Room A102","Room A103","Room B101","Room B102","Room B103","Room C101","Room C102","Room C103"};
    String breaktime = "disabled";
    String time800="08:00:00";  
    String time830="08:30:00";
    String slot800 = "";
    String slot830 = "";
    
    String sched_time;
    String ws_name;
    String sched_location;
    
    String location;
    String workshop_id;
    String speaker_fname;
    String speaker_name;
    String workshop_option ="";
    String workshop_name;
    String WSnameDisabled ="";
    
    // update
    String TimeToEdit="";
    
    // test variables
    int blah;
    String each_workshop_id;
    String test;
    
    Connection conn;
    PreparedStatement prepStat;
    Statement stat;
       
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
                    //ResultSet result = stmt.executeQuery("SELECT * FROM schedule");   
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
"                           <link rel=\"stylesheet\" type=\"text/css\" href=\"CAstyle.css\">");
            out.println("<title>Manage Schedules</title>");            
            out.println("</head>");
            out.println("<body>"
                        + "<div class=\"heading\">\n" +
"                           <br><h1 style=\"text-align:center\">Manage Schedule Details</h1><br>\n" +
                        "</div>"
                    
                    + "<div class=\"navigation\">\n" +
                "            <form style=\"display: inline\" action=\"show_schedule\" method=\"get\"><button name=\"buttonEventSchedule\" title=\"Event Schedule (Alt + 0)\">Event Schedule</button></form>\n" +
                "            <form style=\"display: inline\" action=\"manage_speakers\" method=\"get\"><button name=\"buttonSpeaker\" title=\"Add Speaker Details (Alt + 1)\">Add Speaker Details</button></form>\n" +
                "            <form style=\"display: inline\" action=\"manage_workshops\" method=\"get\"><button name=\"buttonWorkshop\" title=\"Add Workshop Details (Alt + 2)\">Add Workshop Details</button></form>\n" +
                "            <form style=\"display: inline\" action=\"manage_schedule\" method=\"get\"><button name=\"buttonSchedule\" title=\"Add Schedule Details (Alt + 3)\">Add Schedule Details</button></form>\n" +
                "            <form style=\"display: inline\" action=\"manage_exhibitors\" method=\"get\"><button name=\"buttonExhibitor\" title=\"Add Exhibitor Details (Alt + 4)\">Add Exhibitor Details</button></form> \n" +
                "            <form style=\"display: inline\" action=\"eventAdministration.html\" method=\"get\"><button name=\"buttonEventAdmin\" title=\"Return To Event Administration (Alt + 5)\">Event Administration</button></form>\n" +
                "        </div>"
                    
// Add to schedule
                    + "<div class=\"mainbody\">"
                    + "<h2>Add Workshop To Schedule</h2>");
                    try{
                        java.sql.Statement stmt = conn.createStatement(); 
                        //ResultSet result = stmt.executeQuery("SELECT * FROM Schedule");  
                        ResultSet schedule = stmt.executeQuery("SELECT schedule_time,schedule_location FROM Schedule");  
                        
                        //for(int j=0;j<9;j++)
                        //{
                            //ResultSet result = stmt.executeQuery("SELECT schedule_time,schedule_location FROM Schedule WHERE schedule_location LIKE " + rooms[j] + "");  
                            for(int i = 0; i <20; i++)
                            {
                                status[i]="";
                            }
                            while(schedule.next())
                            {
                                sched_time = schedule.getString("schedule_time");  
                                sched_location = schedule.getString("schedule_location");
                                //if (scheduletime.contentEquals("08:00:00")) slot800 = "disabled";
                                //if (scheduletime.contentEquals("08:30:00")) slot830 = "disabled";
                                for(int i = 0; i <20; i++)
                                {
                                    if (sched_time.contentEquals(times[i])) status[i] = "disabled"; // If the time is already in the schedule, disable to prevent double booking
                                    //else status[i] = "";
                                    
                                    //out.println("time: " + sched_time + " enabled? " + status2[i] + "<br>");
                                }
                                //out.println("time: " + scheduletime + "<br>");
                            }   
                    }
                    catch(Exception e)
                    {
                        System.err.println(e);
                    }   
                    //out.println("<br>END OF TEST");
                        
                out.println("<form action=\"add_schedule\" method=\"POST\"><br>" +
                "                <table align=\"center\">");
                
// Schedule Time (shedule_time)
                out.println("<tr>" +
                "               <th>Time:</th>\n" +
                "                   <td>\n" +
                "                       <!--Schedule Times (minus breaks)-->\n" +
                "                       <!--input list=\"schedule_time\" autofocus=\"autofocus\" name=\"schedule_time\" title=\"Select A Time From The List\"><datalist-->\n" +
                "                           <select autofocus=\"autofocus\" name=\"schedule_time\" title=\"Select A Time From The List\" id=\"schedule_time\" style=\"width:100%\">\n" +
                "                               <option value=\"08:00:00\" " + status[0] + ">8.00 am</option>" +
                "                               <option value=\"08:30:00\" " + status[1] + ">8.30 am</option>" +
                "                               <option value=\"09:00:00\" " + status[2] + ">9.00 am</option>" +
                "                               <option value=\"09:30:00\" " + status[3] + ">9.30 am</option>" +
                "                               <option value=\"10:00:00\" " + breaktime + ">10 am Break</option>" +
                "                               <option value=\"10:30:00\" " + status[5] + ">10.30 am</option>" +
                "                               <option value=\"11:00:00\" " + status[6] + ">11.00 am</option>" +
                "                               <option value=\"11:30:00\" " + status[7] + ">11.30 am</option>" +
                "                               <option value=\"12:00:00\" " + status[8] + ">12.00 pm</option>" +
                "                               <option value=\"12:30:00\" " + status[9] + ">12.30 pm</option>" +
                "                               <option value=\"13:00:00\" " + breaktime + ">1 pm Lunch</option>" +
                "                               <option value=\"14:00:00\" " + status[12] + ">2.00 pm</option>" +
                "                               <option value=\"14:30:00\" " + status[13] + ">2.30 pm</option>" +
                "                               <option value=\"15:00:00\" " + status[14] + ">3.00 pm</option>" +
                "                               <option value=\"15:30:00\" " + status[15] + ">3.30 pm</option>" +
                "                               <option value=\"16:00:00\" " + breaktime + ">4 pm Break</option>" +
                "                               <option value=\"16:30:00\" " + status[17] + ">4.30 pm</option>" +
                "                               <option value=\"17:00:00\" " + status[18] + ">5.00 pm</option>" +
                "                               <option value=\"17:30:00\" " + status[19] + ">5.30 pm</option>" +
                "                          </select></td>" +
                "           </tr>");
                
// Schedule Workshop Name (workshop_name)
/* */
/* */
                out.println("<th>Workshop:</th><td><select id=\"workshop_name\" name=\"workshop_name\" title=\"Select A Workshop From The List\" style=\"width:100%\">");
                try{
                    java.sql.Statement stmt = conn.createStatement(); 
                    //ResultSet workshop = stmt.executeQuery("SELECT ws_id,ws_name from Workshops");
                    ResultSet workshop = stmt.executeQuery("select ws_id,ws_name from workshops where ws_id not in (select workshop_id from schedule);"); // workshop ids not already in schedule
                    //ResultSet testresult = stmt.executeQuery("SELECT * FROM Schedule Where ws_id like('"+each_workshop_id+"')");                    
                    //workshop_id = workshop.getString("ws_id");
                        int nameCounter = 0;
                        //workshop_option ="";                        
                        for(int i = 0; i <20; i++)
                        {
                            status3[i]="";
                        }
                        while(workshop.next())
                        {
                            Array_WS_names[nameCounter] = workshop.getString("ws_name"); 
                            Array_WS_values[nameCounter] = workshop_id = workshop.getString("ws_id");                                           // add each workshop id to array list
                            // if Array_WS_values[nameCounter] = in "select workshop_id from shedule"
                            // WSnameDisabled = "Disabled";    
                            /*
                            try{
                                //ResultSet workshopid = stmt.executeQuery("SELECT * from Schedule");    // Get workshop id from db;
                                ResultSet workshopid = stmt.executeQuery("select ws_id  from workshops Join schedule where workshop_id = ws_id;");
                                //ResultSet testresult = stmt.executeQuery("SELECT * FROM Schedule Where ws_id like('"+each_workshop_id+"')");    

                                //workshop = stmt.getMoreResults();
                                while(workshopid.next())
                                {                                                    // add each workshop name to array list  
                                    each_workshop_id = workshop.getString("ws_id");
                                    each_workshop_id = workshop.getString("ws_id");
                                    for(int i = 0; i <20; i++)
                                    {
                                    if (each_workshop_id.contentEquals(Array_WS_values[i])) WSnameDisabled = "disabled";
                                    }
                                }
                             */
                            
                            out.println("<option value=\"" + Array_WS_values[nameCounter] + "\""+WSnameDisabled+">" + Array_WS_names[nameCounter] + "</option>"); // output each list item
                            nameCounter++;
                        }
                }
                catch(Exception e)
                {
                    System.err.println(e);
                }  
                out.println("</select></td><td></td></tr>"); 

/* */                
// Schedule Location Row (schedule_location)     
                out.println("<tr>\n" +
                "                        <th>Location:</th>\n" +
                "                        <!--td><input type=\"text\" name=\"schedule_location\" title=\"Enter location\"></td-->\n" +
                "                        <td><!--input list=\"schedule_location\" name=\"schedule_location\" title=\"Select A Location From The List\">" +
                "                              <datalist id=\"schedule_location\"-->\n" +
                "                            <select id=\"schedule_location\" name=\"schedule_location\" title=\"Select A Location From The List\" style=\"width:100%\">" +
                                                "<option value=\"" + rooms[0] +"\">" + rooms[0] +"</option>" +
                "                                <option value=\"" + rooms[1] +"\">" + rooms[1] +"</option>" +
                "                                <option value=\"" + rooms[2] +"\">" + rooms[2] +"</option>" +
                "                                <option value=\"" + rooms[3] +"\">" + rooms[3] +"</option>" +
                "                                <option value=\"" + rooms[4] +"\">" + rooms[4] +"</option>" +
                "                                <option value=\"" + rooms[5] +"\">" + rooms[5] +"</option>" +
                "                                <option value=\"" + rooms[6] +"\">" + rooms[6] +"</option>" +
                "                                <option value=\"" + rooms[7] +"\">" + rooms[7] +"</option>" +
                "                                <option value=\"" + rooms[8] +"\">" + rooms[8] +"</option>" +
                "                              </select></td><td></td>\n" +
                "                    </tr>");
                
// Submit Button Row
                out.println("<tr>" +
                "                        <td></td>" +
                "                    <td style=\"text-align:right\"><input type=\"submit\" value=\"Submit\" title=\"Submit Details\"/></td><td></td>\n" +
                "                    </tr>\n" +
                "                </table>\n" +
                "            </form><br>\n");
                
                out.println("<p>* Only <b>available</b> time slots can be selected</p>" +
                "        </div><br>");
                
/** Output the Schedule Table */                 
            out.println("<div class=\"mainbody\">" + 
                            "<h2>Current Schedule</h2>" +
                            "<p>A list of the workshops currently scheduled:</p>"
                            + "<table align=\"center\">"
                                + "<tr><th>Time</th><th>Name</th><th>Location</th>"
                                + "</tr>");
                                try {
                                    java.sql.Statement stmt = conn.createStatement();
                                    //ResultSet schedule = stmt.executeQuery("SELECT * from Schedule ORDER BY schedule_time ASC");  // Display table in order of scheduled_time
                                    ResultSet schedule = stmt.executeQuery("SELECT schedule_time,ws_name,schedule_location FROM Schedule JOIN Workshops ON Schedule.workshop_id = Workshops.ws_id ORDER BY schedule_time ASC");
                                    
                                    workshop_option = "";
                                    while (schedule.next()) {
                                        sched_time = schedule.getString("schedule_time");
                                        ws_name = schedule.getString("ws_name");
                                        sched_location = schedule.getString("schedule_location");
                                    out.println("<tr><td>" + sched_time + "</td><td>" + ws_name + "</td><td>" + sched_location + "</td></tr>");
                                        //out.println("time: " + sched_time + "<br>");
                                    }
                                } catch (Exception e) {
                                    System.err.println(e);
                                }
            out.println("</table></div><br>");
            
// Select record to delete
            try{
                java.sql.Statement stmt = conn.createStatement();            
                ResultSet schedule = stmt.executeQuery("SELECT schedule_time from Schedule");
                
                for (int i=0;i<20;i++){ status2[i]="disabled";}
                //boolean compare;
                blah = 1;
                while(schedule.next())
                {
                    sched_time = schedule.getString("schedule_time");  // get each record
                    //out.println("<br>time: " + sched_time + " " + blah);
                    blah++;
                    for(int i = 0; i <20; i++)
                        {
                            if (sched_time.contentEquals(times[i])) status2[i] = "";
                            //if(sched_time.contains(times[i])) status2[i]="";
                            //out.println("time: " + sched_time + " enabled? " + status2[i] + "<br>");
                        }
                }
            }
            catch(Exception e)
            {
                System.err.println(e);
            } 
                    
            out.println("<div class=\"mainbody\">"
                    +"<h2>Edit Schedule Details</h2>"
                    + "<p>Select time slot to delete</p>"
                    + "<form action=\"delete_schedule\" method=\"POST\">"
                    + "<table align=\"center\">"
                        + "<tr>" +
                "               <th>Time To Delete:</th>\n" +
                "                   <td><select name=\"deletetime\" title=\"Select A Time From The List\" style=\"width:100%\">\n" +
                "                               <option value=\"08:00:00\" "+status2[0]+">8.00 am</option>" +
                "                               <option value=\"08:30:00\" "+status2[1]+">8.30 am</option>" +
                "                               <option value=\"09:00:00\" "+status2[2]+">9.00 am</option>" +
                "                               <option value=\"09:30:00\" "+status2[3]+">9.30 am</option>" +
                "                               <option value=\"10:00:00\" " + breaktime + ">10 am Break</option>" +
                "                               <option value=\"10:30:00\" "+status2[5]+">10.30 am</option>" +
                "                               <option value=\"11:00:00\" "+status2[6]+">11.00 am</option>" +
                "                               <option value=\"11:30:00\" "+status2[7]+">11.30 am</option>" +
                "                               <option value=\"12:00:00\" "+status2[8]+">12.00 pm</option>" +
                "                               <option value=\"12:30:00\" "+status2[9]+">12.30 pm</option>" +
                "                               <option value=\"13:00:00\" " + breaktime + ">1 pm Lunch</option>" + // 1 hour for lunch
                "                               <option value=\"14:00:00\" "+status2[12]+">2.00 pm</option>" +
                "                               <option value=\"14:30:00\" "+status2[13]+">2.30 pm</option>" +
                "                               <option value=\"15:00:00\" "+status2[14]+">3.00 pm</option>" +
                "                               <option value=\"15:30:00\" "+status2[15]+">3.30 pm</option>" +
                "                               <option value=\"16:00:00\" " + breaktime + ">4 pm Break</option>" +
                "                               <option value=\"16:30:00\" "+status2[17]+">4.30 pm</option>" +
                "                               <option value=\"17:00:00\" "+status2[18]+">5.00 pm</option>" +
                "                               <option value=\"17:30:00\" "+status2[19]+">5.30 pm</option>" +
                "                          </select></td>" +
                                "<td style=\"text-align:right\"><input type=\"submit\" value=\"Delete\" title=\"Delete Time\"/></td>" +
                "           </tr>"
                    + "</table>" +
                        "</form>" +
                   "<p>* Only <b>booked</b> time slots can be deleted</p>" +
                "</div>");

// Navigation menu
            out.println("<div class=\"navigation\">" +
                "            <form style=\"display: inline\" action=\"reg_admin.html\" method=\"get\"><button name=\"buttonRegAdmin\" title=\"Administrator Registration Page (Alt + 6)\">Administrator Registration</button></form>\n" +
                "            <form style=\"display: inline\" action=\"reg_attendee.html\" method=\"get\"><button name=\"buttonRegAttendee\" title=\"Attendee Registration Page (Alt + 7)\">Attendee Registration</button></form>\n" +
                "            <form style=\"display: inline\" action=\"index.html\" method=\"get\"><button name=\"buttonHome\" title=\"Return To Home Page (Alt + 8)\">Return To Home Page</button></form>\n" +
                "        </div>");
// Bottom Links                    
            out.println("<div  id=\"bl\" class=\"bottomlinks\">" +
                "            <a class=\"class1\" href=\"show_schedule\" title=\"Event Schedule (Alt + 0)\" accesskey=\"0\">0. Event Schedule</a><br>" +
                "            <a href=\"manage_speakers\" title=\"Manage Speaker Details (Alt + 1)\" accesskey=\"1\">1. Manage Speaker Details</a><br>" +
                "            <a href=\"manage_workshops\" title=\"Manage Workshop Details (Alt + 2)\" accesskey=\"2\">2. Manage Workshop Details</a><br>" +
                "            <a href=\"manage_schedule\" title=\"Manage Schedule Details (Alt + 3)\" accesskey=\"3\">3. Manage Schedule Details</a><br>" +
                "            <a href=\"manage_exhibitors\" title=\"Manage Exhibitor Details (Alt + 4)\" accesskey=\"4\">4. Manage Exhibitor Details</a><br>" +
                "            <a href=\"eventAdministration.html\" title=\"Event Administration Page (Alt + 5)\" accesskey=\"5\">5. Event Administration</a><br>" +
                "            <a href=\"reg_admin.html\" title=\"Administrator Registration Page (Alt + 6)\" accesskey=\"1\">6. Administrator Registration</a><br>" +
                "            <a href=\"reg_attendee.html\" title=\"Attendee Registration Page (Alt + 7)\" accesskey=\"2\">7. Attendee Registration</a><br>" +
                "            <a href=\"index.html\" title=\"Return To Homepage (Alt + 8)\" accesskey=\"6\">8. Return To Home Page</a>" +
                "    </div>");
                        
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
