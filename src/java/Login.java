/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.io.IOException;
import java.io.PrintWriter;
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
@WebServlet(urlPatterns = {"/login"})
public class login extends HttpServlet {
        String URL = "JDBC:MYSQL://LOCALHOST:3306/";
        String DB = "JoeCA";
        String USERNAME = "root";
        String PASSWORD = "password";
        Connection conn = null;        
        String DB_username; 
        String DB_password;
        String admin_name;
        String username;
        String password;
        String title = "Event Administration";
        String docType = "<!doctype html >";
        boolean  passwordValidate;
   
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        
        PrintWriter out = response.getWriter(); 
        
        username = request.getParameter("username");    // username from html form
        password = request.getParameter("password");    // password form html form
                        
        try{
            Class.forName("com.mysql.jdbc.Driver");
            conn = DriverManager .getConnection(URL + DB,USERNAME,PASSWORD);    // Establish/request a connection to a database
            Statement stmt = conn.createStatement(); 
            ResultSet result = stmt.executeQuery("SELECT * FROM administrators WHERE admin_username = '"+username+"'");  // works OK
            result.next();
            DB_username = result.getString("admin_username");
            admin_name = result.getString("admin_fname") + " " + result.getString("admin_lname");
            DB_password = result.getString("admin_password"); 
        }
        catch(Exception e)
        {
            System.err.println(e);
        }           
        //passwordValidate = password.contentEquals( DB_password ); // see does password match database password
        
        out.println(docType + 
                "<html>" +                
                  "<head>" +
                    "<link rel=\"stylesheet\" type=\"text/css\" href=\"CAstyle.css\">" + 
                    "<title>" + title + "</title>" +
                  "</head>" +    
                  "<body>");
        

        //if (passwordValidate == true)
        //if (password.contentEquals( DB_password ))
        if(password.equals(DB_password))
        {        
// Heading
            out.println("<div class=\"heading\">" +
                            "<table>" +
                                "<tr><td><a align=\"left\" href=\"index\" title=\"Return To Homepage (Alt + 7)\" accesskey=\"7\"><img src=\"http://s21.postimg.org/gyukaf1l3/Logo.png\" alt=\"Event Logo\" id=\"img150\"></a></td>" +
                                "<td><h1>" + title + "</h1></td></tr>" +
                            "</table>" +
                        "</div>");            
// Navigation menu (Event Administration Highlighted)
            out.println("<div class=\"navigation\"><span>" +
                            "<form action=\"show_schedule\" method=\"get\"><button name=\"buttonEventSchedule\" title=\"Event Schedule (Alt + 3)\">Event Schedule</button></form>" +
                            "<form action=\"manage_speakers\" method=\"get\"><button name=\"buttonSpeaker\" title=\"Add Speaker Details (Alt + h)\">Manage Speakers</button></form>" +
                            "<form action=\"manage_workshops\" method=\"get\"><button name=\"buttonWorkshop\" title=\"Add Workshop Details (Alt + j)\">Manage Workshops</button></form>" +
                            "<form action=\"manage_schedule\" method=\"get\"><button name=\"buttonSchedule\" title=\"Add Schedule Details (Alt + k)\">Manage Schedule</button></form>" +
                            "<form action=\"manage_exhibitors\" method=\"get\"><button name=\"buttonExhibitor\" title=\"Add Exhibitor Details (Alt + l)\">Manage Exhibitors</button></form>" +
                            "<button id=\"active\" name=\"buttonEventAdmin\" title=\"Return To Event Administration (Alt + 8)\">Event Administration</button>" +
                            "<form action=\"index\" method=\"get\"><button name=\"buttonHome\" title=\"Return To Homepage (Alt + 7)\">Home</button></form>" +
                        "</span></div>");
// Successful Login Greeting
            out.println("<div class=\"mainbody\"><br>" +
                            "<ul><h2>Hello " + request.getParameter("username") + ", welcome back!!!</h2></ul>" +  // or FIRST NAME LAST NAME
                        "</div>");            
// Event Administration
            out.println("<div class=\"mainbody\">" +
                            "<h2>Manage Speakers, Workshops, Schedule, Exhibitors</h2>" +
                            "<p>ICT in Eductation event" +
                            "<p>This is the event administration page" +
                            "<p>The following options are available:</p><span>" +
                                "<form action=\"manage_speakers\" method=\"get\"><button name=\"buttonSpeaker\" autofocus=\"autofocus\" title=\"Alt + h - Manage Speaker Details\">Manage Speaker Details</button></form>" +
                                "<form action=\"manage_workshops\" method=\"get\"><button name=\"buttonWorkshop\" title=\"Alt + j - Manage Workshop Details\">Manage Workshop Details</button></form>" +
                                "<form action=\"manage_schedule\" method=\"get\"><button name=\"buttonSchedule\" title=\"Alt + k - Manage Schedule Details\">Manage Schedule Details</button></form>" +
                                "<form action=\"manage_exhibitors\" method=\"get\"><button name=\"buttonExhibitor\" title=\"Alt + l4 - Manage Exhibitor Details\">Manage Exhibitor Details</button></form>" +
                        "</span></div><br>");            
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
        }
        else
        {
// Admin Login
            out.println("<div class=\"login\">" +
                            "<form action=\"login\" method=\"Get\">" +
                                "<table>" +
                                    "<tr>" +
                                        "<td width=100% rowspan=\"2\"><a align=\"left\" href=\"index\" title=\"Return To Homepage (Alt + 7)\" accesskey=\"7\">" +
                                            "<img src=\"http://s21.postimg.org/gyukaf1l3/Logo.png\" alt=\"Event Logo\" id=\"img50\"></a></td>" +
                                        "<th id=\"thc\">Administrator</th>" +
                                        "<td>Username:</td>" +
                                        "<td><input type=\"text\" name=\"username\" autofocus=\"autofocus\" title=\"Please enter username\" maxlength\"40\" required></td>" +
                                        "<td></td>" +
                                    "</tr>" +
                                    "<tr>" +
                                        "<th id=\"thc\">Login</th>" +
                                        "<td>Password:</td>" +
                                        "<td><input type=\"password\" name=\"password\" title=\"Please enter password\" maxlength\"40\" required></td>" +
                                        "<td id=\"bt\" ><input type=\"submit\" value=\"Submit\" title=\"Submit Details\"/></td>" +
                                    "</tr>" +
                                "</table>" +
                            "</form>" +
                        "</div>");
// Heading
            out.println("<div class=\"heading\">" +
                            "<table>" +
                                "<tr><td>&nbsp;</td></tr>" +
                                "<tr><td>&nbsp;</td></tr>" +
                                "<tr><td><a align=\"left\" href=\"index\" title=\"Return To Homepage (Alt + 7)\" accesskey=\"7\">" +
                                         "<img src=\"http://s21.postimg.org/gyukaf1l3/Logo.png\" alt=\"Event Logo\" id=\"img150\"></a></td>" +
                                "<td><h1>Login Failed</h1></td></tr>" +
                            "</table>" +
                        "</div>");
// Navigation menu
            out.println("<div class=\"navigation dontprint\"><span>" +
                            "<form action=\"show_speakers\" method=\"get\"><button name=\"buttonSpeakers\" title=\"Event Speakers (Alt + 1)\">Event Speakers</button></form>" +
                            "<form action=\"show_workshops\" method=\"get\"><button name=\"buttonWorkshops\" title=\"Event Workshops (Alt + 2)\">Event Workshops</button></form>" +
                            "<form action=\"show_schedule\" method=\"get\"><button name=\"buttonSchedule\" title=\"Event Schedule (Alt + 3)\">Event Schedule</button></form>" +
                            "<form action=\"show_exhibitors\" method=\"get\"><button name=\"buttonExhibitors\" title=\"Event Exhibitors (Alt + 4)\">Event Exhibitors</button></form>" +
                            "<form action=\"reg_admin\" method=\"get\"><button name=\"buttonRegAdmin\" title=\"Administrator Registration Page (Alt + 5)\">Administrator Registration</button></form>" +
                            "<form action=\"reg_attendee.html\" method=\"get\"><button name=\"buttonRegAttendee\" title=\"Attendee Registration Page (Alt + 6)\">Attendee Registration</button></form>" +
                            "<form action=\"index\" method=\"get\"><button name=\"buttonHome\" title=\"Return To Homepage (Alt + 7)\">Home</button></form>" +
                        "</span></div>");
            
// Unsuccessful Login Greeting
            out.println("<div class=\"mainbody\">" +        
                            "<ul><h2>Hello " + request.getParameter("username") + ", The Username Or Password Entered Is Incorrect!!!</h2></ul>" +   
                            "<form><a href=\"index\" title=\"Return To Homepage (Alt + 7)\"><button name=\"button\" value=\"OK\" type=\"button\">Go Back</button></a></form><br>" +
                        "</div>");
            
// Bottom Links                    
            out.println("<div id=\"bl\" class=\"bottomlinks\">" +
                            "<table align=\"center\">" +
                                "<tr><th>Display:</th><th>Register:</th><th>Other:</th><tr>" +
                                "<tr><td><a href=\"show_speakers\" title=\"Show Speakers (Alt + 1)\" accesskey=\"1\">1. Show Speakers</a></td><td><a href=\"reg_admin\" title=\"Administrator Registration Page (Alt + 5)\" accesskey=\"5\">5. Administrator Registration</a></td><td><a href=\"index\" title=\"Return To Homepage (Alt + 7)\" accesskey=\"7\">7. Home Page</a></td></tr>" +
                                "<tr><td><a href=\"show_workshops\" title=\"Show Workshops (Alt + 2)\" accesskey=\"2\">2. Show Workshops</a></td><td><a href=\"reg_attendee.html\" title=\"Attendee Registration Page (Alt + 6)\" accesskey=\"6\">6. Attendee Registration</a></td><td></td></tr>" +
                                "<tr><td><a href=\"show_schedule\" title=\"Show Schedule (Alt + 3)\" accesskey=\"3\">3. Show Schedule</a></td><td></td><td></td></tr>" +
                                "<tr><td><a href=\"show_exhibitors\" title=\"Show Exhibitors (Alt + 4)\" accesskey=\"4\">4. Show Exhibitors</a></td><td></td><td></td></tr>" +
                            "</table>" +
                        "</div>");
            out.println("</body></html>");
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
