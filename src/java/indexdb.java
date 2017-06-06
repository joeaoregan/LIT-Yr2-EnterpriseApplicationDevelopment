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

@WebServlet(urlPatterns = {"/indexdb"})
public class indexdb extends HttpServlet {
        String URL = "JDBC:MYSQL://LOCALHOST:3306/";
        String DB = "JoeCA";
        String USERNAME = "root";
        String PASSWORD = "password";
        Connection conn = null;
        
        String DB_username; 
        String DB_password;
        String username;
        String password;
        String title = "Administrator Login";
        String docType = "<!doctype html >";
   
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        
        
        PrintWriter out = response.getWriter(); 
        
        username = request.getParameter("username");    // username from html form
        password = request.getParameter("password");    // password form html form
        String passwordToGuess = "password";            // global password for every user
        //boolean  passwordValidate = password.contentEquals( passwordToGuess );
        
        out.println(docType + 
                "<html>" +                
                  "<head>" +
                    "<link rel=\"stylesheet\" type=\"text/css\" href=\"CAstyle.css\">" + 
                    "<title>" + title + "</title>" +
                  "</head>" +    
                  "<body>" +
                    "<div class=\"heading\"><br>" +
                      "<h1 align=\"center\">" + title + "</h1><br>" +
                    "</div>");
        //out.println("<li>form username: " + username + " form password: " + password);
        //out.println("<li>db username 1: " + DB_username + " db password 1: " + DB_password);
                        
        try{
            Class.forName("com.mysql.jdbc.Driver");
            conn = DriverManager .getConnection(URL + DB,USERNAME,PASSWORD);    // Establish/request a connection to a database
            Statement stmt = conn.createStatement(); 
            //ResultSet result = stmt.executeQuery("SELECT admin_username,admin_password FROM administrators WHERE admin_username = " + username + ")");
            //ResultSet result = stmt.executeQuery("SELECT * FROM administrators WHERE admin_username = 'Joe')");
            //ResultSet result = stmt.executeQuery("SELECT * FROM administrators WHERE admin_username = 'Joe'");  // works OK
            ResultSet result = stmt.executeQuery("SELECT * FROM administrators WHERE admin_username = '"+username+"'");  // works OK
            result.next();
            
            //while(result.next()){
            DB_username = result.getString("admin_username");
            DB_password = result.getString("admin_password"); 
            
           // out.println("<li>db username 2: " + DB_username + " db password 2: " + DB_password);
        }
        catch(Exception e)
        {
            System.err.println(e);
        }
           
        boolean  passwordValidate = password.contentEquals( DB_password );
        //out.println("<li>db username 3: " + DB_username + " db password 3: " + DB_password);

        if (passwordValidate == true)
        {
            out.println(
                    "<div class=\"navigation\">\n" +
                        "<form style=\"display: inline\" action=\"eventAdministration.html\" method=\"get\"><button name\"buttonEventAdmin\" title=\"Event Administration Page (Alt + 1)\">Event Administration Page</button></form>\n" +
                        "<form style=\"display: inline\" action=\"reg_admin.html\" method=\"get\"><button name=\"buttonRegAdmin\" title=\"Administrator Registration Page (Alt + 2)\">Administrator Registration</button></form>\n" +
                        "<form style=\"display: inline\" action=\"reg_attendee.html\" method=\"get\"><button name=\"buttonRegAttendee\" title=\"Attendee Registration Page (Alt + 3)\">Attendee Registration</button></form>\n" +
                        "<form style=\"display: inline\" action=\"index.html\" method=\"get\"><button name=\"buttonHome\" title=\"Return To Home Page (Alt + 5)\">Return To Home Page</button></form>\n" +
                    "</div>" +  

                    "<div class=\"mainbody\"><br>" +
                        "<ul><h2>Hello " + request.getParameter("username") + ", welcome back!!!</h2></ul>" +  // or FIRST NAME LAST NAME
                        "<form><a href=\"eventAdministration.html\" title=\"Go To Event Administation Page\"><button name=\"button\" autofocus=\"autofocus\" value=\"OK\" type=\"button\">Continue</button></a></form><br>" +
                    "</div>" + 

                    "<div class=\"bottomlinks\"><br>" +
                            "<a href=\"eventAdministration.html\" title=\"Event Administation Page (Alt + 1)\" accesskey=\"1\">1. Continue To Event Administration Page</a><br>" +
                            "<a href=\"reg_admin.html\" title=\"Administrator Registration Page (Alt + 2)\" accesskey=\"2\">2. Administrator Registration</a><br>" +
                            "<a href=\"reg_attendee.html\" title=\"Attendee Registration Page (Alt + 3)\" accesskey=\"3\">3. Attendee Registration</a><br>" +
                            "<a href=\"index.html\" title=\"Return To Homepage (Alt + 4)\" accesskey=\"4\">4. Return To Home Page</a><br>" +
                    "</div>" +
                "</body></html>");
        }
        else
        {
            out.println(
                "<div class=\"navigation\">\n" +
                    "<form style=\"display: inline\" action=\"index.html\" method=\"get\"><button name=\"buttonHome\" title=\"Return To Home Page (Alt + 1)\">Return To Home Page</button></form>\n" +
                "</div>" +
                        
                "<div class=\"mainbody\">" +        
                    "<ul><h2>Hello " + request.getParameter("username") + ", The Password Entered Is Incorrect!!!</h2></ul>\n" +   
                    "<form><a href=\"index.html\" title=\"Return To Homepage (Alt + 1)\"><button name=\"button\" autofocus=\"autofocus\" value=\"OK\" type=\"button\">Go Back</button></a></form><br>" +
                "</div>" +
                        
                "<div class=\"bottomlinks\"><br>" +        
                    "<a href=\"index.html\" title=\"Return To Homepage (Alt + 1)\" accesskey=\"1\">Return to Home Page</a>" +
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
