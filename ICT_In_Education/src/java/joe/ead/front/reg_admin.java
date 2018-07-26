/**
 *
 * @author Joe O'Regan
 * Student Number: K00203642
 */
package joe.ead.front;

import com.mysql.jdbc.Connection;
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
import joe.ead.abstracted.Connect; // 24/07/2018
import joe.ead.abstracted.Menu; // 24/07/2018

@WebServlet(urlPatterns = {"/reg_admin"})
public class reg_admin extends HttpServlet {
    // Connection
    Connection conn;
    Statement stat;
    String title = "Administrator Registration";
    // List of counties for drop down list
    String[] counties = {"Antrim","Armagh","Carlow","Cavan","Clare","Cork","Derry","Donegal","Down","Dublin","Fermanagh",
                        "Galway","Kerry","Kildare","Kilkenny","Laois","Leitrim","Limerick","Longford","Louth","Mayo","Meath",
                        "Monaghan","Offaly","Roscommon","Sligo","Tipperary","Tyrone","Waterford","Westmeath","Wexford","Wicklow"};
    String db_username;
    String check_username;
    int admin_count;
    
    public void init() throws ServletException {
        try {
            Class.forName("com.mysql.jdbc.Driver");
            conn = (com.mysql.jdbc.Connection) DriverManager.getConnection (Connect.url+Connect.dbName,Connect.userName,Connect.password);
            stat = (Statement) conn.createStatement();
            //java.sql.Statement stmt = conn.createStatement(); 
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
        response.setContentType("text/html;charset=UTF-8");
        
        try (PrintWriter out = response.getWriter()) {
            Menu menu = new Menu();
            
            out.println("<!DOCTYPE html>" +
                        "<html>" +
                        "<head>" +
                            "<link rel=\"stylesheet\" type=\"text/css\" href=\"css/CAstyle.css\">" +
                            "<title>"+title+"</title>" +
                        "</head>");
// Admin Login
            out.println("<body>" +
                            "<div class=\"login\">" +
                                "<form action=\"login\" method=\"Get\">" +
                                    "<table>" +
                                        "<tr>" +
                                            "<td width=100% rowspan=\"2\"><a align=\"left\" href=\"index\" title=\"Return To Homepage (Alt + 7)\" accesskey=\"7\"><img src=\"images/logo50.png\" alt=\"Random ICT Event Logo\" id=\"img50\"></a></td>" +
                                            "<th id=\"thc\">Administrator</th>" +
                                            "<td>Username:</td>" +
                                            "<td id=\"bt\"><input type=\"text\" name=\"username\" autofocus=\"autofocus\" title=\"Please enter username\" maxlength\"40\" required></td>" +
                                            "<td></td>" +
                                        "</tr>" +
                                        "<tr>" +
                                            "<th id=\"thc\">Login</th>" +
                                            "<td>Password:</td>" +
                                            "<td><input type=\"password\" name=\"password\" title=\"Please enter password\" maxlength\"40\" required></td>" +
                                            "<td id=\"bt\"><input type=\"submit\" value=\"Submit\" title=\"Submit Details\"/></td>" +
                                        "</tr>" +
                                    "</table>" +
                                "</form></div>");

            menu.heading(request, out, title); // Page Heading
            menu.navigationMenu(out, menu.REG_ADMIN, "navigation"); // Navigation menu
           
// Count admins
            try {
                admin_count = 0;
                java.sql.Statement stmt = conn.createStatement();
                ResultSet admins = stmt.executeQuery("SELECT count(*) AS admin_count FROM Administrators");
                while (admins.next()) {
                    admin_count = admins.getInt("admin_count");
                }
            } catch (Exception e) {
                System.err.println(e);
            } 
                     
// Check username available           
            if (admin_count>0) {   // only display username check if there is data to check
                out.println("<div class=\"mainbody\" id=\"sp0\">" +
                    "<form action=\"check_username\" method=\"POST\">" +
                    "<table align=\"center\">" +
                        "<tr><td class=\"mainhead\" colspan=\"2\">Check Username Available</td></tr>" +
                        "<tr class=\"tbody\"><th>Enter Username:</th><td><input type=\"text\" name=\"username\" maxlength=\"40\" placeholder=\"Enter up to 40 characters\" required></td></tr>" +
                        "<tr><td  class=\"tbase\" colspan=\"2\" id=\"bt\"><input type=\"submit\" value=\"Submit\"></td></tr>");
                out.println("</table>" +  
                        "</form>" + 
                    "</div>");
            } // end check
            
// Register Admin
            out.println("<div class=\"mainbody\">" +
                            "<form action=\"add_admin\" method=\"POST\"><br>" +
                                "<table align=\"center\" id=\"sp0\">" +
                                    "<tr><td class=\"mainhead\" colspan=\"3\">"+title+"</td></tr>" +
                                    "<tr class=\"tbody\"><td><b>Username:</b></td>" +
                                        "<td><input type=\"text\" name=\"admin_username\" autofocus=\"autofocus\" title=\"Enter username\" maxlength=\"40\" placeholder=\"Required Field\" required></td><td>Max 40 characters</td>" +
                                    "</tr>" +
                                    "<tr class=\"tbody\"><td><b>Password:</b></td>" +
                                        "<td><input type=\"password\" name=\"admin_password\" title=\"Enter password\" maxlength=\"40\" placeholder=\"Required Field\" required></td><td>Max 40 characters</td>" +
                                    "</tr>" +
                                    "<tr class=\"tbody\"><td colspan=\"3\"><br></td></tr>" +
                    
                                    "<tr class=\"tbody\"><td><b>First Name:</b></td>" +
                                        "<td><input type=\"text\" name=\"admin_fname\" title=\"Enter administrator first name\" maxlength=\"40\"></td><td>Max 40 characters</td>" +
                                    "</tr>" +
                                    "<tr class=\"tbody\"><td><b>Last Name:</b></td>" +
                                        "<td><input type=\"text\" name=\"admin_lname\" title=\"Enter administrator last name\" maxlength=\"40\"></td><td>Max 40 characters</td>" +
                                    "</tr>" +
                                    "<tr class=\"tbody\"><td><b>Email Address:</b></td>" +
                                        "<td><input type=\"text\" name=\"admin_email\" title=\"Enter an email address\" maxlength=\"60\" placeholder=\"Required Field\" required></td><td>Max 60 Characters</td>" +
                                    "</tr>" +
                                    "<tr class=\"tbody\"><td><b>Phone Number:</b></td>" +
                                        "<td><input type=\"text\" name=\"admin_phone\" title=\"Enter a phone number\" maxlength=\"18\"></td><td>Max 18 characters</td>" +
                                    "</tr>" +
                                    "<tr class=\"tbody\"><td><b>Address Line 1:</b></td>" +
                                        "<td><input type=\"text\" name=\"admin_addr1\" title=\"Enter address line 1\" maxlength=\"40\"></td><td>Max 40 characters</td>" +
                                    "</tr>" +
                                    "<tr class=\"tbody\"><td><b>Address Line 2:</b></td>" +
                                        "<td><input type=\"text\" name=\"admin_addr2\" title=\"Enter address line 2\" maxlength=\"40\"></td><td>Max 40 characters</td>" +
                                    "</tr>" +
                                    "<tr class=\"tbody\"><td><b>Town:</b></td>" +
                                        "<td><input type=\"text\" name=\"admin_town\" title=\"Enter a town\" maxlength=\"40\"></td><td>Max 40 characters</td>" +
                                    "</tr>" +
                                    "<tr class=\"tbody\"><td><b>County:</b></td>" +
                                        "<td><input list=\"admin_county\" name=\"admin_county\" title=\"Select A Country\" maxlength=\"40\">" +
                                        "<datalist id=\"admin_county\">");
                                        for (int i=0;i<32;i++) { // display each county as list item
                                            out.println("<option value=\""+counties[i]+"\">");
                                        }
                                        out.println("</datalist></td><td>Max 40 characters</td>" +
                                    "</tr>" +
                                    "<tr class=\"tbody\"><td colspan=\"3\">&nbsp</td></tr>" +
                                    "<tr><td class=\"tbase\" colspan=\"3\" id=\"bt\"><input type=\"submit\" value=\"Submit\" title=\"Submit Details\"/></td>" +
                                    "</tr>" +
                                "</table>" +
                                "</form>" +
                            "</div><br>");

            menu.bottomMenu(request,out); // Bottom Links 
                        
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