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
 * @author Joe O'Regan Student Number: K00203642
 */
@WebServlet(urlPatterns = {"/out_schedule"})
public class out_schedule extends HttpServlet {

    String[] times = {"08:00:00", "08:30:00", "09:00:00", "09:30:00", "10:00:00", "10:30:00", "11:00:00", "11:30:00", "12:00:00", "12:30:00", "13:00:00", "13:30:00", "14:00:00", "14:30:00", "15:00:00", "15:30:00", "16:00:00", "16:30:00", "17:00:00", "17:30:00"};
    String[] status = {"", "", "", "", "disabled", "", "", "", "", "", "disabled", "", "", "", "", "", "disabled", "", "", ""};
    String[] rooms = {"Room A101", "Room A102", "Room A103", "Room B101", "Room B102", "Room B103", "Room C101", "Room C102", "Room C103"};
    String breaktime = "disabled";
    String time800 = "08:00:00";
    String time830 = "08:30:00";
    String slot800 = "";
    String slot830 = "";
    String sched_time;
    String sched_title;
    String sched_location;
    String location;
    String speaker_id;
    String speaker_fname;
    String speaker_name;
    String workshop_option = "";
    String workshop_name;

    Connection conn;
    PreparedStatement prepStat;
    Statement stat;

    public void init() throws ServletException {
        String url = "jdbc:mysql://localhost:3306/";
        String dbName = "JoeCA";
        String userName = "root";
        String password = "password";
        try {
            Class.forName("com.mysql.jdbc.Driver");
            conn = (Connection) DriverManager.getConnection(url + dbName, userName, password);
            stat = (Statement) conn.createStatement();

            java.sql.Statement stmt = conn.createStatement();
            //ResultSet result = stmt.executeQuery("SELECT * FROM schedule");   
        } catch (Exception e) {
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

        //sched_location = request.getParameter("schedule_location");
        response.setContentType("text/html;charset=UTF-8");

        try (PrintWriter out = response.getWriter()) {
            out.println("<!DOCTYPE html>"
                    + "<html>"
                    + "<head>"
                    + "                           <link rel=\"stylesheet\" type=\"text/css\" href=\"CAstyle.css\">");
            out.println("<title>Edit Schedule</title>");
            out.println("</head>");
            out.println("<body>"
                    + "<div class=\"heading\">\n"
                    + "                           <br><h1 style=\"text-align:center\">Schedule Details</h1><br>\n"
                    + "</div>"
                    + "<div class=\"navigation\">\n"
                    + "            <form style=\"display: inline\" action=\"eventschedule\" method=\"get\"><button name=\"buttonEventSchedule\" title=\"Event Schedule (Alt + 0)\">Event Schedule</button></form>\n"
                    + "            <form style=\"display: inline\" action=\"in_speakers.html\" method=\"get\"><button name=\"buttonSpeaker\" title=\"Add Speaker Details (Alt + 1)\">Add Speaker Details</button></form>\n"
                    + "            <form style=\"display: inline\" action=\"in_workshops.html\" method=\"get\"><button name=\"buttonWorkshop\" title=\"Add Workshop Details (Alt + 2)\">Add Workshop Details</button></form>\n"
                    + "            <form style=\"display: inline\" action=\"in_schedule\" method=\"get\"><button name=\"buttonSchedule\" title=\"Add Schedule Details (Alt + 3)\">Add Schedule Details</button></form>\n"
                    + "            <form style=\"display: inline\" action=\"in_exhibitors.html\" method=\"get\"><button name=\"buttonExhibitor\" title=\"Add Exhibitor Details (Alt + 4)\">Add Exhibitor Details</button></form> \n"
                    + "            <form style=\"display: inline\" action=\"eventAdministration.html\" method=\"get\"><button name=\"buttonEventAdmin\" title=\"Return To Event Administration (Alt + 5)\">Event Administration</button></form>\n"
                    + "        </div>"
                    + "<div class=\"mainbody\">");

            try {
                java.sql.Statement stmt = conn.createStatement();
                //ResultSet result = stmt.executeQuery("SELECT * FROM Schedule");  
                ResultSet schedule = stmt.executeQuery("SELECT schedule_time,schedule_location FROM Schedule");

                //for(int j=0;j<9;j++)
                //{
                //ResultSet result = stmt.executeQuery("SELECT schedule_time,schedule_location FROM Schedule WHERE schedule_location LIKE " + rooms[j] + "");  
                while (schedule.next()) {
                    sched_time = schedule.getString("schedule_time");
                    sched_location = schedule.getString("schedule_location");
                    //if (scheduletime.contentEquals("08:00:00")) slot800 = "disabled";
                    //if (scheduletime.contentEquals("08:30:00")) slot830 = "disabled";
                    for (int i = 0; i < 20; i++) {
                        if (sched_time.contentEquals(times[i])) {
                            status[i] = "disabled"; // If the time is already in the schedule, disable to prevent double booking
                        }
                    }
                    //out.println("time: " + scheduletime + "<br>");
                }
                //}    
            } catch (Exception e) {
                System.err.println(e);
            }
            //out.println("<br>END OF TEST");

            out.println("<form action=\"schedule\" method=\"POST\"><br>\n"
                    + "                <table>\n"
                    + "                    <tr>\n"
                    + "                        <th>Time:</th>\n"
                    + "                        <td>\n"
                    + "                            <!--Schedule Times (minus breaks)-->\n"
                    + "                            <!--input list=\"schedule_time\" autofocus=\"autofocus\" name=\"schedule_time\" title=\"Select A Time From The List\"><datalist-->\n"
                    + "                              <select autofocus=\"autofocus\" name=\"schedule_time\" title=\"Select A Time From The List\" id=\"schedule_time\" style=\"width:100%\">\n"
                    + "                                <option value=\"08:00:00\" " + status[0] + ">8.00 am</option>\n"
                    + "                                <option value=\"08:30:00\" " + status[1] + ">8.30 am</option>\n"
                    + "                                <option value=\"09:00:00\" " + status[2] + ">9.00 am</option>\n"
                    + "                                <option value=\"09:30:00\" " + status[3] + ">9.30 am</option>\n"
                    + "                                <option value=\"10:00:00\" " + breaktime + ">10 am Break</option>\n"
                    + "                                <option value=\"10:30:00\" " + status[5] + ">10.30 am</option>\n"
                    + "                                <option value=\"11:00:00\" " + status[6] + ">11.00 am</option>\n"
                    + "                                <option value=\"11:30:00\" " + status[7] + ">11.30 am</option>\n"
                    + "                                <option value=\"12:00:00\" " + status[8] + ">12.00 pm</option>\n"
                    + "                                <option value=\"12:30:00\" " + status[9] + ">12.30 pm</option>\n"
                    + "                                <option value=\"13:00:00\" " + breaktime + ">1 pm Lunch</option>\n"
                    + "                                <option value=\"14:00:00\" " + status[12] + ">2.00 pm</option>\n"
                    + "                                <option value=\"14:30:00\" " + status[13] + ">2.30 pm</option>\n"
                    + "                                <option value=\"15:00:00\" " + status[14] + ">3.00 pm</option>\n"
                    + "                                <option value=\"15:30:00\" " + status[15] + ">3.30 pm</option>\n"
                    + "                                <option value=\"16:00:00\" " + breaktime + ">4 pm Break</option>\n"
                    + "                                <option value=\"16:30:00\" " + status[17] + ">4.30 pm</option>\n"
                    + "                                <option value=\"17:00:00\" " + status[18] + ">5.00 pm</option>\n"
                    + "                                <option value=\"17:30:00\" " + status[19] + ">5.30 pm</option>\n"
                    + "                              </select></td>"
                    + "                    </tr>\n"
                    + "                        <th>Workshop:</th>\n");

            try {
                java.sql.Statement stmt = conn.createStatement();
                //ResultSet speaker = stmt.executeQuery("SELECT * from Speakers");
                ResultSet workshop = stmt.executeQuery("SELECT * from Workshops");
                //ResultSet speaker = stmt.executeQuery("SELECT speaker_id,speaker_fname,speaker_lname from Speakers");

                workshop_option = "";
                while (workshop.next()) {
                    workshop_name = workshop.getString("ws_name");
                    workshop_option = workshop_option.concat("<option value=\"" + workshop_name + "\">" + workshop_name + "</option>");
                    //out.println("workshop: " + workshop_name + "<br>");

                }
            } catch (Exception e) {
                System.err.println(e);
            }
            out.println("<td><select id=\"workshop_name\" name=\"workshop_name\" title=\"Select A Workshop From The List\" style=\"width:100%\">" + workshop_option + "</select></td><td></td>\n");

            out.println("</tr>\n"
                    + "                    <tr>\n"
                    + "                        <th>Location:</th>\n"
                    + "                        <!--td><input type=\"text\" name=\"schedule_location\" title=\"Enter location\"></td-->\n"
                    + "                        <td><!--input list=\"schedule_location\" name=\"schedule_location\" title=\"Select A Location From The List\">"
                    + "                              <datalist id=\"schedule_location\"-->\n"
                    + "                            <select id=\"schedule_location\" name=\"schedule_location\" title=\"Select A Location From The List\" style=\"width:100%\">"
                    + "<option value=\"" + rooms[0] + "\">" + rooms[0] + "</option>"
                    + "                                <option value=\"" + rooms[1] + "\">" + rooms[1] + "</option>"
                    + "                                <option value=\"" + rooms[2] + "\">" + rooms[2] + "</option>"
                    + "                                <option value=\"" + rooms[3] + "\">" + rooms[3] + "</option>"
                    + "                                <option value=\"" + rooms[4] + "\">" + rooms[4] + "</option>"
                    + "                                <option value=\"" + rooms[5] + "\">" + rooms[5] + "</option>"
                    + "                                <option value=\"" + rooms[6] + "\">" + rooms[6] + "</option>"
                    + "                                <option value=\"" + rooms[7] + "\">" + rooms[7] + "</option>"
                    + "                                <option value=\"" + rooms[8] + "\">" + rooms[8] + "</option>"
                    + "                              </select></td><td></td>\n"
                    + "                    </tr>"
                    + "                    <tr>"
                    + "                        <td></td>"
                    + "                    <td style=\"text-align:right\"><input type=\"submit\" value=\"Submit\" title=\"Submit Details\"/></td><td></td>\n"
                    + "                    </tr>\n"
                    + "                </table>\n"
                    + "            </form><br>\n"
                    + "<p>* Only <b>available</b> time slots can be selected</p><br>"
                    + "        </div>");
            
            //out.println("<div>"
            //        + "<table>"
            //        + "<tr><th>Time</th><th>Name</th><th>Location</th>"
            //        + "</tr>");
            
            try {
                java.sql.Statement stmt = conn.createStatement();
                ResultSet schedule = stmt.executeQuery("SELECT * from Schedule");

                workshop_option = "";
                while (schedule.next()) {
                    sched_time = schedule.getString("schedule_time");
                    sched_title = schedule.getString("schedule_title");
                    sched_location = schedule.getString("schedule_location");
                //out.println("<tr><td>" + sched_time + "</td><td>" + sched_title + "</td><td>" + sched_location + "</td></tr>");
                    //out.println("time: " + sched_time + "<br>");
                }
            } catch (Exception e) {
                System.err.println(e);
            }
            //out.println("</table>"
            //        + "</div>");

            
            out.println("<div class=\"navigation\">"
                    + "            <form style=\"display: inline\" action=\"reg_admin.html\" method=\"get\"><button name=\"buttonRegAdmin\" title=\"Administrator Registration Page (Alt + 6)\">Administrator Registration</button></form>\n"
                    + "            <form style=\"display: inline\" action=\"reg_attendee.html\" method=\"get\"><button name=\"buttonRegAttendee\" title=\"Attendee Registration Page (Alt + 7)\">Attendee Registration</button></form>\n"
                    + "            <form style=\"display: inline\" action=\"index.html\" method=\"get\"><button name=\"buttonHome\" title=\"Return To Home Page (Alt + 8)\">Return To Home Page</button></form>\n"
                    + "        </div>\n"
                    + "        <div  id=\"bl\" class=\"bottomlinks\">"
                    + "            <a class=\"class1\" href=\"eventschedule\" title=\"Event Schedule (Alt + 0)\" accesskey=\"0\">0. Event Schedule</a><br>"
                    + "            <a href=\"in_speakers.html\" title=\"Add Speaker Details (Alt + 1)\" accesskey=\"1\">1. Add Speaker Details</a><br>"
                    + "            <a href=\"in_workshops.html\" title=\"Add Workshop Details (Alt + 2)\" accesskey=\"2\">2. Add Workshop Details</a><br>"
                    + "            <a href=\"in_schedule\" title=\"Add Schedule Details (Alt + 3)\" accesskey=\"3\">3. Add Schedule Details</a><br>"
                    + "            <a href=\"in_exhibitors.html\" title=\"Add Exhibitor Details (Alt + 4)\" accesskey=\"4\">4. Add Exhibitor Details</a><br>"
                    + "            <a href=\"eventAdministration.html\" title=\"Event Administration Page (Alt + 5)\" accesskey=\"5\">5. Event Administration</a><br>"
                    + "            <a href=\"reg_admin.html\" title=\"Administrator Registration Page (Alt + 6)\" accesskey=\"1\">6. Administrator Registration</a><br>"
                    + "            <a href=\"reg_attendee.html\" title=\"Attendee Registration Page (Alt + 7)\" accesskey=\"2\">7. Attendee Registration</a><br>"
                    + "            <a href=\"index.html\" title=\"Return To Homepage (Alt + 8)\" accesskey=\"6\">8. Return To Home Page</a>"
                    + "    </div>"
                    + "</body>"
                    + "</html>");
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
