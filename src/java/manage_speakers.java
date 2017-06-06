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
@WebServlet(urlPatterns = {"/manage_speakers"})
public class manage_speakers extends HttpServlet {
    Connection conn;
    PreparedStatement prepStat;
    Statement stat;
    
    String sp_id;
    String speak_name;
    String speak_bio;
    String speak_site;
    String speak_pic;
    int speak_num = 1;
       
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
        
        try (PrintWriter out = response.getWriter()) {
            out.println("<!DOCTYPE html>" +
                        "<html>" +
                        "<head>" +
"                           <link rel=\"stylesheet\" type=\"text/css\" href=\"CAstyle.css\">" +
                        "<title>Manage Speakers</title>" +
                        "</head>");
            
            out.println("<body>"
                        + "<div class=\"heading\">" +
"                           <br><h1 style=\"text-align:center\">Manage Speaker Details</h1><br>" +
                        "</div>"
// Navigation menu
                    + "<div class=\"navigation\">" +
                "            <form style=\"display: inline\" action=\"show_schedule\" method=\"get\"><button name=\"buttonEventSchedule\" title=\"Event Schedule (Alt + 0)\">Event Schedule</button></form>\n" +
                "            <form style=\"display: inline\" action=\"manage_speakers\" method=\"get\"><button name=\"buttonSpeaker\" title=\"Add Speaker Details (Alt + 1)\">Add Speaker Details</button></form>\n" +
                "            <form style=\"display: inline\" action=\"manage_workshops\" method=\"get\"><button name=\"buttonWorkshop\" title=\"Add Workshop Details (Alt + 2)\">Add Workshop Details</button></form>\n" +
                "            <form style=\"display: inline\" action=\"manage_schedule\" method=\"get\"><button name=\"buttonSchedule\" title=\"Add Schedule Details (Alt + 3)\">Add Schedule Details</button></form>\n" +
                "            <form style=\"display: inline\" action=\"manage_exhibitors\" method=\"get\"><button name=\"buttonExhibitor\" title=\"Add Exhibitor Details (Alt + 4)\">Add Exhibitor Details</button></form> \n" +
                "            <form style=\"display: inline\" action=\"eventAdministration.html\" method=\"get\"><button name=\"buttonEventAdmin\" title=\"Return To Event Administration (Alt + 5)\">Event Administration</button></form>\n" +
                "        </div>");
// Speaker input            
            out.println("<div class=\"mainbody\">" +
                    "<h2>Enter Speaker Details</h2>" +
                      "<form action=\"add_speaker\" method=\"POST\"><br>" +
        "                <table align=\"center\">" +
        "                    <tr>" +
        "                        <th>First Name:</th>" +
        "                        <td><input type=\"text\" name=\"speaker_fname\" autofocus=\"autofocus\" title=\"Enter speakers first name\"></td>" +
        "                    </tr>" +
        "                    <tr>" +
        "                        <th>Last Name:</th>" +
        "                        <td><input type=\"text\" name=\"speaker_lname\" title=\"Enter speakers last name\"></td>" +
        "                    </tr>" +
        "                    <tr>" +
        "                        <th>Biography:</th>" +
        "                        <td><textarea rows=\"15\" cols=\"50\" name=\"speaker_bio\" title=\"Enter speakers background info\"></textarea></td>" +
        "                    </tr>" +
        "                    <tr>" +
        "                        <th>Website:</th>" +
        "                        <td><input type=\"text\" name=\"speaker_website\" title=\"Enter speakers website\"></td>" +
        "                    </tr>" +
        "                    <tr>" +
        "                        <th>Picture URL:</th>" +
        "                        <td><input type=\"text\" name=\"speaker_pic\" title=\"Enter speaker picture url\"></td>" +
        "                    </tr>" +
        "                    <tr>" +
        "                        <td></td><td style=\"text-align:right\"><input type=\"submit\" value=\"Submit\" title=\"Submit Details\"/></td>" +
        "                    </tr>" +
        "              </table>" +
                     "</form>" +
                    "</div><br>");
            
            out.println("<div class=\"mainbody\">" +
                    "<h2>Current Speakers</h2>" +
                    "<p>A list of the keynote speakers currently scheduled:</p>" +
                        "<table align=\"center\">");                    
            try {
                java.sql.Statement stmt = conn.createStatement();
                ResultSet speakers = stmt.executeQuery("SELECT * FROM Speakers");
                
                speak_num=1;
                while (speakers.next()) {
                    sp_id = speakers.getString("speaker_id");
                    speak_name = speakers.getString("speaker_fname") + " " + speakers.getString("speaker_lname");
                    speak_bio = speakers.getString("speaker_bio");
                    speak_site = speakers.getString("speaker_website");
                    speak_pic = speakers.getString("speaker_pic");
                    
                // out.println("<tr><td>" + sched_time + "</td><td>" + ws_name + "</td><td>" + sched_location + "</td></tr>");
                
                out.println("<tr><td rowspan=\"6\"><img src="+speak_pic+" alt=\"Speaker Picture\" style=\"width:200px;height:200px;\"></td><td></td><td></td></tr>" +
                            "<tr><th colspan=\"2\" style=\"text-align: center\">Keynote Speaker "+speak_num+"</th></tr>" +
                            "<tr><th>DB ID:</th><td>" + sp_id + "</td></tr>" +
                            "<tr><th>Name:</th><td>" + speak_name + "</td></tr>" +
                            //"<tr><th valign=\"top\">Biography:</th><td valign=\"top\">" + speak_bio + "</td></tr>" +
                            "<tr valign=\"top\"><th>Biography:</th><td>" + speak_bio + "</td></tr>" +
                            "<tr><th>Website:</th><td><a href=\"" + speak_site + "\">\"" + speak_site + "\"</a></td></tr>" +
                            "<tr><td colspan=\"3\"><hr></td></tr>");
                speak_num++;
                }
            }
            catch (Exception e)
            {
                System.err.println(e);
            }            
            out.println("</table></div><br>");
            
// Edit Speakers           
            out.println("<div class=\"mainbody\">"
                    +"<h2>Edit Speakers Details</h2>"
                    + "<p>Select exhibitor to delete</p>"
                    + "<form action=\"delete_speaker\" method=\"POST\">"
                    + "<table align=\"center\">"
                        + "<tr>" +
                "               <th>Speaker To Delete:</th>\n" +
                "                   <td><select name=\"delete_speaker\" title=\"Select A Name From The List\" style=\"width:100%\">");
            try{
                java.sql.Statement stmt = conn.createStatement();            
                ResultSet Speaker = stmt.executeQuery("SELECT speaker_id,speaker_fname,speaker_lname FROM Speakers");
                
                while(Speaker.next())
                {
                    speak_name = Speaker.getString("speaker_fname") + " " + Speaker.getString("speaker_lname");
                    sp_id = Speaker.getString("speaker_id"); 
                    out.println("<option value=\""+sp_id+"\">" + sp_id + ". " + speak_name + "</option>");
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
