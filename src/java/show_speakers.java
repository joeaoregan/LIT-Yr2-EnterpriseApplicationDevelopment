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
@WebServlet(urlPatterns = {"/show_speakers"})
public class show_speakers extends HttpServlet {
    String tableheading = "Current Speakers";
    String title = "Speakers";
    String selectQuery = "id";
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
                    "<head ><title>" + title + "</title></head>" +
                        "</head>");

            out.println("<body>" +
                        "<div class=\"heading\">" +
                        "<table>" +
                            "<tr><td><a align=\"left\" href=\"index\" title=\"Return To Homepage (Alt + 7)\" accesskey=\"7\"><img src=\"http://s21.postimg.org/gyukaf1l3/Logo.png\" alt=\"Event Logo\" style=\"width:150px;height:150px;\"></a></td>" +
                            "<td><h1 style=\"text-align:center\">" + title + "</h1></td></tr>" +
                        "</table>" +
                        "</div>");
// Navigation menu
            out.println("<div class=\"navigation\">" +
                        "<form style=\"display: inline\" action=\"show_speakers\" method=\"get\"><button style=\"color: blue; background-color: white;\" name=\"buttonSpeakers\" title=\"Event Speakers (Alt + 1)\">Event Speakers</button></form>" +
                        "<form style=\"display: inline\" action=\"show_workshops\" method=\"get\"><button name=\"buttonWorkshops\" title=\"Event Workshops (Alt + 2)\">Event Workshops</button></form>" +
                        "<form style=\"display: inline\" action=\"show_schedule\" method=\"get\"><button name=\"buttonSchedule\" title=\"Event Schedule (Alt + 3)\">Event Schedule</button></form>" +
                        "<form style=\"display: inline\" action=\"show_exhibitors\" method=\"get\"><button name=\"buttonExhibitors\" title=\"Event Exhibitors (Alt + 4)\">Event Exhibitors</button></form>" +
                        "<form style=\"display: inline\" action=\"reg_admin.html\" method=\"get\"><button name=\"buttonRegAdmin\" title=\"Administrator Registration Page (Alt + 5)\">Administrator Registration</button></form>" +
                        "<form style=\"display: inline\" action=\"reg_attendee.html\" method=\"get\"><button name=\"buttonRegAttendee\" title=\"Attendee Registration Page (Alt + 6)\">Attendee Registration</button></form>" +
                        "<form style=\"display: inline\" action=\"index\" method=\"get\"><button title=\"Return To Homepage (Alt + 7)\">Home</button></form>" +
                    "</div>");

// Show speakers            
            out.println("<div class=\"mainbody\">" +
                            "<table align=\"center\">" +
                                "<tr><td class=\"tbhead\" colspan=\"3\">"+tableheading+"</td></tr>" +
                                "<tr><td colspan=\"3\">&nbsp;</td></tr>" +
                                "<tr><td colspan=\"3\">A list of the keynote speakers currently scheduled:</td></tr>" +
                                "<tr><td colspan=\"3\">&nbsp;</td></tr>");
            try {
                java.sql.Statement stmt = conn.createStatement();
                ResultSet speakers = stmt.executeQuery("SELECT * FROM Speakers ORDER BY speaker_lname");
                
                speak_num=1;
                while (speakers.next()) {
                    sp_id = speakers.getString("speaker_id");
                    speak_name = speakers.getString("speaker_fname") + " " + speakers.getString("speaker_lname");
                    speak_bio = speakers.getString("speaker_bio");
                    speak_site = speakers.getString("speaker_website");
                    speak_pic = speakers.getString("speaker_pic");
                /*
                out.println("<tr><td rowspan=\"6\"><img src="+speak_pic+" alt=\"Speaker Picture For "+speak_name+"\" style=\"width:200px;height:200px;\"></td><td></td><td></td></tr>" +
                            //"<tr><th colspan=\"2\" style=\"text-align: center\">Keynote Speaker "+speak_num+"</th></tr>" +
                            "<tr><th colspan=\"2\" class=\"thead\">Keynote Speaker "+speak_num+"</th></tr>" +
                            "<tr><th>DB ID:</th><td>" + sp_id + "</td></tr>" +
                            "<tr><th>Name:</th><td>" + speak_name + "</td></tr>" +
                            "<tr valign=\"top\"><th>Biography:</th><td>" + speak_bio + "</td></tr>" +
                            "<tr><th>Website:</th><td><a href=\"" + speak_site + "\">\"" + speak_site + "\"</a></td></tr>"
                            ); //+ "<tr><td colspan=\"3\"><hr></td></tr>");
                */
                out.println("<tr><th colspan=\"3\" class=\"thead\">Keynote Speaker "+speak_num+": "+speak_name+"</th></tr>" +
                            "<tr><td rowspan=\"3\"><img src="+speak_pic+" alt=\"Speaker Picture For "+speak_name+"\" style=\"width:200px;height:200px;\"></td><td></td><td></td></tr>" +
                            //"<tr><th>DB ID:</th><td>" + sp_id + "</td></tr>" +
                            //"<tr><th>Name:</th><td>" + speak_name + "</td></tr>" +
                            "<tr valign=\"top\"><th>About:</th><td>" + speak_bio + "</td></tr>" +
                            "<tr><th>Website:</th><td><a href=\"" + speak_site + "\">\"" + speak_site + "\"</a></td></tr>");
                speak_num++;
                }
            }
            catch (Exception e)
            {
                System.err.println(e);
            }            
            out.println("</table></div>");
   
// Navigation menu
            out.println("<div class=\"navigation\">" +
                        "<form style=\"display: inline\" action=\"show_speakers\" method=\"get\"><button style=\"color: blue; background-color: white;\" name=\"buttonSpeakers\" title=\"Event Speakers (Alt + 1)\">Event Speakers</button></form>" +
                        "<form style=\"display: inline\" action=\"show_workshops\" method=\"get\"><button name=\"buttonWorkshops\" title=\"Event Workshops (Alt + 2)\">Event Workshops</button></form>" +
                        "<form style=\"display: inline\" action=\"show_schedule\" method=\"get\"><button name=\"buttonSchedule\" title=\"Event Schedule (Alt + 3)\">Event Schedule</button></form>" +
                        "<form style=\"display: inline\" action=\"show_exhibitors\" method=\"get\"><button name=\"buttonExhibitors\" title=\"Event Exhibitors (Alt + 4)\">Event Exhibitors</button></form>" +
                        "<form style=\"display: inline\" action=\"reg_admin.html\" method=\"get\"><button name=\"buttonRegAdmin\" title=\"Administrator Registration Page (Alt + 5)\">Administrator Registration</button></form>" +
                        "<form style=\"display: inline\" action=\"reg_attendee.html\" method=\"get\"><button name=\"buttonRegAttendee\" title=\"Attendee Registration Page (Alt + 6)\">Attendee Registration</button></form>" +
                        "<form style=\"display: inline\" action=\"index\" method=\"get\"><button name=\"buttonHome\" title=\"Return To Homepage (Alt + 7)\">Home</button></form>" +
                    "</div>");
// Bottom Links                    
            out.println("<div id=\"bl\" class=\"bottomlinks\">\n" +
                "<table align=\"center\">\n" +
                    "<tr><th>Display:</th><th>Register:</th><th>Other:</th><tr>\n" +
                    "<tr><td><a href=\"show_speakers\" title=\"Show Speakers (Alt + 1)\" accesskey=\"1\">1. Show Speakers</a></td><td><a href=\"reg_admin.html\" title=\"Administrator Registration Page (Alt + 5)\" accesskey=\"5\">5. Administrator Registration</a></td><td><a href=\"index\" title=\"Return To Homepage (Alt + 7)\" accesskey=\"7\">7. Home Page</a></td></tr>" +
                    "<tr><td><a href=\"show_workshops\" title=\"Show Workshops (Alt + 2)\" accesskey=\"2\">2. Show Workshops</a></td><td><a href=\"reg_attendee.html\" title=\"Attendee Registration Page (Alt + 6)\" accesskey=\"6\">6. Attendee Registration</a></td><td></td></tr>" +
                    "<tr><td><a href=\"show_schedule\" title=\"Show Schedule (Alt + 3)\" accesskey=\"3\">3. Show Schedule</a></td><td></td><td></td></tr>" +
                    "<tr><td><a href=\"show_exhibitors\" title=\"Show Exhibitors (Alt + 4)\" accesskey=\"4\">4. Show Exhibitors</a></td><td></td><td></td></tr>" +
                "</table>" +
            "</div>");
                        
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
