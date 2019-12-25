/**
 *
 * @author Joe O'Regan
 * Student Number: K00203642
 */
package joe.ead.manage;

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
import joe.ead.abstracted.Connect; // 26/07/2018
import joe.ead.abstracted.Menu; // 26/07/2018

@WebServlet(urlPatterns = {"/login"})
public class Login extends HttpServlet {
        Connection conn = null;   
        String DB_password;
        String title = "Event Administration";
        String docType = "<!doctype html >";
   
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        
        PrintWriter out = response.getWriter(); 
        Menu menu = new Menu();        
        String username = request.getParameter("username");    // username from html form
        String password = request.getParameter("password");    // password form html form
                        
        try{
            Class.forName("com.mysql.jdbc.Driver");
            //conn = (com.mysql.jdbc.Connection) DriverManager.getConnection (Connect.url+Connect.dbName,Connect.userName,Connect.password);    // Establish/request a connection to a database
            conn = Connect.getConnection();
            Statement stmt = conn.createStatement(); 
            ResultSet result = stmt.executeQuery("SELECT * FROM administrators WHERE admin_username = '"+username+"'");  // works OK
            result.next();
            DB_password = result.getString("admin_password"); 
        } catch(Exception e) {
            System.err.println(e);
        }           
        
        out.println(docType + 
                "<html>" +                
                  "<head>" +
                    "<link rel=\"stylesheet\" type=\"text/css\" href=\"css/CAstyle.css\">" + 
                    "<title>" + title + "</title>" +
                  "</head>" +    
                  "<body>");        

        if (password.equals(DB_password)) {     
            showSuccessfulLogin(out, request);
        } else {
            showUnsuccessfulLogin(out,request,"Login Failed");
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

    private void showSuccessfulLogin(PrintWriter out, HttpServletRequest request) {
        Menu menu = new Menu();
        
        menu.heading(request, out, title); // Heading
        menu.navigationMenuManage(out, menu.HIGHLIGHT_LOGIN); // Navigation menu (Event Administration Highlighted)

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

        menu.bottomMenuManage(out); // Bottom Links (Manage)          
    }
    
    
    private void showUnsuccessfulLogin(PrintWriter out, HttpServletRequest request, String title) {
        Menu menu = new Menu();
        
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

        menu.heading(request, out, title); // Heading            
        menu.navigationMenu(out, 0, "navigation"); // Navigation menu

// Unsuccessful Login Greeting
        out.println("<div class=\"mainbody\">" +        
                        "<ul><h2>Hello " + request.getParameter("username") + ", The Username Or Password Entered Is Incorrect!!!</h2></ul>" +   
                        "<form><a href=\"index\" title=\"Return To Homepage (Alt + 7)\"><button name=\"button\" value=\"OK\" type=\"button\">Go Back</button></a></form><br>" +
                    "</div>");            

        menu.bottomMenuManage(out); // Bottom Links (Manage)  
    }
}
