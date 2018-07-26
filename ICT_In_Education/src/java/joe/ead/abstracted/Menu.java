package joe.ead.abstracted;

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
    public static final int SHOW_SPEAKERS = 1;
    public static final int SHOW_WORKSHOPS = 2;
    public static final int SHOW_SCHEDULE = 3;
    public static final int SHOW_EXHIBITORS = 4;
    public static final int REG_ADMIN = 5;
    public static final int REG_ATTENDEE = 6;
    public static final int INDEX = 7;
    
    // Heading
    public void heading(HttpServletRequest request, PrintWriter out, String title) {
        out.println("<div class=\"heading\">" +
            "<table>" +
                
                "<tr><td>&nbsp;</td></tr><tr><td>&nbsp;</td></tr>" + // SORT THIS 
                
                "<tr><td><div class=\"logo\"><a align=\"left\" href=\"index\" title=\"Return To Homepage (Alt + 7)\" accesskey=\"7\">" +
                    "<img src='" + request.getContextPath() + "/images/logoT.png' alt=\"Event Logo\" id=\"img150\"></a></div></td>" +
                    "<td><h1>" + title + "</h1></td></tr>" +
            "</table>" +
        "</div>");
    }
    
    // Navigation menu
    public void navigationMenu(PrintWriter out, int highlight) {       
        out.println("<div class=\"navigation\"><span>" +
            "<form action=\"show_speakers\" method=\"get\"><button " + ((highlight == SHOW_SPEAKERS) ? "id=\"active\"" : "") + " name=\"buttonSpeakers\" title=\"Event Speakers (Alt + 1)\">Speakers</button></form>" +
            "<form action=\"show_workshops\" method=\"get\"><button " + ((highlight == SHOW_WORKSHOPS) ? "id=\"active\"" : "") + " name=\"buttonWorkshops\" title=\"Event Workshops (Alt + 2)\">Workshops</button></form>" +
            "<form action=\"show_schedule\" method=\"get\"><button " + ((highlight == SHOW_SCHEDULE) ? "id=\"active\"" : "") + " name=\"buttonSchedule\" title=\"Event Schedule (Alt + 3)\">Schedule</button></form>" +
            "<form action=\"show_exhibitors\" method=\"get\"><button " + ((highlight == SHOW_EXHIBITORS) ? "id=\"active\"" : "") + " name=\"buttonExhibitors\" title=\"Event Exhibitors (Alt + 4)\">Exhibitors</button></form>" +
            "<form action=\"reg_admin\" method=\"get\"><button " + ((highlight == REG_ADMIN) ? "id=\"active\"" : "") + " name=\"buttonRegAdmin\" title=\"Administrator Registration Page (Alt + 5)\">Administrator Registration</button></form>" +
            "<form action=\"reg_attendee.html\" method=\"get\"><button " + ((highlight == REG_ATTENDEE) ? "id=\"active\"" : "") + " name=\"buttonRegAttendee\" title=\"Attendee Registration Page (Alt + 6)\">Attendee Registration</button></form>" +
            "<form action=\"index\" method=\"get\"><button title=\"Return To Homepage (Alt + 7)\"" + ((highlight == INDEX) ? "id=\"active\"" : "") + ">Home</button></form>" +
            "</span></div>");
    }
    
    public void navigationMenuManage(PrintWriter out, int highlight) { 
        out.println("<div class=\"navigation\"><span>" +
                "<form action=\"show_schedule\" method=\"get\"><button name=\"buttonEventSchedule\" title=\"Event Schedule (Alt + 3)\">Event Schedule</button></form>" +
                "<form action=\"manage_speakers\" method=\"get\"><button " + ((highlight == SHOW_SPEAKERS) ? "id=\"active\"" : "") + " name=\"buttonSpeaker\" title=\"Add Speaker Details (Alt + h)\">Manage Speakers</button></form>" +
                "<form action=\"manage_workshops\" method=\"get\"><button " + ((highlight == SHOW_WORKSHOPS) ? "id=\"active\"" : "") + " name=\"buttonWorkshop\" title=\"Add Workshop Details (Alt + j)\">Manage Workshops</button></form>" +
                "<form action=\"manage_schedule\" method=\"get\"><button " + ((highlight == SHOW_SCHEDULE) ? "id=\"active\"" : "") + " name=\"buttonSchedule\" title=\"Add Schedule Details (Alt + k)\">Manage Schedule</button></form>" +
                "<form action=\"manage_exhibitors\" method=\"get\"><button " + ((highlight == SHOW_EXHIBITORS) ? "id=\"active\"" : "") + " name=\"buttonExhibitor\" title=\"Add Exhibitor Details (Alt + l)\">Manage Exhibitors</button></form>" +
                "<form action=\"index\" method=\"get\"><button name=\"buttonHome\" title=\"Return To Homepage (Alt + 7)\">Home</button></form>" +
            "</span></div>");
    }
    
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
    
    // Bottom Links Manage
    //public void bottomMenuManage(HttpServletRequest request, PrintWriter out) {
    public void bottomMenuManage(PrintWriter out) {
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
}
