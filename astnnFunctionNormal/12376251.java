class BackupThread extends Thread {
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
            response.setContentType("text/html; charset=utf-8");
            pageContext = _jspxFactory.getPageContext(this, request, response, "", true, 8192, true);
            _jspx_page_context = pageContext;
            application = pageContext.getServletContext();
            config = pageContext.getServletConfig();
            session = pageContext.getSession();
            out = pageContext.getOut();
            _jspx_out = out;
            out.write('\r');
            out.write('\n');
            out.write("\r\n");
            out.write("\r\n");
            out.write("\r\n");
            out.write("\r\n");
            out.write("\r\n");
            out.write("\r\n");
            out.write("\r\n");
            out.write("\r\n");
            response.setHeader("Pragma", "No-cache");
            response.setHeader("Cache-Control", "no-cache");
            response.setDateHeader("Expires", 0);
            out.write("\r\n");
            out.write("<html>\r\n");
            out.write("<head>\r\n");
            out.write("<meta http-equiv=\"Content-Type\" content=\"text/html; charset=utf-8\" />\r\n");
            out.write("<title>中山市美斯特实业有限公司</title>\r\n");
            out.write("<link type=\"text/css\" rel=\"stylesheet\" href=\"/system/css/style.css\" />\r\n");
            out.write("<script type=\"text/javascript\" language=\"javascript\" src=\"/system/js/public.js\"></script>\r\n");
            out.write("<script type=\"text/javascript\" src=\"../js/jquery-1.4.2.js\"></script>\r\n");
            out.write("<script type=\"text/javascript\" src=\"../js/validate.js\"></script>\r\n");
            out.write("\t\t<script type=\"text/javascript\">\t\r\n");
            out.write("\t\t//验证按钮名是否为空\r\n");
            out.write("\t\t$(document).ready(function(){\r\n");
            out.write("\t\t\t\tvalidateIsNull(\"#buttonName\",\"[按钮名不能为空！]\");\t\t\t\t\r\n");
            out.write("\t\t\t\tvalidateIsNull(\"#actionKey\",\"[actionKey不能为空！]\");\r\n");
            out.write("\t\t\t\t$(\"#actionKey\").blur(function(){\r\n");
            out.write("\t\t\t\t  \t\tvar actionKey=$(\"#actionKey\").val();//动态设置json传递参数，以及相应的形参。。。。。。\r\n");
            out.write("\t\t\t\t  \t\tajaxRequest_validate(\"#actionKey\",\"/system/ajax/button.do?method=validate\",{\"actionKey\":actionKey});\r\n");
            out.write("\t\t\t\t});\r\n");
            out.write("\t\t\t\treturn false;\t\r\n");
            out.write("\t\t\t\t\t\t\t\t\r\n");
            out.write("\t\t});\r\n");
            out.write("\r\n");
            out.write("\r\n");
            out.write("\t\t</script>\r\n");
            out.write("\t\r\n");
            out.write("\r\n");
            out.write("\r\n");
            out.write("</head>\r\n");
            out.write("<body>\r\n");
            out.write("<div class=\"main\">\r\n");
            out.write("\t<div class=\"position\">当前位置: <a href=\"/sysadm/desktop.jsp\">桌 面</a> → 添加菜单</div>\r\n");
            out.write("\t<div class=\"mainbody\">\r\n");
            out.write("\t\t<div class=\"operate_info\">操作说明：带 * 号必填</div>\r\n");
            out.write("\t\t<div class=\"table\">\r\n");
            out.write("\t\t<form action=\"/system/config/button.do?method=save\" method=\"post\" >\r\n");
            out.write("\t\t\t<table width=\"100%\" border=\"0\" cellpadding=\"1\" cellspacing=\"1\" class=\"table_form\">\r\n");
            out.write("\t\t\t\t\t<tr onMouseOver=\"mouseOver(this)\" onMouseOut=\"mouseOut(this)\">\r\n");
            out.write("\t\t\t\t\t\t<td align=\"right\">按钮名称：</td>\t\t\t\t\t\r\n");
            out.write("\t\t\t\t\t\t<td><input name=\"buttonName\" id=\"buttonName\"/>　*必填 \r\n");
            out.write("\t\t\t\t\t\t </td>\r\n");
            out.write("\t\t\t\t\t</tr>\r\n");
            out.write("\t\t\t\t\t<tr onMouseOver=\"mouseOver(this)\" onMouseOut=\"mouseOut(this)\">\r\n");
            out.write("\t\t\t\t\t\t<td align=\"right\">操作键：</td>\r\n");
            out.write("\t\t\t\t\t\t<td><input name=\"actionKey\" id=\"actionKey\" />　*必填 例如：adduser </td>\r\n");
            out.write("\t\t\t\t\t</tr>\r\n");
            out.write("\t\t\t\t\t<tr onMouseOver=\"mouseOver(this)\" onMouseOut=\"mouseOut(this)\">\r\n");
            out.write("\t\t\t\t\t\t<td align=\"right\">允许操作的路径：</td>\r\n");
            out.write("\t\t\t\t\t\t<td>\r\n");
            out.write("\t\t\t\t\t\t\t<input name=\"actionUrl\" id=\"actionUrl\" size=\"75\" />\r\n");
            out.write("\t\t\t\t\t\t\t<br>允许具有该权限的角色用户的操作路径,多个路径请用逗号\",\"隔开,例如/system/test.jsp,/system/test.do\r\n");
            out.write("\t\t\t\t\t\t</td>\r\n");
            out.write("\t\t\t\t\t</tr>\r\n");
            out.write("\t\t\t\t\t<tr onMouseOver=\"mouseOver(this)\" onMouseOut=\"mouseOut(this)\">\r\n");
            out.write("\t\t\t\t\t\t<td align=\"right\">按钮相关说明：</td>\r\n");
            out.write("\t\t\t\t\t\t<td><textarea rows=\"3\" cols=\"20\" name=\"buttonNote\" ></textarea>　非必填  </td>\r\n");
            out.write("\t\t\t\t\t</tr>\r\n");
            out.write("\t\t\t\t\t<tr>\r\n");
            out.write("\t\t\t\t\t\t<td colspan=\"2\" class=\"form_button\" style=\"padding-top:10px;\">\r\n");
            out.write("\t\t\t\t\t\t\t<input type=\"submit\" value=\"保存\" id=\"save\"/>\r\n");
            out.write("\t\t\t\t\t\t\t<input type=\"reset\" value=\"重置\" />\r\n");
            out.write("\t\t\t\t\t\t</td>\r\n");
            out.write("\t\t\t\t\t</tr>\r\n");
            out.write("\t\t\t</table>\r\n");
            out.write("\t\t\t</form>\r\n");
            out.write("\t\t</div>\r\n");
            out.write("\t</div>\r\n");
            out.write("</div>\r\n");
            out.write("</body>\r\n");
            out.write("</html>\r\n");
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
