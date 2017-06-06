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
    String title ="Manage Exhibitors";
    // Exhibitor details
    String ex_name;
    String ex_id;
    String ex_bio;
    String ex_web;
    String ex_pic;
    
    int ex_count; // Current Exhibitor number
    int ex_num = 0; // Total number of exhibitors
    
    // Connection    
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
                            "<tr><td>&nbsp;</td></tr>" +
                            "<tr><td>&nbsp;</td></tr>" +
                            "<tr><td><a align=\"left\" href=\"index\" title=\"Return To Homepage (Alt + 7)\" accesskey=\"7\"><img src=\"http://s21.postimg.org/gyukaf1l3/Logo.png\" alt=\"Event Logo\" style=\"width:150px;height:150px;\"></a></td>" +
                            "<td><h1 style=\"text-align:center\">" + title + "</h1></td></tr>" +
                        "</table>" +
                    "</div>");
// Navigation menu (Exhibitors Highlighted)
            out.println("<div class=\"navigation\">" +
                            "<form style=\"display: inline\" action=\"show_schedule\" method=\"get\"><button name=\"buttonEventSchedule\" title=\"Event Schedule (Alt + 3)\">Event Schedule</button></form>" +
                            "<form style=\"display: inline\" action=\"manage_speakers\" method=\"get\"><button name=\"buttonSpeaker\" title=\"Add Speaker Details (Alt + h)\">Manage Speakers</button></form>" +
                            "<form style=\"display: inline\" action=\"manage_workshops\" method=\"get\"><button name=\"buttonWorkshop\" title=\"Add Workshop Details (Alt + j)\">Manage Workshops</button></form>" +
                            "<form style=\"display: inline\" action=\"manage_schedule\" method=\"get\"><button name=\"buttonSchedule\" title=\"Add Schedule Details (Alt + k)\">Manage Schedule</button></form>" +
                            "<form style=\"display: inline\" action=\"manage_exhibitors\" method=\"get\"><button name=\"buttonExhibitor\" style=\"color: blue; background-color: white;\" title=\"Add Exhibitor Details (Alt + l)\">Manage Exhibitors</button></form>" +
                            "<form style=\"display: inline\" action=\"eventAdministration.html\" method=\"get\"><button name=\"buttonEventAdmin\" title=\"Return To Event Administration (Alt + 8)\">Event Administration</button></form>" +
                            "<form style=\"display: inline\" action=\"index\" method=\"get\"><button name=\"buttonHome\" title=\"Return To Homepage (Alt + 7)\">Home</button></form>" +
                        "</div>");
// Count exhibitors
            try {
                java.sql.Statement stmt = conn.createStatement();
                ResultSet exhibitors = stmt.executeQuery("SELECT COUNT(*) As ex_count FROM exhibitors;");
                exhibitors.next();    
                ex_num = exhibitors.getInt("ex_count");
            } catch (Exception e) {
                System.err.println(e);
            }
            
// Add to Exhibitors
                out.println("<div class=\"mainbody\">" +
                            "<h2 class=\"tbhead\">Add An Exhibitor</h2>");
                            //out.println("Test: "+ex_num+"");
                            if(ex_num == 1) out.println("<p>There is "+ex_num+" Exhibitor currently registered");
                            else if(ex_num == 0) out.println("<p>There is no Exhibitor registered");
                            else out.println("<p>There are "+ex_num+" Exhibitors registered");
                            
                out.println("<form action=\"add_exhibitor\" method=\"POST\"><br>" +
                                    "<table  style=\"text-align:left\" align=\"center\">" +
                                        "<tr>" +
                                            "<th>First Name:</th>" +
                                            "<td><input type=\"text\" name=\"exhibitor_fname\" autofocus=\"autofocus\" title=\"Enter exhibitors first name\" maxlength=\"40\" placeholder=\"Required Field\" required></td>" +
                                        "</tr>" +
                                        "<tr>" +
                                            "<th>Last Name:</th>" +
                                            "<td><input type=\"text\" name=\"exhibitor_lname\" title=\"Enter exhibitors last name\" maxlength=\"40\" placeholder=\"Required Field\" required></td>" +
                                        "</tr>" +
                                        "<tr>" +
                                            "<th>Biography:</th>" +
                                            "<td><textarea rows=\"15\" cols=\"50\" name=\"exhibitor_bio\" title=\"Enter background information for exhibitor\" placeholder=\"Required Field\" required></textarea></td>" +
                                        "</tr>" +
                                        "<tr>" +
                                            "<th>Website:</th>" +
                                            "<td><input type=\"text\" name=\"exhibitor_website\" title=\"Enter exhibitors website\" maxlength=\"60\"></td>" +
                                        "</tr>" +
                                        "<tr>" +
                                            "<th>Picture URL:</th>" +
                                            "<td><input type=\"text\" name=\"exhibitor_pic\" title=\"Enter exhibitor picture url\" maxlength=\"60\"></td>" +
                                        "</tr>" +
                                        "<tr>" +
                                            "<td></td>" +
                                            "<td style=\"text-align:right\"><input type=\"submit\" value=\"Submit\" title=\"Submit Details\"/></td>" +
                                        "</tr>" +
                                    "</table>" +
                                "</form><br>" +
                            "</div><br>");
                
/** Output the Exhibitors Table */                 
            out.println("<div class=\"mainbody\">" + 
                            "<h2 class=\"tbhead\">Current Exhibitors</h2>" +
                            "<p>A list of the Exhibitors currently booked:</p>"
                            + "<table align=\"center\">");
                                try {
                                    java.sql.Statement stmt = conn.createStatement();
                                    ResultSet exhibitors = stmt.executeQuery("SELECT * FROM exhibitors ORDER BY exhibitor_lname;");
                                    ex_count = 1;
                                    while (exhibitors.next()) {
                                        ex_id = exhibitors.getString("exhibitor_id"); 
                                        ex_bio = exhibitors.getString("exhibitor_bio");
                                        ex_web = exhibitors.getString("exhibitor_website");
                                        ex_pic = exhibitors.getString("exhibitor_pic");
                                        ex_name = exhibitors.getString("exhibitor_fname") + " " + exhibitors.getString("exhibitor_lname");
                                    
                                    out.println("<tr><td rowspan=\"5\"><img src="+ex_pic+" alt=\"Picture of "+ex_name+"\" style=\"width:200px;height:200px;\"></td><th style=\"text-align:center\"colspan=\"2\">Exhibitor " + ex_count + "</th></tr>" +                                             
                                                "<tr><th>Database ID:</th><td>" + ex_id + "</td></tr>" +
                                                "<tr><th>Name:</th><td>" + ex_name + "</td></tr>"
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
                        + "<h2 class=\"tbhead\">Edit Exhibitor Details</h2>"
                        + "<p>Select exhibitor to delete</p>"
                        + "<form action=\"delete_exhibitor\" method=\"POST\">"
                        + "<table align=\"center\">"
                            + "<tr>" +
                                "<th>Exhibitor To Delete:</th>\n" +
                                "<td><select name=\"delete_ex\" title=\"Select A Name From The List\" style=\"width:100%\">");
                                try{
                                    java.sql.Statement stmt = conn.createStatement();            
                                    ResultSet exhibitor = stmt.executeQuery("SELECT exhibitor_id,exhibitor_fname,exhibitor_lname from Exhibitors");

                                    while(exhibitor.next())
                                    {
                                        ex_name = exhibitor.getString("exhibitor_fname") + " " + exhibitor.getString("exhibitor_lname");
                                        ex_id = exhibitor.getString("exhibitor_id"); 
                                        out.println("<option value=\""+ex_id+"\">" + ex_id + ". " + ex_name + "</option>");
                                    }
                                }
                                catch(Exception e)
                                {
                                    System.err.println(e);
                                } 
            out.println("</select></td>" +
                                "<td style=\"text-align:right\"><input type=\"submit\" value=\"Delete\" title=\"Delete Time\"/></td>" +
                            "</tr>"
                        + "</table>" +
                    "</form>" +
                "</div>");
     
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
                                    + "<td><a href=\"eventAdministration.html\" title=\"Event Administration Page (Alt + 8)\" accesskey=\"8\">8. Event Administration</a></td></tr>" +
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
