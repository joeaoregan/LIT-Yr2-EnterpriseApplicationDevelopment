/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import com.mysql.jdbc.Connection;
import com.mysql.jdbc.Statement;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.DriverManager;
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
@WebServlet(urlPatterns = {"/reg_admin"})
public class reg_admin extends HttpServlet {
    // Connection
    Connection conn;
    Statement stat;
    String title = "Administrator Registration";
    String[] counties = {"Antrim","Armagh","Carlow","Cavan","Clare","Cork","Derry","Donegal","Down","Dublin","Fermanagh",
                        "Galway","Kerry","Kildare","Kilkenny","Laois","Leitrim","Limerick","Longford","Louth","Mayo","Meath",
                        "Monaghan","Offaly","Roscommon","Sligo","Tipperary","Tyrone","Waterford","Westmeath","Wexford","Wicklow"};
    String form_ad_username;
       
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
                        "</head>");
// Admin Login
            out.println("<body>" +
                            "<div class=\"login\">" +
                                "<form action=\"Login\" method=\"Get\">" +
                                    "<table>" +
                                        "<tr>" +
                                            "<td width=100% rowspan=\"2\"><a align=\"left\" href=\"index\" title=\"Return To Homepage (Alt + 7)\" accesskey=\"7\"><img src=\"http://s21.postimg.org/gyukaf1l3/Logo.png\" alt=\"Event Logo\" style=\"width:50px;height:50px;\"></a></td>" +
                                            "<th style=\"text-align:center\">Administrator</th>" +
                                            "<td>Username:</td>" +
                                            "<td><input type=\"text\" name=\"username\" autofocus=\"autofocus\" title=\"Please enter username\"></td>" +
                                            "<td></td>" +
                                        "</tr>" +
                                        "<tr>" +
                                            "<th style=\"text-align:center\">Login</th>" +
                                            "<td>Password:</td>" +
                                            "<td><input type=\"password\" name=\"password\" title=\"Please enter password\"></td>" +
                                            "<td style=\"text-align:right\"><input type=\"submit\" value=\"Submit\" title=\"Submit Details\"/></td>" +
                                        "</tr>" +
                                    "</table>" +
                                "</form>" +
                            "</div>");
// Heading
            out.println("<div class=\"heading\">" +
                            "<table>" +
                                "<tr><td>&nbsp;</td></tr>" +
                                "<tr><td>&nbsp;</td></tr>" +
                                "<tr><td><a align=\"left\" href=\"index\" title=\"Return To Homepage (Alt + 7)\" accesskey=\"7\"><img src=\"http://s21.postimg.org/gyukaf1l3/Logo.png\" alt=\"Event Logo\" style=\"width:150px;height:150px;\"></a></td>" +
                                "<td><h1 style=\"text-align:center\">" + title + "</h1></td></tr>" +
                            "</table>" +
                        "</div>");
                    
// Navigation menu
            out.println("<div class=\"navigation\">" +
                            "<form style=\"display: inline\" action=\"show_speakers\" method=\"get\"><button name=\"buttonSpeakers\" title=\"Event Speakers (Alt + 1)\">Event Speakers</button></form>" +
                            "<form style=\"display: inline\" action=\"show_workshops\" method=\"get\"><button name=\"buttonWorkshops\" title=\"Event Workshops (Alt + 2)\">Event Workshops</button></form>" +
                            "<form style=\"display: inline\" action=\"show_schedule\" method=\"get\"><button name=\"buttonSchedule\" title=\"Event Schedule (Alt + 3)\">Event Schedule</button></form>" +
                            "<form style=\"display: inline\" action=\"show_exhibitors\" method=\"get\"><button name=\"buttonExhibitors\" title=\"Event Exhibitors (Alt + 4)\">Event Exhibitors</button></form>" +
                            "<form style=\"display: inline\" action=\"reg_admin\" method=\"get\"><button style=\"color: blue; background-color: white;\" name=\"buttonRegAdmin\" title=\"Administrator Registration Page (Alt + 5)\">Administrator Registration</button></form>" +
                            "<form style=\"display: inline\" action=\"reg_attendee.html\" method=\"get\"><button name=\"buttonRegAttendee\" title=\"Attendee Registration Page (Alt + 6)\">Attendee Registration</button></form>" +
                            "<form style=\"display: inline\" action=\"index\" method=\"get\"><button name=\"buttonHome\" title=\"Return To Homepage (Alt + 7)\">Home</button></form>" +
                        "</div>");

            
// Register Admin
            //request.setAttribute("admin_username", form_username);
            out.println("<div class=\"mainbody\">" +
                            "<form action=\"add_admin\" method=\"POST\"><br>" +
                                "<table align=\"center\">" +
                                    "<tr><td class=\"tbhead\" colspan=\"3\">"+title+"</td></tr>" +
                                    "<tr><td><b>Username:</b></td>" +
                                        "<td><input type=\"text\" name=\"admin_username\" autofocus=\"autofocus\" title=\"Enter username\" maxlength=\"40\" placeholder=\"Required Field\"></td><td>Max 40 characters</td>" +
                                    "</tr>" +
                                    "<tr><td><b>Password:</b></td>" +
                                        "<td><input type=\"password\" name=\"admin_password\" title=\"Enter password\" maxlength=\"40\" placeholder=\"Required Field\"></td><td>Max 40 characters</td>" +
                                    "</tr>" +
                                    "<tr><td colspan=\"3\"><br></td></tr>" +
                    
                                    "<tr><td><b>First Name:</b></td>" +
                                        "<td><input type=\"text\" name=\"admin_fname\" title=\"Enter administrator first name\" maxlength=\"40\"></td><td>Max 40 characters</td>" +
                                    "</tr>" +
                                    "<tr><td><b>Last Name:</b></td>" +
                                        "<td><input type=\"text\" name=\"admin_lname\" title=\"Enter administrator last name\" maxlength=\"40\"></td><td>Max 40 characters</td>" +
                                    "</tr>" +
                                    "<tr><td><b>Email Address:</b></td>" +
                                        "<td><input type=\"text\" name=\"admin_email\" title=\"Enter an email address\" maxlength=\"60\" placeholder=\"Required Field\"></td><td>Max 60 Characters</td>" +
                                    "</tr>" +
                                    "<tr><td><b>Phone Number:</b></td>" +
                                        "<td><input type=\"text\" name=\"admin_phone\" title=\"Enter a phone number\" maxlength=\"18\"></td><td>Max 18 characters</td>" +
                                    "</tr>" +
                                    "<tr><td><b>Address Line 1:</b></td>" +
                                        "<td><input type=\"text\" name=\"admin_addr1\" title=\"Enter address line 1\" maxlength=\"40\"></td><td>Max 40 characters</td>" +
                                    "</tr>" +
                                    "<tr><td><b>Address Line 2:</b></td>" +
                                        "<td><input type=\"text\" name=\"admin_addr2\" title=\"Enter address line 2\" maxlength=\"40\"></td><td>Max 40 characters</td>" +
                                    "</tr>" +
                                        "<tr><td><b>Town:</b></td>" +
                                        "<td><input type=\"text\" name=\"admin_town\" title=\"Enter a town\" maxlength=\"40\"></td><td>Max 40 characters</td>" +
                                    "</tr>" +
                                    "<tr><td><b>County:</b></td>" +
                                        "<td><input list=\"admin_county\" name=\"admin_county\" title=\"Select A Country\" maxlength=\"40\">" +
                                        "<datalist id=\"admin_county\">");
                                        for (int i=0;i<26;i++)
                                        {
                                            out.println("<option value=\""+counties[i]+"\">");
                                        }
                                        out.println("</datalist></td><td>Max 40 characters</td>" +
                                    "</tr>" +
                                    "<tr><td></td><td></td><td style=\"text-align:right\"><input type=\"submit\" value=\"Submit\" title=\"Submit Details\"/></td>" +
                                    "</tr>" +
                                "</table>" +
                                "</form>" +
                            "</div><br>");
            
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