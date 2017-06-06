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
@WebServlet(urlPatterns = {"/manage_workshops"})
public class manage_workshops extends HttpServlet {
    Connection conn;
    PreparedStatement prepStat;
    Statement stat;
    
    String ws_id;
    String ws_name;
    String ws_pres1;
    String ws_pres2;;
    String ws_info;
    int ws_num = 1;
       
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
                        "<title>Manage Workshops</title>" +
                    "</head>");
            
            out.println("<body>"
                        + "<div class=\"heading\">" +
"                           <br><h1 style=\"text-align:center\">Manage Workshop Details</h1><br>\n" +
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
// Workshop input            
            out.println("<div class=\"mainbody\">" +
                    "<h2>Enter Workshop Details</h2>" +
"            <form action=\"add_workshop\" method=\"POST\"><br>" +
"                <table align=\"center\">" +
"                    <tr>" +
"                        <th>Name:</th>" +
"                        <td><input type=\"text\" name=\"ws_name\" autofocus=\"autofocus\" title=\"Enter a name for the workshop\"></td>" +
"                    </tr>\n" +
"                    <tr>\n" +
"                        <th>Presenter 1:</th>\n" +
"                        <td><input type=\"text\" name=\"ws_presenter1\" title=\"Enter first presenters name\"></td>" +
"                    </tr>\n" +
"                    <tr>\n" +
"                        <th>Presenter 2:</th>\n" +
"                        <td><input type=\"text\" name=\"ws_presenter2\" title=\"Enter second presenters name\"></td>" +
"                    </tr>\n" +
"                    <tr>\n" +
"                        <th>Information:</th>\n" +
"                        <td><textarea rows=\"15\" cols=\"50\" name=\"ws_info\" title=\"Enter information about the workshop\"></textarea></td>" +
"                    </tr>\n" +
"                    <tr>\n" +
"                        <td></td>\n" +
"                    <td style=\"text-align:right\"><input type=\"submit\" value=\"Submit\" title=\"Submit Details\"/></td>\n" +
"                    </tr>\n" +
"                </table>\n" +
"            </form><br>\n" +
"        </div><br>");

// Current Workshops            
            out.println("<div class=\"mainbody\">" +
                    "<h2>Current Workshops</h2>" +
                    "<p>A list of the workshops currently scheduled:</p>" +
                        "<table align=\"center\">");                    
            try {
                java.sql.Statement stmt = conn.createStatement();
                ResultSet speakers = stmt.executeQuery("SELECT * FROM Workshops WHERE ws_name NOT LIKE 'Break'");
                
                ws_num=1;                                                                                                               // workshop number
                while (speakers.next()) {
                    ws_id = speakers.getString("ws_id");
                    ws_name = speakers.getString("ws_name");
                    ws_pres1 = speakers.getString("ws_presenter1");
                    ws_pres2 = speakers.getString("ws_presenter2");
                    ws_info = speakers.getString("ws_info");
                    
                out.println("<tr><th colspan=\"4\" style=\"text-align: center\">Workshop "+ws_num+"</th></tr>" +                 // heading
                            "<tr><th>Workshop Name:</th><td>"+ws_name+"</td><th>Workshop DB ID:</th><td>"+ws_id+"</td></tr>" +          // name & id                
                            "<tr><th>Presenter 1:</th><td>"+ws_pres1+"</td><th>Presenter 2:</th><td>"+ws_pres2+"</td></tr>" +           // presenter names
                            "<tr><th>About:</th><td colspan=\"3\">"+ws_info+"</td>" +
                            "<tr><td colspan=\"4\"><hr></td></tr>");                                                                    // line
                ws_num++;
                }
            }
            catch (Exception e)
            {
                System.err.println(e);
            }            
            out.println("</table></div><br>");
            
// Edit Workshops ***********            
            out.println("<div class=\"mainbody\">"
                    +"<h2>Edit Workshop Details</h2>"
                    + "<p>Select workshop to delete</p>"
                    + "<form action=\"delete_workshop\" method=\"POST\">"
                    + "<table align=\"center\">"
                        + "<tr>" +
                "               <th>Workshop To Delete:</th>\n" +
                "                   <td><select name=\"delete_workshop\" title=\"Select A Name From The List\" style=\"width:100%\">");
            try{
                java.sql.Statement stmt = conn.createStatement();            
                ResultSet Workshop = stmt.executeQuery("SELECT ws_id,ws_name FROM Workshops WHERE ws_name NOT LIKE 'Break'");
                
                while(Workshop.next())
                {
                    ws_id = Workshop.getString("ws_id"); 
                    ws_name = Workshop.getString("ws_name");
                    out.println("<option value=\""+ws_id+"\">" + ws_id + ". " + ws_name + "</option>");
                }
            }
            catch(Exception e)
            {
                System.err.println(e);
            } 
            
            out.println("</select></td>" +
                                "<td style=\"text-align:right\"><input type=\"submit\" value=\"Delete\" title=\"Delete Workshop\"/></td>" +
                            "</tr>"
                    + "</table>" +
                        "</form>" +
                    "<p>Choose the workshop ID & name to select</p>" +
                "</div><br>");
 
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
