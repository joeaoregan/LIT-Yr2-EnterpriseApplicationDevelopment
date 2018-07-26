package joe.ead.manage;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import com.mysql.jdbc.Connection;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.DriverManager;
import java.sql.ResultSet;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import joe.ead.abstracted.Connect; // 24/07/2018
import joe.ead.abstracted.Menu;  // 26/07/2018
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
    String ex_fname;
    String ex_lname;
    String ex_id;
    String ex_bio;
    String ex_web;
    String ex_pic;
    
    int ex_count; // Current Exhibitor number
    int ex_num = 0; // Total number of exhibitors
    
    // Connection    
    Connection conn;
       
    public void init() throws ServletException {
        try{
            Class.forName("com.mysql.jdbc.Driver");
            conn = (Connection) DriverManager.getConnection (Connect.url+Connect.dbName,Connect.userName,Connect.password);
            //stat = (Statement) conn.createStatement();
        } catch(Exception e){
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
        
        Menu menu = new Menu();
        
        response.setContentType("text/html;charset=UTF-8");
        try (PrintWriter out = response.getWriter()) {
            out.println("<!DOCTYPE html><html><head>" +
                            "<link rel=\"stylesheet\" type=\"text/css\" href=\"CAstyle.css\">" + 
                            "<title>"+title+"</title>" +
                        "</head>" +                    
                        "<body>");
            
            menu.heading(request,out,title); // Heading
            menu.navigationMenuManage(out, menu.SHOW_EXHIBITORS); // Navigation menu (Exhibitors Highlighted)
            /*
            out.println("<div class=\"heading\"><table>" +
                            "<tr><td><div class=\"logo\"><a align=\"left\" href=\"index\" title=\"Return To Homepage (Alt + 7)\" accesskey=\"7\">" +
                                "<img src='" + request.getContextPath() + "/images/logoT.png' alt=\"Event Logo\" id=\"img150\"></a></div></td>" +
                                "<td><h1>" + title + "</h1></td></tr>" +
                        "</table></div>");
            
            out.println("<div class=\"navigation\"><span>" +
                            "<form action=\"show_schedule\" method=\"get\"><button name=\"buttonEventSchedule\" title=\"Event Schedule (Alt + 3)\">Event Schedule</button></form>" +
                            "<form action=\"manage_speakers\" method=\"get\"><button name=\"buttonSpeaker\" title=\"Add Speaker Details (Alt + h)\">Manage Speakers</button></form>" +
                            "<form action=\"manage_workshops\" method=\"get\"><button name=\"buttonWorkshop\" title=\"Add Workshop Details (Alt + j)\">Manage Workshops</button></form>" +
                            "<form action=\"manage_schedule\" method=\"get\"><button name=\"buttonSchedule\" title=\"Add Schedule Details (Alt + k)\">Manage Schedule</button></form>" +
                            "<form action=\"manage_exhibitors\" method=\"get\"><button name=\"buttonExhibitor\" id=\"active\" title=\"Add Exhibitor Details (Alt + l)\">Manage Exhibitors</button></form>" +
                            "<form action=\"index\" method=\"get\"><button name=\"buttonHome\" title=\"Return To Homepage (Alt + 7)\">Home</button></form>" +
                        "</span></div>");
            */
// Count exhibitors
            try {
                java.sql.Statement stmt = conn.createStatement();
                ex_num = 0;
                ResultSet exhibitors = stmt.executeQuery("SELECT COUNT(*) As ex_count FROM exhibitors;");
                exhibitors.next();    
                ex_num = exhibitors.getInt("ex_count");
            } catch (Exception e) {
                System.err.println(e);
            }
            
// Add to Exhibitors
            out.println("<div class=\"mainbody\"><h2 class=\"tbhead\">Add An Exhibitor</h2>");
            //out.println("Test: "+ex_num+"");
            
            switch (ex_num) {
                case 1:
                    out.println("<p>There is "+ex_num+" Exhibitor currently registered");
                    break;
                case 0:
                    out.println("<p>There is no Exhibitor registered");
                    break;
                default:
                    out.println("<p>There are "+ex_num+" Exhibitors registered");
                    break;
            }
            
            out.println("<form action=\"add_exhibitor\" method=\"POST\"><br>" +
                        "<table align=\"center\">" +
                            "<tr><th>First Name:</th>" +
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
                            "<tr><td></td><td>e.g \"/images/example.png\"</td></tr>" +
                            "<tr>" +
                                "<td></td>" +
                                "<td id=\"bt\"><input type=\"submit\" value=\"Submit\" title=\"Submit Details\"/></td>" +
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

                    out.println("<tr><td rowspan=\"5\"><img src='"+ request.getContextPath() + ex_pic+"' alt=\"Picture of "+ex_name+"\" id=\"img150\"></td><th id=\"thc\" colspan=\"2\">Exhibitor " + ex_count + "</th></tr>" +                                             
                            "<tr><th>Database ID:</th><td>" + ex_id + "</td></tr>" +
                            "<tr><th>Name:</th><td>" + ex_name + "</td></tr>"
                        +   "<tr><th>Website</th><td><a href=\"" + ex_web + "\">" + ex_web + "</a></td></tr>" + 
                            "<tr><th>About:</th><td>" + ex_bio + "</td></tr>" +
                            "<tr><td colspan=\"3\"><hr></td></tr>");
                    
                    ex_count++;
                }
            } catch (Exception e) {
                System.err.println(e);
            }
                                
            out.println("</table></div><br>");
            
// Delete Exhibitors            
            out.println("<div class=\"mainbody\">"
                        + "<h2 class=\"tbhead\">Edit & Delete Exhibitor Details</h2>"
                        + "<p>Select exhibitor to delete</p>"
                        + "<form action=\"delete_exhibitor\" method=\"POST\">"
                        + "<table align=\"center\">"
                            + "<tr>" +
                                "<th>Exhibitor To Delete:</th>" +
                                "<td><select name=\"delete_ex\" title=\"Select A Name From The List\">");
            try {
                java.sql.Statement stmt = conn.createStatement();            
                ResultSet exhibitor = stmt.executeQuery("SELECT exhibitor_id,exhibitor_fname,exhibitor_lname from Exhibitors");

                while(exhibitor.next()) {
                    ex_name = exhibitor.getString("exhibitor_fname") + " " + exhibitor.getString("exhibitor_lname");
                    ex_id = exhibitor.getString("exhibitor_id"); 
                    out.println("<option value=\""+ex_id+"\">" + ex_id + ". " + ex_name + "</option>");
                }
            } catch(Exception e) { 
                System.err.println(e); 
            }
            
            out.println("</select></td>" +
                                "<td id=\"bt\"><input type=\"submit\" value=\"Delete\" title=\"Delete Exhibitor\"/></td>" +
                            "</tr>"
                        + "</table>" +
                    "</form>");
            
// Edit Exhibitors            
            out.println("<p>Select exhibitor to edit</p>"
                        + "<form action=\"edit_ex_name\" method=\"POST\">"
                        + "<table align=\"center\">"
                            + "<tr><th>Exhibitor To Edit:</th>" +
                                "<td><select name=\"edit_name\" title=\"Select A Name From The List\">");
            
            try{
                java.sql.Statement stmt = conn.createStatement();            
                ResultSet exhibitor = stmt.executeQuery("SELECT exhibitor_id,exhibitor_fname,exhibitor_lname from Exhibitors");

                while(exhibitor.next()) {
                    ex_fname = exhibitor.getString("exhibitor_fname");
                    ex_lname = exhibitor.getString("exhibitor_lname");
                    ex_id = exhibitor.getString("exhibitor_id"); 
                    out.println("<option value=\""+ex_id+"\">"+ ex_id +". "+ ex_fname +" "+ ex_lname +"</option>");
                }
            } catch(Exception e){ 
                System.err.println(e); 
            } 
                                
            out.println("</select></td><td id=\"bt\"><input type=\"submit\" value=\"Edit\" title=\"Edit Exhibitor\"/></td></tr>");                    
            out.println("<tr><td><b>New First Name:</b></td><td><input type=\"text\" name=\"new_ex_fname\" title=\"Enter NEW Exhibitor First Name\" maxlength=\"40\" required></td><td></td></tr>");
            out.println("</table></form></div>");
            
            menu.bottomMenuManage(out); // Bottom Links (Manage)  
/*
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
                                    + "<td></td></tr>" +
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
*/
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
