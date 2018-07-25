package joe.lit.ead;
import joe.ait.cse.Connect;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
import java.sql.DriverManager;
import com.mysql.jdbc.Connection;
import com.mysql.jdbc.PreparedStatement;
import com.mysql.jdbc.Statement;
import java.io.IOException;
import java.io.PrintWriter;
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
@WebServlet(urlPatterns = {"/check_username"})
public class check_username extends HttpServlet {
    String title = "Check Username";
    String username;
    String DB_username;
    String docType = "<!doctype html >";
    // Connection
    Connection conn;
    PreparedStatement prepStat;
    Statement stat;
    
    
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
        
        PrintWriter out = response.getWriter(); 
        
        username = request.getParameter("username");
        
        out.println(docType + 
                "<html>" +                
                  "<head>" +
                    "<link rel=\"stylesheet\" type=\"text/css\" href=\"CAstyle.css\">" + 
                    "<title>" + title + "</title>" +
                  "</head>" +    
                  "<body>");
        try {
            java.sql.Statement stmt = conn.createStatement(); 
            ResultSet result = stmt.executeQuery("SELECT * FROM administrators WHERE admin_username = '"+username+"'");  // works OK
            result.next();
            DB_username = result.getString("admin_username");
            }
        catch (Exception e)
        {
            System.err.println(e);
        }    
        boolean  passwordValidate = username.contentEquals( DB_username );
        
        if (passwordValidate == true)
        {   
// Successful Login Greeting
            out.println("<div class=\"mainbody\"><br>" +
                            "<h2>The Username \""+username+"\" is already in the database</h2>" +
                            "<p>Press Enter To Return" +
                            "<form><a href=\"reg_admin\" title=\"Return To Administrator Registration\"><button name=\"button\" autofocus=\"autofocus\" value=\"OK\" type=\"button\">Continue</button></a></form><br>" +
                        "</div>");            
        }
        else
        {
// Unsuccessful Login Greeting
            out.println("<div class=\"mainbody\">" +   
                            "<h2>The Username \""+username+"\" is available</h2>" +
                            "<p>Press Enter To Return" +
                            "<form><a href=\"reg_admin\" title=\"Return To Administrator Registration\"><button name=\"button\" autofocus=\"autofocus\" value=\"OK\" type=\"button\">Continue</button></a></form><br>" +
                        "</div>");            
        }
        out.println("</body></html>");
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
    //    response.sendRedirect("reg_admin");  // redirects back to RegAttendee.html after form submitted
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

    public void init() throws ServletException
    {
        try{
            Class.forName("com.mysql.jdbc.Driver");
            conn = (Connection) DriverManager.getConnection (Connect.url+Connect.dbName,Connect.userName,Connect.password);
            stat = (Statement) conn.createStatement();
        } catch (Exception e) 
        {
            System.err.println(e);
        }
    } // end of init() method
}
