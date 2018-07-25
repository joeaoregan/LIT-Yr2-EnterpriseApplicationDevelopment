package joe.ait.cse;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author joeaoregan
 */

import java.io.PrintWriter;
import javax.servlet.http.HttpServletRequest;

public class Menu {
        
    // Bottom Links                    
    public void bottomMenu(HttpServletRequest request, PrintWriter out) {
        out.println("<div id=\"bl\" class=\"bottomlinks\">" +
            "<table align=\"center\">" +
                "<tr><th>Display:</th><th>Register:</th><td rowspan=\"5\"><a align=\"left\" href=\"index\" title=\"Return To Homepage (Alt + 7)\" accesskey=\"7\"><img src='" + request.getContextPath() + "/images/logoT.png' alt=\"Event Logo\" id=\"img100\"></a></td></tr>" +
                //"<tr><th>Display:</th><th>Register:</th><td rowspan=\"5\"><a align=\"left\" href=\"index\" title=\"Return To Homepage (Alt + 7)\" accesskey=\"7\"><img src=\"http://s21.postimg.org/gyukaf1l3/Logo.png\" alt=\"Event Logo\" id=\"img100\"></a></td></tr>" +
                "<tr><td><a href=\"show_speakers\" title=\"Show Speakers (Alt + 1)\" accesskey=\"1\">1. Show Speakers</a></td><td><a href=\"reg_admin\" title=\"Administrator Registration Page (Alt + 5)\" accesskey=\"5\">5. Administrator Registration</a></td></tr>" +
                "<tr><td><a href=\"show_workshops\" title=\"Show Workshops (Alt + 2)\" accesskey=\"2\">2. Show Workshops</a></td><td><a href=\"reg_attendee.html\" title=\"Attendee Registration Page (Alt + 6)\" accesskey=\"6\">6. Attendee Registration</a></td><td></td></tr>" +
                "<tr><td><a href=\"show_schedule\" title=\"Show Schedule (Alt + 3)\" accesskey=\"3\">3. Show Schedule</a></td><td></td><td></td></tr>" +
                "<tr><td><a href=\"show_exhibitors\" title=\"Show Exhibitors (Alt + 4)\" accesskey=\"4\">4. Show Exhibitors</a></td><td></td><td></td></tr>" +
            "</table>" +
        "</div>");
    }

}
