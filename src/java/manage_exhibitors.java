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
@WebServlet(urlPatterns = {"/manage_exhibitors"})
public class manage_exhibitors extends HttpServlet {
    String[] arrExNames = new String[20]; // array of exhibitor names, for list enable/disable
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
    
    String ex_id;
    String ex_fname;
    String ex_lname;
    String ex_bio;
    String ex_web;
    String ex_pic;
    int ex_count;
    String ex_name_list;
    String ex_pic_name;
    
    
    
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
        try{
                        java.sql.Statement stmt = conn.createStatement(); 
                        ResultSet exhibitor = stmt.executeQuery("SELECT * FROM Exhibitors");  
                        
                        for(int i = 0; i <20; i++)  // reset status
                        {
                            status[i]="";
                        }
                        while(exhibitor.next())
                        {
                            ex_fname = exhibitor.getString("exhibitor_fname");  
                            ex_lname = exhibitor.getString("exhibitor_lname");
                            
                            for (int i = 0; i < 20; i++)
                            {
                                arrExNames[i] = ex_fname + " " + ex_lname;  // add names to array
                            }
                            
                            //for(int i = 0; i <20; i++)
                            //{
                            //    if (ex_fname.contentEquals(arrExNames[i])) status[i] = "disabled";
                            //}
                        }   
                    }
                    catch(Exception e)
                    {
                        System.err.println(e);
                    } 
                    
        try (PrintWriter out = response.getWriter()) {
            out.println("<!DOCTYPE html>" +
                        "<html>" +
                        "<head>" +
"                           <link rel=\"stylesheet\" type=\"text/css\" href=\"CAstyle.css\">" + 
                            "<title>Manage Exhibitors</title>" +
                        "</head>" +                    
                        "<body>" +
                        "<div class=\"heading\">\n" +
"                           <br><h1 style=\"text-align:center\">Manage Exhibitor Details</h1><br>\n" +
                        "</div>"
// Navigation menu
                    + "<div class=\"navigation\">" +
                            "<form style=\"display: inline\" action=\"show_schedule\" method=\"get\"><button name=\"buttonEventSchedule\" title=\"Event Schedule (Alt + 0)\">Event Schedule</button></form>\n" +
                            "<form style=\"display: inline\" action=\"manage_speakers\" method=\"get\"><button name=\"buttonSpeaker\" title=\"Add Speaker Details (Alt + 1)\">Manage Speakers</button></form>\n" +
                            "<form style=\"display: inline\" action=\"manage_workshops\" method=\"get\"><button name=\"buttonWorkshop\" title=\"Add Workshop Details (Alt + 2)\">Manage Workshops</button></form>\n" +
                            "<form style=\"display: inline\" action=\"manage_schedule\" method=\"get\"><button name=\"buttonSchedule\" title=\"Add Schedule Details (Alt + 3)\">Manage Schedules</button></form>\n" +
                            "<form style=\"display: inline\" action=\"manage_exhibitors\" method=\"get\"><button style=\"color: blue; background-color: white;\" name=\"buttonExhibitor\" title=\"Add Exhibitor Details (Alt + 4)\">Manage Exhibitors</button></form> \n" +
                            "<form style=\"display: inline\" action=\"eventAdministration.html\" method=\"get\"><button name=\"buttonEventAdmin\" title=\"Return To Event Administration (Alt + 5)\">Event Administration</button></form>\n" +
                        "</div>");
                    
// Add to Exhibitors
                out.println("<div class=\"mainbody\">"
                    + "<h2>Add An Exhibitor</h2>");                    
// Table
                out.println("<form action=\"add_exhibitor\" method=\"POST\"><br>" +
                                "<table  style=\"text-align:left\" align=\"center\">");
                
// Exhibitor Details
                out.println("<tr>\n" +
            "                    <th>First Name:</th>" +
            "                    <td><input type=\"text\" name=\"exhibitor_fname\" autofocus=\"autofocus\" title=\"Enter exhibitors first name\"></td>" +
            "                </tr>\n" +
            "                <tr>\n" +
            "                    <th>Last Name:</th>" +
            "                    <td><input type=\"text\" name=\"exhibitor_lname\" title=\"Enter exhibitors last name\"></td>" +
            "                </tr>\n" +
            "                <tr>\n" +
            "                    <th>Biography:</th>" +
            "                    <td><textarea rows=\"15\" cols=\"50\" name=\"exhibitor_bio\" title=\"Enter background information for exhibitor\"></textarea></td>" +
            "                </tr>\n" +
            "                <tr>\n" +
            "                    <th>Website:</th>" +
            "                    <td><input type=\"text\" name=\"exhibitor_website\" title=\"Enter exhibitors website\"></td>" +
            "                </tr>" +
        "                    <tr>" +
        "                        <th>Picture URL:</th>" +
        "                        <td><input type=\"text\" name=\"exhibitor_pic\" title=\"Enter exhibitor picture url\"></td>" +
        "                    </tr>" +
            "                <tr>\n" +
            "                    <td></td>\n" +
            "                <td style=\"text-align:right\"><input type=\"submit\" value=\"Submit\" title=\"Submit Details\"/></td>" +
            "                </tr>");
                    
                out.println("</table>\n" +
                "            </form><br>"
                        + "</div><br>");
                
/** Output the Exhibitors Table */                 
            out.println("<div class=\"mainbody\">" + 
                            "<h2>Current Exhibitors</h2>" +
                            "<p>A list of the Exhibitors currently booked:</p>"
                            + "<table align=\"center\">");
                                try {
                                    java.sql.Statement stmt = conn.createStatement();
                                    ResultSet exhibitors = stmt.executeQuery("SELECT * FROM exhibitors ORDER BY exhibitor_lname;");
                                    ex_count = 1;
                                    while (exhibitors.next()) {
                                        ex_id = exhibitors.getString("exhibitor_id"); 
                                        ex_fname = exhibitors.getString("exhibitor_fname");
                                        ex_lname = exhibitors.getString("exhibitor_lname");
                                        ex_bio = exhibitors.getString("exhibitor_bio");
                                        ex_web = exhibitors.getString("exhibitor_website");
                                        ex_pic = exhibitors.getString("exhibitor_pic");
                                        ex_pic_name = ex_fname + " " + ex_lname;
                                    
                                    out.println("<tr><td rowspan=\"5\"><img src="+ex_pic+" alt=\"Picture of "+ex_pic_name+"\" style=\"width:200px;height:200px;\"></td><th style=\"text-align:center\"colspan=\"2\">Exhibitor " + ex_count + "</th></tr>" +                                             
                                                "<tr><th>Database ID:</th><td>" + ex_id + "</td></tr>" +
                                                "<tr><th>Name:</th><td>" + ex_fname + " " + ex_lname + "</td></tr>"
                                            +   "<tr><th>Website</th><td><a href=\"" + ex_web + "\">\"" + ex_web + "\"</a></td></tr>" + 
                                                "<tr><th>About:</th><td>" + ex_bio + "</td></tr>" +
                                                "<tr><td colspan=\"3\"><hr></td></tr>");
                                    ex_count++;
                                    }
                                } catch (Exception e) {
                                    System.err.println(e);
                                }
            out.println("</table></div><br>");
// Edit Exhibitors            
            out.println("<div class=\"mainbody\">"
                    +"<h2>Edit Exhibitor Details</h2>"
                    + "<p>Select exhibitor to delete</p>"
                    + "<form action=\"delete_exhibitor\" method=\"POST\">"
                    + "<table align=\"center\">"
                        + "<tr>" +
                "               <th>Exhibitor To Delete:</th>\n" +
                "                   <td><select name=\"delete_ex\" title=\"Select A Name From The List\" style=\"width:100%\">");
            try{
                java.sql.Statement stmt = conn.createStatement();            
                ResultSet exhibitor = stmt.executeQuery("SELECT exhibitor_id,exhibitor_fname,exhibitor_lname from Exhibitors");
                
                while(exhibitor.next())
                {
                    ex_name_list = exhibitor.getString("exhibitor_fname") + " " + exhibitor.getString("exhibitor_lname");
                    ex_id = exhibitor.getString("exhibitor_id"); 
                    out.println("<option value=\""+ex_id+"\">" + ex_id + ". " + ex_name_list + "</option>");
                }
            }
            catch(Exception e)
            {
                System.err.println(e);
            } 
            
            out.println("</select></td>" +
                                "<td style=\"text-align:right\"><input type=\"submit\" value=\"Delete\" title=\"Delete Time\"/></td>" +
                "           </tr>"
                    + "</table>" +
                        "</form>" +
                "</div>");

// Navigation menu
            out.println("<div class=\"navigation\">" +
                "            <form style=\"display: inline\" action=\"reg_admin.html\" method=\"get\"><button name=\"buttonRegAdmin\" title=\"Administrator Registration Page (Alt + 6)\">Administrator Registration</button></form>\n" +
                "            <form style=\"display: inline\" action=\"reg_attendee.html\" method=\"get\"><button name=\"buttonRegAttendee\" title=\"Attendee Registration Page (Alt + 7)\">Attendee Registration</button></form>\n" +
                "            <form style=\"display: inline\" action=\"index\" method=\"get\"><button name=\"buttonHome\" title=\"Return To Home Page (Alt + 8)\">Return To Home Page</button></form>\n" +
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
                "            <a href=\"index\" title=\"Return To Homepage (Alt + 8)\" accesskey=\"6\">8. Return To Home Page</a>" +
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
