package org.apache.jsp;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.jsp.*;
import webstore.model.IConstants;
import java.util.*;
import database.model.User;
import database.model.Role;
import database.model.Region;
import database.model.RolesManager;
import database.model.RegionsManager;
import webstore.model.SessionUser;
import webstore.utility.CustomerType;
import webstore.security.LoggedUsers;

public final class admin_jsp extends org.apache.jasper.runtime.HttpJspBase implements org.apache.jasper.runtime.JspSourceDependent {

    private static final JspFactory _jspxFactory = JspFactory.getDefaultFactory();

    private static java.util.List _jspx_dependants;

    private javax.el.ExpressionFactory _el_expressionfactory;

    private org.apache.AnnotationProcessor _jsp_annotationprocessor;

    public Object getDependants() {
        return _jspx_dependants;
    }

    public void _jspInit() {
        _el_expressionfactory = _jspxFactory.getJspApplicationContext(getServletConfig().getServletContext()).getExpressionFactory();
        _jsp_annotationprocessor = (org.apache.AnnotationProcessor) getServletConfig().getServletContext().getAttribute(org.apache.AnnotationProcessor.class.getName());
    }

    public void _jspDestroy() {
    }

    public void _jspService(HttpServletRequest request, HttpServletResponse response) throws java.io.IOException, ServletException {
        PageContext pageContext = null;
        HttpSession session = null;
        ServletContext application = null;
        ServletConfig config = null;
        JspWriter out = null;
        Object page = this;
        JspWriter _jspx_out = null;
        PageContext _jspx_page_context = null;
        try {
            response.setContentType("text/html");
            pageContext = _jspxFactory.getPageContext(this, request, response, null, true, 8192, true);
            _jspx_page_context = pageContext;
            application = pageContext.getServletContext();
            config = pageContext.getServletConfig();
            session = pageContext.getSession();
            out = pageContext.getOut();
            _jspx_out = out;
            out.write("<!DOCTYPE HTML PUBLIC \"-//W3C//DTD HTML 4.01 Transitional//EN\" \"http://www.w3.org/TR/html4/loose.dtd\">\r\n");
            out.write("\r\n");
            out.write("\r\n");
            out.write("\r\n");
            out.write("\r\n");
            out.write("\r\n");
            out.write("\r\n");
            out.write("\r\n");
            out.write("\r\n");
            out.write("\r\n");
            out.write("\r\n");
            out.write("\r\n");
            out.write("\r\n");
            out.write("<html>\r\n");
            out.write("<head>\r\n");
            out.write("<meta http-equiv=\"X-UA-Compatible\" content=\"IE=EmulateIE8\">\r\n");
            out.write("<meta http-equiv=\"Content-Type\" content=\"text/html; charset=ISO-8859-1\">\r\n");
            out.write("<title>Order Management System, Release 1.0, v. 1.0</title>\r\n");
            out.write("<style type=\"text/css\">\r\n");
            out.write("html, body, form, fieldset, h1, h2, h3, h4, h5, h6, p, pre, select, \r\n");
            out.write("        input, blockquote, ul, ol, dl, address {       \r\n");
            out.write("    font: normal 11px auto \"Trebuchet MS\", Verdana, Arial, Helvetica, sans-serif;\r\n");
            out.write("    color:#000080;\r\n");
            out.write("    text-align:left;   \r\n");
            out.write("    align:left;\r\n");
            out.write("    background:white;\r\n");
            out.write("    }\r\n");
            out.write("body {    background:#E6E6FA;}\r\n");
            out.write("p { margin-left:10px;}\r\n");
            out.write("h1 {    font-size:14px;    margin-left:10px;}\r\n");
            out.write("h2 {    font-size:10px;    font-color:#FF0000;    font-weight:bold;} \r\n");
            out.write("input {    background:#FFFFFF;}\r\n");
            out.write("a:link {\r\n");
            out.write("    color:#FF0000;\r\n");
            out.write("    text-decoration:none;\r\n");
            out.write("    border-bottom:1px solid #FF0000;  \r\n");
            out.write("    } \r\n");
            out.write("a:visited {\r\n");
            out.write("    color:#d42945;\r\n");
            out.write("    border-bottom:none;\r\n");
            out.write("    text-decoration:none;\r\n");
            out.write("    }       \r\n");
            out.write("a:hover,\r\n");
            out.write("a:focus {\r\n");
            out.write("    color:#f03b58;\r\n");
            out.write("    border-bottom:1px solid #f03b58;\r\n");
            out.write("    text-decoration:none;\r\n");
            out.write("    }\r\n");
            out.write("table a,\r\n");
            out.write("table a:link,\r\n");
            out.write("table a:visited {\r\n");
            out.write("    border:none;\r\n");
            out.write("    }                           \r\n");
            out.write("    \r\n");
            out.write("img {\r\n");
            out.write("    border:0;\r\n");
            out.write("    margin-top:.5em;\r\n");
            out.write("    }   \r\n");
            out.write("table {\r\n");
            out.write("    width:100%;\r\n");
            out.write("    align:left;\r\n");
            out.write("    text-align:left;    \r\n");
            out.write("    border-collapse:collapse;\r\n");
            out.write("    }\r\n");
            out.write("caption {\r\n");
            out.write("    color: #9ba9b4;\r\n");
            out.write("    font-size:.94em;\r\n");
            out.write("        letter-spacing:.1em;\r\n");
            out.write("        margin:1em 0 0 0;\r\n");
            out.write("        padding:0;\r\n");
            out.write("        caption-side:top;\r\n");
            out.write("        text-align:center;\r\n");
            out.write("    }   \r\n");
            out.write("tr.odd td   {\r\n");
            out.write("    background:#f7fbff\r\n");
            out.write("    align:left;\r\n");
            out.write("    text-align:left;\r\n");
            out.write("    }\r\n");
            out.write("tr.odd .column1 {\r\n");
            out.write("    background:#f4f9fe;\r\n");
            out.write("    align:left;\r\n");
            out.write("    text-align:left;\r\n");
            out.write("    }   \r\n");
            out.write(".column1    {\r\n");
            out.write("    background:#f9fcfe;\r\n");
            out.write("    align:left;\r\n");
            out.write("    text-align:left;\r\n");
            out.write("    }\r\n");
            out.write("td {\r\n");
            out.write("    align:left;\r\n");
            out.write("    text-align:left;\r\n");
            out.write("    }               \r\n");
            out.write("th {\r\n");
            out.write("    font-weight:normal;\r\n");
            out.write("    text-align:left;\r\n");
            out.write("    padding:.3em 1em;\r\n");
            out.write("    }                           \r\n");
            out.write("thead th {\r\n");
            out.write("    background:#f4f9fe;\r\n");
            out.write("    align:left;\r\n");
            out.write("    text-align:left;\r\n");
            out.write("    }   \r\n");
            out.write("tfoot th {\r\n");
            out.write("    align:left;\r\n");
            out.write("    text-align:left;\r\n");
            out.write("    background:#f4f9fe;\r\n");
            out.write("    }   \r\n");
            out.write("tfoot th strong {\r\n");
            out.write("    margin:.5em .5em .5em 0;\r\n");
            out.write("        }       \r\n");
            out.write("tfoot th em {\r\n");
            out.write("    font-weight: bold;\r\n");
            out.write("    font-size: 1.1em;\r\n");
            out.write("    font-style: normal;\r\n");
            out.write("    }\r\n");
            out.write("}  \r\n");
            out.write("\r\n");
            out.write("#container\r\n");
            out.write("{\r\n");
            out.write("position:relative;\r\n");
            out.write("border-bottom:1px dotted #000000;\r\n");
            out.write("float:right;\r\n");
            out.write("}\r\n");
            out.write("#container span\r\n");
            out.write("{\r\n");
            out.write("display:none;\r\n");
            out.write("}\r\n");
            out.write("#container:hover\r\n");
            out.write("{\r\n");
            out.write("cursor:pointer;\r\n");
            out.write("border:none;\r\n");
            out.write("background:#E6E6FA;\r\n");
            out.write("}\r\n");
            out.write("#container:hover span\r\n");
            out.write("{\r\n");
            out.write("display:block;\r\n");
            out.write("background:#E6E6FA;;\r\n");
            out.write("position:absolute;\r\n");
            out.write("right:20px;\r\n");
            out.write("top:1em;\r\n");
            out.write("padding:5px;\r\n");
            out.write("} \r\n");
            out.write("\r\n");
            out.write("\r\n");
            out.write("\r\n");
            out.write("#newtable {\r\n");
            out.write("    width:100%;\r\n");
            out.write("    border-right:1px solid black;\r\n");
            out.write("    border-top:1px solid black;\r\n");
            out.write("    border-bottom:1px solid black;\r\n");
            out.write("    border-collapse:collapse;\r\n");
            out.write("    \r\n");
            out.write("    }\r\n");
            out.write("#newtable caption {\r\n");
            out.write("    color: #9ba9b4;\r\n");
            out.write("    font-size:.94em;\r\n");
            out.write("        letter-spacing:.1em;\r\n");
            out.write("        caption-side:top;\r\n");
            out.write("        text-align:center;\r\n");
            out.write("    }   \r\n");
            out.write("#newtable tr.odd td   {\r\n");
            out.write("    background:#f7fbff\r\n");
            out.write("    }\r\n");
            out.write("#newtable tr.odd .column1 {\r\n");
            out.write("    background:#f4f9fe;\r\n");
            out.write("    }   \r\n");
            out.write("#newtable .column1    {\r\n");
            out.write("    background:#f9fcfe;\r\n");
            out.write("    }\r\n");
            out.write("#newtable td {\r\n");
            out.write("    color:#678197;\r\n");
            out.write("    border-bottom:1px solid #e5eff8;\r\n");
            out.write("    border-left:1px solid black;\r\n");
            out.write("    text-align:center;\r\n");
            out.write("    }               \r\n");
            out.write("#newtable th {\r\n");
            out.write("    font-weight:normal;\r\n");
            out.write("    color: #678197;\r\n");
            out.write("    text-align:left;\r\n");
            out.write("    border-bottom: 1px solid #e5eff8;\r\n");
            out.write("    border-left:1px solid black;\r\n");
            out.write("    }                           \r\n");
            out.write("#newtable thead th {\r\n");
            out.write("    background:#f4f9fe;\r\n");
            out.write("    text-align:center;\r\n");
            out.write("    color:#0000CD\r\n");
            out.write("    }   \r\n");
            out.write("#newtable tfoot th {\r\n");
            out.write("    text-align:center;\r\n");
            out.write("    background:#f4f9fe;\r\n");
            out.write("    }   \r\n");
            out.write("#newtable tfoot th strong {\r\n");
            out.write("    color:#66a3d3;\r\n");
            out.write("        }       \r\n");
            out.write("#newtable tfoot th em {\r\n");
            out.write("    color:#f03b58;\r\n");
            out.write("    font-weight: bold;\r\n");
            out.write("    font-size: 1.1em;\r\n");
            out.write("    font-style: normal;\r\n");
            out.write("    }\r\n");
            out.write("    \r\n");
            out.write("</style>\r\n");
            out.write("\r\n");
            out.write("\r\n");
            out.write("</head>\r\n");
            out.write("\r\n");
            out.write("<body ");
            if (request.getAttribute("removeflag") != null) {
                out.write("onLoad=\"alert('This user was already deleted from the grid by another administrator. \\nPlease refresh the page to ensure.');\" ");
            }
            out.write(">\r\n");
            out.write("<br>\r\n");
            out.write("<table>\r\n");
            out.write("<tr>\r\n");
            out.write("    <td width=\"40%\"><div style=\"font-size:12px; margin-left:10px;\"><fieldset>\r\n");
            out.write("        Administration</fieldset></div></td>\r\n");
            out.write("    <td width=\"10%\"></td>\r\n");
            out.write("    ");
            SessionUser currentuser = (SessionUser) session.getAttribute(IConstants.USER_SESSION);
            out.write("\r\n");
            out.write("    <td width=\"30%\">Logged User: ");
            out.print(currentuser.getUsername());
            out.write("</td>\r\n");
            out.write("    <form name=\"logoutform\" method=\"post\" action=\"logoutAction.perform\" id=\"logoutform\">\r\n");
            out.write("    <td width=\"10%\" align=\"right\"><input type=\"submit\" id=\"LogOut4\" name=\"LogOut4\" \r\n");
            out.write("            value=\"Logout\" onClick=\"return confirm('Are you sure you want to log out from the application?');\"></td>\r\n");
            out.write("    </form>\r\n");
            out.write(" \r\n");
            out.write("    <!-- Pokazat' vsplivayuschee okno -->\r\n");
            out.write("    \r\n");
            out.write("    <td width=\"10%\" align=\"right\">\r\n");
            out.write("    <div id=\"container\">\r\n");
            out.write("        <fieldset>User Info</fieldset>\r\n");
            out.write("        <span>\r\n");
            out.write("            <fieldset>\r\n");
            out.write("                <b>User Info</b><hr>\r\n");
            out.write("                <table border=\"none\">\r\n");
            out.write("                    <tr>\r\n");
            out.write("                        <td>User Name: </td>\r\n");
            out.write("                        <td>");
            out.print(currentuser.getFL());
            out.write(" </td>\r\n");
            out.write("                    </tr>\r\n");
            out.write("                    <tr>\r\n");
            out.write("                        <td>Role: </td>\r\n");
            out.write("                        <td>");
            out.print(currentuser.getRole());
            out.write("</td>\r\n");
            out.write("                    </tr>\r\n");
            out.write("                     ");
            if (currentuser.getRole().equals("Customer")) {
                out.write(" \r\n");
                out.write("                    <tr>\r\n");
                out.write("                        <td>Customer Type: </td>\r\n");
                out.write("                        <td>");
                out.print(CustomerType.getType(currentuser.getBalance()));
                out.write("</td>\r\n");
                out.write("                    </tr>\r\n");
                out.write("                    <tr>\r\n");
                out.write("                        <td>Account Balance: </td>\r\n");
                out.write("                        <td>");
                out.print(currentuser.getBalance());
                out.write("</td>\r\n");
                out.write("                    </tr>\r\n");
                out.write("                    ");
            }
            out.write("\r\n");
            out.write("                </table>\r\n");
            out.write("                <br>\r\n");
            out.write("                ");
            if (currentuser.getRole().equals("Customer") && CustomerType.getType(currentuser.getBalance()).equals("Platinum")) {
                out.write("\r\n");
                out.write("                Need to spend ");
                out.print(CustomerType.getMoney(currentuser.getBalance()));
                out.write(" more to become a ");
                out.print(CustomerType.getNextType(currentuser.getBalance()));
                out.write(" type of customer\r\n");
                out.write("                ");
            }
            out.write("\r\n");
            out.write("            </fieldset>\r\n");
            out.write("        </span>\r\n");
            out.write("    </div>\r\n");
            out.write("    \r\n");
            out.write("    </td>\r\n");
            out.write("</tr>    \r\n");
            out.write("</table>\r\n");
            out.write("\r\n");
            out.write("<div style=\"margin:10px;\">\r\n");
            out.write("<fieldset>\r\n");
            out.write("This page is appointed for creating new and managing existing users.\r\n");
            out.write("\r\n");
            out.write("<!-- ÃÂÃÂ¾ÃÂ³ÃÂ¾ ÃÂ·ÃÂ´ÃÂµÃÂÃÂ? -->\r\n");
            out.write("\r\n");
            out.write("<h5><a href=\"createNewUserAction.perform\">Create New User</a></h5>\r\n");
            out.write("\r\n");
            out.write("Number of Found Users: ");
            out.print(request.getAttribute("USERSNUMBER"));
            out.write("\r\n");
            out.write("<br>\r\n");
            out.write("<fieldset style=\"margin:0px\">\r\n");
            out.write("    <legend>Search by</legend>\r\n");
            out.write("    <form method=\"post\" action=\"adminpage.perform\">\r\n");
            out.write("    \r\n");
            out.write("        <table width=\"100%\">\r\n");
            out.write("            <tr> \r\n");
            out.write("                <td width=\"20%\">Field Filter: </td>\r\n");
            out.write("                <td width=\"20%\">    \r\n");
            out.write("                    <select name=\"Combobox1\" id=\"Combobox1\">  \r\n");
            out.write("                        <option value=\"all_columns\" ");
            out.print(request.getAttribute("FFall_columns"));
            out.write(">All columns</option>\r\n");
            out.write("                        <option value=\"login_name\" ");
            out.print(request.getAttribute("FFlogin_name"));
            out.write(">User Name</option>\r\n");
            out.write("                        <option value=\"first_name\" ");
            out.print(request.getAttribute("FFfirst_name"));
            out.write(">First Name</option>\r\n");
            out.write("                        <option value=\"last_name\" ");
            out.print(request.getAttribute("FFlast_name"));
            out.write(">Last Name</option>\r\n");
            out.write("                        <option value=\"role\" ");
            out.print(request.getAttribute("FFrole"));
            out.write(">Role</option>                               \r\n");
            out.write("                    </select>                            \r\n");
            out.write("                </td>\r\n");
            out.write("                <td width=\"20%\">\r\n");
            out.write("                <!-- ÃÂÃÂÃÂÃÂÃÂÃÂ¯ÃÂÃÂ name ÃÂ¸ id ÃÂ½ÃÂ° Combobox2, ÃÂ ÃÂ¢ÃÂ ÃÂ¡ ÃÂÃÂ ÃÂÃÂÃÂ«ÃÂÃÂ£ÃÂ©ÃÂÃÂ ÃÂÃÂÃÂÃÂÃÂÃÂÃÂÃÂÃÂ¡ÃÂÃÂ ÃÂÃÂÃÂÃÂÃÂÃÂÃÂÃÂÃÂ«ÃÂ ÃÂÃÂ«ÃÂÃÂ!-->\r\n");
            out.write("                    <select name=\"Combobox2\" id=\"Combobox2\">\r\n");
            out.write("                        <option value=\"equals\" ");
            out.print(request.getAttribute("FTequals"));
            out.write(">equals</option>\r\n");
            out.write("                        <option value=\"not_equals\" ");
            out.print(request.getAttribute("FTnot_equals"));
            out.write(">not equal to</option>\r\n");
            out.write("                        <option value=\"start_with\" ");
            out.print(request.getAttribute("FTstart_with"));
            out.write(">starts with</option>\r\n");
            out.write("                        <option value=\"contains\" ");
            out.print(request.getAttribute("FTcontains"));
            out.write(">contains</option>\r\n");
            out.write("                        <option value=\"not_contains\" ");
            out.print(request.getAttribute("FTnot_contains"));
            out.write(">does not contain</option> \r\n");
            out.write("                    </select>\r\n");
            out.write("                <td width=\"20%\">\r\n");
            out.write("                    <input type=\"text\" size=\"25\" id=\"Editbox1\" name=\"Editbox1\" value=\"");
            out.print(session.getAttribute("filterString"));
            out.write("\">\r\n");
            out.write("                </td>            \r\n");
            out.write("                <td width=\"20%\"><div style=\"text-align:right;\">\r\n");
            out.write("                <input type=\"submit\" name=\"Button3\" id=\"Button3\" value=\"Search\"> \r\n");
            out.write("                </div>\r\n");
            out.write("                </td>\r\n");
            out.write("            </tr>\r\n");
            out.write("        </table>\r\n");
            out.write("     </form> \r\n");
            out.write("</fieldset>\r\n");
            out.write("\r\n");
            out.write("<br>\r\n");
            out.write("<table width=\"100%\">\r\n");
            out.write("        <tr>\r\n");
            out.write("            <td width=\"100%\"><div style=\"text-align:right;\">\r\n");
            out.write("            ");
            if ((Integer) session.getAttribute("pagecount") == 10) {
                out.write("\r\n");
                out.write("            <a href=\"adminpage.perform?pagecount=25\" >Show 25 lines </a>\r\n");
                out.write("            ");
            } else {
                out.write("\r\n");
                out.write("            <a href=\"adminpage.perform?pagecount=10\" >Show 10 lines </a>\r\n");
                out.write("            ");
            }
            out.write("\r\n");
            out.write("              \r\n");
            out.write("            </div>\r\n");
            out.write("            </td>\r\n");
            out.write("         </tr>\r\n");
            out.write("    </table>\r\n");
            out.write("    \r\n");
            out.write("<br>\r\n");
            out.write("\r\n");
            out.write("<div id=\"newtable\" style=\"margin-right:10px;\">\r\n");
            out.write("<!-- <form method=\"post\" action=\"editOrder.perform\">   -->\r\n");
            out.write("<table style=\"margin-right:10px;\">\r\n");
            out.write("<thead>\r\n");
            out.write("    <tr class=\"odd\">\r\n");
            out.write("        <th align=\"left\" scope=\"col\"><a href=\"adminpage.perform?sortField=login_name\" >User Name</a></th>\r\n");
            out.write("        <th align=\"left\" scope=\"col\"><a href=\"adminpage.perform?sortField=first_name\" >First Name</a></th>\r\n");
            out.write("        <th align=\"left\" scope=\"col\"><a href=\"adminpage.perform?sortField=last_name\" >Last Name</a></th>\r\n");
            out.write("        <th align=\"left\" scope=\"col\"><a href=\"adminpage.perform?sortField=role_name\" >Role</a></th>\r\n");
            out.write("        <th align=\"left\" scope=\"col\"><a href=\"adminpage.perform?sortField=email\" >Email</a></th>\r\n");
            out.write("        <th align=\"left\" scope=\"col\"><a href=\"adminpage.perform?sortField=region_name\" >Region</a></th>\r\n");
            out.write("        <th align=\"left\" scope=\"col\">Edit</th>\r\n");
            out.write("        <th align=\"left\" scope=\"col\">Delete</th>\r\n");
            out.write("        <th align=\"left\" scope=\"col\">Duplicate</th>\r\n");
            out.write("    </tr>   \r\n");
            out.write("</thead>\r\n");
            out.write("<tbody>\r\n");
            out.write("\r\n");
            out.write("<!-- ÃÂÃÂ ÃÂÃÂÃÂÃÂÃÂÃÂÃÂÃÂ:-->\r\n");
            out.write("        ");
            RegionsManager regman = new RegionsManager();
            RolesManager roleman = new RolesManager();
            List<User> list = (List) request.getAttribute("USERSLIST");
            if (list != null) {
                int c = 0;
                for (Iterator i = list.iterator(); i.hasNext(); ) {
                    c++;
                    User user = (User) i.next();
                    out.write(" \r\n");
                    out.write("\r\n");
                    out.write("         <!-- ÃÂÃÂÃÂÃÂ°ÃÂ²ÃÂ¸ÃÂÃÂ ÃÂ² ÃÂÃÂ¸ÃÂºÃÂ»ÃÂµ ÃÂ²ÃÂÃÂµ ÃÂ·ÃÂ°ÃÂºÃÂ°ÃÂ·ÃÂ, ÃÂºÃÂ¾ÃÂÃÂ¾ÃÂÃÂÃÂµ ÃÂÃÂ¾ÃÂ·ÃÂ´ÃÂ°ÃÂ½ÃÂ -->\r\n");
                    out.write("    <tr ");
                    if (c % 2 == 0) {
                        out.write("class=\"odd\" ");
                    }
                    out.write(" >\r\n");
                    out.write("        <td>");
                    out.print(user.getLoginName());
                    out.write("</td>\r\n");
                    out.write("        <td>");
                    out.print(user.getFirstName());
                    out.write("</td>\r\n");
                    out.write("        <td>");
                    out.print(user.getLastName());
                    out.write("</td>\r\n");
                    out.write("        <td>");
                    out.print(roleman.getRole(user.getRole()).getName());
                    out.write("</td>\r\n");
                    out.write("        <td>");
                    out.print(user.getEmail());
                    out.write("</td>\r\n");
                    out.write("        <td>");
                    out.print(regman.getRegion(user.getRegion()).getName());
                    out.write("</td>        \r\n");
                    out.write("\t\t\r\n");
                    out.write("\t\t<form name=\"editform\" method=\"post\" action=\"editUserAction.perform?id=");
                    out.print(user.getId());
                    out.write("\" id=\"editform\">\r\n");
                    out.write("        <td width=\"100\"><input type=\"image\" src=\"./Admin_files/icon-edit.gif\" width=\"16\" height=\"16\" id=\"edit1\" name=\"edit1\"></td>\r\n");
                    out.write("\t\t</form>\r\n");
                    out.write("\t\t\r\n");
                    out.write("\t\t<form name=\"removeform\" method=\"post\" action=\"removeUserAction.perform?id=");
                    out.print(user.getId());
                    out.write("\" id=\"removeform\">\r\n");
                    out.write("        <td width=\"100\"><input class=\"dis\" type=\"image\" width=\"16\" height=\"16\" id=\"remove1\" name=\"remove1\" \r\n");
                    out.write("        \t");
                    if (LoggedUsers.isLogged(user.getLoginName())) {
                        out.write("src=\"./Admin_files/icon-remove-dis.gif\" disabled");
                    } else {
                        out.write("src=\"./Admin_files/icon-remove.gif\"");
                    }
                    out.write("\r\n");
                    out.write("            onClick=\"return confirm('Are you sure you want delete User?');\"\r\n");
                    out.write("        ></td>\r\n");
                    out.write("\t\t</form>\r\n");
                    out.write("\t\t\r\n");
                    out.write("\t\t<form name=\"duplicateform\" method=\"post\" action=\"duplicateUserAction.perform?id=");
                    out.print(user.getId());
                    out.write("\" id=\"duplicateform\">\r\n");
                    out.write("        <td width=\"100\"><input type=\"image\" src=\"./Admin_files/icon-duplicate.gif\" width=\"16\" height=\"16\" id=\"duplicate1\" name=\"duplicate1\"></td>\r\n");
                    out.write("\t\t</form>\r\n");
                    out.write("\t\t\r\n");
                    out.write("        \r\n");
                    out.write("    </tr>   \r\n");
                    out.write("  \r\n");
                    out.write("   \r\n");
                    out.write("        ");
                }
            }
            out.write("  \r\n");
            out.write("    \r\n");
            out.write("\r\n");
            out.write("      \r\n");
            out.write("</table>\r\n");
            out.write("<!-- </form> -->\r\n");
            out.write("</div>\r\n");
            out.write("<br>\r\n");
            out.write("<!-- ÃÂ¡ÃÂ®ÃÂÃÂ ÃÂÃÂÃÂÃÂÃÂÃÂÃÂ¢ÃÂ¬ ÃÂÃÂÃÂÃÂÃÂÃÂ§ÃÂÃÂ FIRST-LAST... -->\r\n");
            out.write("<!-- If only one page of orders is available, pugination buttons \r\n");
            out.write("         should be disabled. In case two (or more) pages 'Forward' and 'Last'\r\n");
            out.write("         should be enabled when user on the first page, and 'First' and \r\n");
            out.write("         'Backward' should be enabled if user on the second page. -->\r\n");
            out.write("    \r\n");
            out.write("    <!-- ?????????????ÐÑÐµ Ð½ÐµÐ¸Ð·Ð²ÐµÑÑÐ½Ð¾, ÐºÐ°Ðº ÑÑÐ¾ ÑÐ´ÐµÐ»Ð°ÑÑ????????????? -->\r\n");
            out.write("  \r\n");
            out.write("    <!-- ÐÑÐ¾Ð±ÑÐ°Ð¶Ð°ÐµÐ¼ ÑÐ°Ð±Ð»Ð¸ÑÑ --> \r\n");
            out.write("   \r\n");
            out.write("    <table>\r\n");
            out.write("        <tr> \r\n");
            out.write("        <td width=\"30%\">           \r\n");
            out.write("        \r\n");
            out.write("            Page #: ");
            out.print(session.getAttribute("currentpage"));
            out.write("\r\n");
            out.write("            from ");
            out.print(request.getAttribute("PAGENUMBER"));
            out.write("      \r\n");
            out.write("        </td>\r\n");
            out.write("        \r\n");
            out.write("        \t<form method=\"post\" action=\"adminpage.perform\">\r\n");
            out.write("        \t\t\t<td width=\"10\">  \r\n");
            out.write("                        <input type=\"submit\" id=\"changepages\" name=\"changepages\" value=\" First  \" ");
            out.print(request.getAttribute("FIRST"));
            out.write(">\r\n");
            out.write("                    </td>\r\n");
            out.write("                    <td width=\"10%\">  \r\n");
            out.write("                        <input type=\"submit\" id=\"changepages\" name=\"changepages\" value=\"Backward\" ");
            out.print(request.getAttribute("BACKWARD"));
            out.write(">\r\n");
            out.write("                    </td>\r\n");
            out.write("        \r\n");
            out.write("              \t\t<td width=\"10%\">  \r\n");
            out.write("                        <input type=\"submit\" id=\"changepages\" name=\"changepages\" value=\"Forward \" ");
            out.print(request.getAttribute("FORWARD"));
            out.write(">\r\n");
            out.write("               \t\t</td>\r\n");
            out.write("             \t\t<td width=\"10%\">  \r\n");
            out.write("                        <input type=\"submit\" id=\"changepages\" name=\"changepages\" value=\"  Last  \" ");
            out.print(request.getAttribute("LAST"));
            out.write(">\r\n");
            out.write("                \t</td>\r\n");
            out.write("          \t\t  \t<td width=\"30%\"></td>\r\n");
            out.write("          \t</form>\r\n");
            out.write("        </tr>\r\n");
            out.write("    </table>\r\n");
            out.write("\r\n");
            out.write("<br>\r\n");
            out.write("\r\n");
            out.write("<br>\r\n");
            out.write("</fieldset>\r\n");
            out.write("</div>\r\n");
            out.write("<br>\r\n");
            out.write("</body>\r\n");
            out.write("</html>");
        } catch (Throwable t) {
            if (!(t instanceof SkipPageException)) {
                out = _jspx_out;
                if (out != null && out.getBufferSize() != 0) try {
                    out.clearBuffer();
                } catch (java.io.IOException e) {
                }
                if (_jspx_page_context != null) _jspx_page_context.handlePageException(t);
            }
        } finally {
            _jspxFactory.releasePageContext(_jspx_page_context);
        }
    }
}
