/**
 *
 * @author Joe O'Regan
 * Student Number: K00203642
 */
package joe.ead.front;

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
import joe.ead.abstracted.Menu; // 24/07/2018

@WebServlet(urlPatterns = {"/show_workshops"})
public class show_workshops extends HttpServlet {
    String title = "Workshops";
    String tableheading = "Current Workshops";
    Connection conn;
    
    String ws_id;
    String ws_name;
    String ws_pres1;
    String ws_pres2;;
    String ws_info;
    int ws_num = 1;
    int workshop_count;
       
    public void init() throws ServletException {
        try{
            Class.forName("com.mysql.jdbc.Driver");
            //conn = (com.mysql.jdbc.Connection) DriverManager.getConnection (Connect.url+Connect.dbName,Connect.userName,Connect.password);
            conn = Connect.getConnection();
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
                        "<html lang=\"en\">" +
                        "<head>" +
                            "<link rel=\"stylesheet\" type=\"text/css\" href=\"css/CAstyle.css\">" +
                    "<title>" + title + "</title>" +
                    "</head><body>");

            menu.heading(request, out, title); // Page Heading
            menu.navigationMenu(out, menu.SHOW_WORKSHOPS, "navigation"); // Navigation menu
            
// Count the number of workshops scheduled (from Workshops table)
            try {
                java.sql.Statement stmt = conn.createStatement();
                workshop_count = 0;
                ResultSet schedule = stmt.executeQuery("SELECT COUNT(*) AS counter FROM Workshops WHERE ws_id != 1;");
                schedule.next();        
                workshop_count = schedule.getInt("counter");
            } catch (Exception e) {
                System.err.println(e);
            }
            
// Current Workshops
            if(workshop_count==0){
                out.println("<div class=\"mainbody\"><br><h2>There are no workshops currently scheduled</h2>");
                out.println("<h3>Register for updates<h3>"
                        + "<form action=\"reg_attendee.html\" method=\"get\"><button name=\"buttonRegAttendee\" title=\"Attendee Registration Page (Alt + 6)\">Register</button></form></div>");
            } else {            
                out.println("<div class=\"mainbody\">" +
                                "<table align=\"center\">" +
                                    "<tr><td class=\"mainhead\" colspan=\"4\">"+tableheading+"</td></tr>");
                if (workshop_count < 1)         out.println("<tr><td class=\"mainbase\" colspan=\"4\">There are no workshops currently scheduled:</td></tr>");
                else if (workshop_count == 1)   out.println("<tr><td class=\"mainbase\" colspan=\"4\">There is "+workshop_count+" workshops currently scheduled:</td></tr>");
                else                            out.println("<tr><td class=\"mainbase\" colspan=\"4\">There are "+workshop_count+" workshops currently scheduled:</td></tr>");

                out.println("<tr><td colspan=\"4\">&nbsp;</td></tr>");     

                try {
                    java.sql.Statement stmt = conn.createStatement();
                    ResultSet speakers = stmt.executeQuery("SELECT * FROM Workshops WHERE ws_name NOT LIKE 'Break'");
                    ws_num=1;                                                                                                                                   // workshop number

                    while (speakers.next()) {
                        ws_id = speakers.getString("ws_id");
                        ws_name = speakers.getString("ws_name");
                        ws_pres1 = speakers.getString("ws_presenter1");
                        ws_pres2 = speakers.getString("ws_presenter2");
                        ws_info = speakers.getString("ws_info");

                        out.println("<tr><th colspan=\"4\" class=\"thead\">Workshop "+ws_num+": "+ws_name+"</th></tr>");                                            // heading
                        // Format Output
                        if (ws_pres2.contentEquals( "" )) out.println("<tr class=\"tbody\"><th>Presenter:</th><td colspan=\"3\">"+ws_pres1+"</td></tr>");           // 2 presenters
                        else out.println("<tr class=\"tbody\"><th>Presenter 1:</th><td>"+ws_pres1+"</td><th>Presenter 2:</th><td>"+ws_pres2+"</td></tr>");          // 1 presenter                               

                        out.println("<tr class=\"tbody\"><th>About:</th><td colspan=\"3\">"+ws_info+"</td></tr>" +                                                  // line
                                    "<tr><td  class=\"tbase\"colspan=\"4\">&nbsp;</td></tr>" +
                                    "<tr><td colspan=\"4\">&nbsp;</td></tr>");
                        ws_num++;
                    }
                } catch (Exception e) {
                    System.err.println(e);
                }            
                out.println("</table></div>");
            } // End else (show workshops)
            
            menu.bottomMenu(request,out); // Bottom Links 
                        
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
