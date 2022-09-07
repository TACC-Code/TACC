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
        java.lang.Object _jspx_menu1_1 = null;
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
            String path = request.getContextPath();
            String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + path + "/";
            pageContext.setAttribute("basePath", basePath);
            out.write("\r\n");
            out.write("<html>\r\n");
            out.write("<head>\r\n");
            out.write("<base href=\"");
            out.write((java.lang.String) org.apache.jasper.runtime.PageContextImpl.proprietaryEvaluate("${pageScope.basePath }", java.lang.String.class, (PageContext) _jspx_page_context, null, false));
            out.write("\">\r\n");
            out.write("<meta http-equiv=\"Content-Type\" content=\"text/html; charset=utf-8\" />\r\n");
            out.write("<title>中山市美斯特实业有限公司</title>\r\n");
            out.write("<link type=\"text/css\" rel=\"stylesheet\" href=\"system/css/style.css\" />\r\n");
            out.write("<script type=\"text/javascript\" language=\"javascript\" src=\"system/js/public.js\"></script>\r\n");
            out.write("<script type=\"text/javascript\" src=\"system/js/jquery-1.4.2.js\"></script>\r\n");
            out.write("<script type=\"text/javascript\" src=\"system/js/validate.js\"></script>\r\n");
            out.write("<script type=\"text/javascript\">\r\n");
            out.write("\t$(document).ready(function(){\t\t\t\r\n");
            out.write("\t\t\t\tvalidate_Effect(\"#menuName\",\"[菜单名不能为空！]\");\r\n");
            out.write("\t\t\t\tvalidateNumber_Effect(\"#menuSort\",\"排序非法，要求是正整数!\");\t\r\n");
            out.write("\t\t\t\tvalidate_Effect(\"#actionKey\",\"[操作键不能为空！]\");\t\t\t\t\t\t\r\n");
            out.write("\t\t\t//通过ajax，为指定的控件验证其值是否存在。并将返回的信息放到控件后。。。。\r\n");
            out.write("\t\t\t$(\"#actionKey\").blur(function(){\r\n");
            out.write("\t\t\t  \t\tvar actionKey=$(\"#actionKey\").val();//动态设置json传递参数，以及相应的形参。。。。。。\r\n");
            out.write("\t\t\t  \t\tajaxRequest_validate(\"#actionKey\",\"system/ajax/menu.do?method=validate\",{\"actionKey\":actionKey});\r\n");
            out.write("\t\t\t});\t\r\n");
            out.write("\r\n");
            out.write("});\t\r\n");
            out.write("\r\n");
            out.write("\r\n");
            out.write("function checkForm(){\t\r\n");
            out.write("\tvar menuNameIsNull=validateValueIsNull(\"#menuName\");\r\n");
            out.write("\tif(menuNameIsNull){\r\n");
            out.write("\t\treturn false;\r\n");
            out.write("\t}\r\n");
            out.write("\tvar menuSortIsRight=validateNumberIsRight(\"#menuSort\");\r\n");
            out.write("\tif(!menuSortIsRight){\r\n");
            out.write("\t\treturn false;\r\n");
            out.write("\t}\r\n");
            out.write("\tvar actionKeyIsNull=validateValueIsNull(\"#actionKey\");\r\n");
            out.write("\tif(actionKeyIsNull){\r\n");
            out.write("\t\treturn false;\r\n");
            out.write("\t}\t\r\n");
            out.write("\telse{\r\n");
            out.write("\t\treturn true;\r\n");
            out.write("\t}\r\n");
            out.write("}\r\n");
            out.write("</script>\r\n");
            out.write("</head>\r\n");
            out.write("<body>\r\n");
            out.write("<div class=\"main\">\r\n");
            out.write("\t<div class=\"position\">当前位置: <a href=\"sysadm/desktop.jsp\">桌 面</a> → 添加菜单</div>\r\n");
            out.write("\t<div class=\"mainbody\">\r\n");
            out.write("\t\t<div class=\"operate_info\">操作说明：带 * 号必填</div>\r\n");
            out.write("\t\t<div class=\"table\">\r\n");
            out.write("\t\t<form action=\"system/config/menu_add.do\" method=\"post\" onsubmit=\"return checkForm()\">\r\n");
            out.write("\t\t\t<table width=\"100%\" border=\"0\" cellpadding=\"1\" cellspacing=\"1\" class=\"table_form\">\r\n");
            out.write("\t\t\t\t\t<tr onMouseOver=\"mouseOver(this)\" onMouseOut=\"mouseOut(this)\">\r\n");
            out.write("\t\t\t\t\t\t<td align=\"right\">菜单名称：</td>\r\n");
            out.write("\t\t\t\t\t\t<td><input name=\"menuName\" id=\"menuName\" />　*必填 如：测试 </td>\r\n");
            out.write("\t\t\t\t\t</tr>\r\n");
            out.write("\t\t\t\t\t<tr onMouseOver=\"mouseOver(this)\" onMouseOut=\"mouseOut(this)\">\r\n");
            out.write("\t\t\t\t\t\t<td align=\"right\">上级菜单：</td>\r\n");
            out.write("\t\t\t\t\t\t<td>\r\n");
            out.write("\t\t\t\t\t\t\t\t\t<select name=\"menuParent\">\t\r\n");
            out.write("\t\t\t\t\t\t\t\t\t <option value =\"0\" selected=\"selected\">无</option>\t\t \r\n");
            out.write("\t\t\t\t\t\t\t\t\t\t");
            if (_jspx_meth_mytag_005fList_005f0(_jspx_page_context)) return;
            out.write("\r\n");
            out.write("\t\t\t\t\t\t\t\t\t\t ");
            org.apache.struts.taglib.logic.IterateTag _jspx_th_logic_005fiterate_005f0 = (org.apache.struts.taglib.logic.IterateTag) _005fjspx_005ftagPool_005flogic_005fiterate_0026_005fname_005fid.get(org.apache.struts.taglib.logic.IterateTag.class);
            _jspx_th_logic_005fiterate_005f0.setPageContext(_jspx_page_context);
            _jspx_th_logic_005fiterate_005f0.setParent(null);
            _jspx_th_logic_005fiterate_005f0.setId("menu0");
            _jspx_th_logic_005fiterate_005f0.setName("menuList");
            int _jspx_eval_logic_005fiterate_005f0 = _jspx_th_logic_005fiterate_005f0.doStartTag();
            if (_jspx_eval_logic_005fiterate_005f0 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
                java.lang.Object menu0 = null;
                if (_jspx_eval_logic_005fiterate_005f0 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE) {
                    out = _jspx_page_context.pushBody();
                    _jspx_th_logic_005fiterate_005f0.setBodyContent((javax.servlet.jsp.tagext.BodyContent) out);
                    _jspx_th_logic_005fiterate_005f0.doInitBody();
                }
                menu0 = (java.lang.Object) _jspx_page_context.findAttribute("menu0");
                do {
                    out.write("\r\n");
                    out.write("\t\t\t\t\t\t\t\t\t\t \t<option value =\"");
                    out.write((java.lang.String) org.apache.jasper.runtime.PageContextImpl.proprietaryEvaluate("${menu0.menuId}", java.lang.String.class, (PageContext) _jspx_page_context, null, false));
                    out.write('"');
                    out.write('>');
                    out.write((java.lang.String) org.apache.jasper.runtime.PageContextImpl.proprietaryEvaluate("${menu0.menuName}", java.lang.String.class, (PageContext) _jspx_page_context, null, false));
                    out.write("</option>\r\n");
                    out.write("\t\t\t\t\t\t\t\t\t\t          ");
                    if (_jspx_meth_mytag_005fList_005f1(_jspx_th_logic_005fiterate_005f0, _jspx_page_context)) return;
                    out.write("\r\n");
                    out.write("\t\t\t\t\t\t\t\t\t\t\t\t  ");
                    org.apache.struts.taglib.logic.IterateTag _jspx_th_logic_005fiterate_005f1 = (org.apache.struts.taglib.logic.IterateTag) _005fjspx_005ftagPool_005flogic_005fiterate_0026_005fname_005fid.get(org.apache.struts.taglib.logic.IterateTag.class);
                    _jspx_th_logic_005fiterate_005f1.setPageContext(_jspx_page_context);
                    _jspx_th_logic_005fiterate_005f1.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_logic_005fiterate_005f0);
                    _jspx_th_logic_005fiterate_005f1.setId("menu1");
                    _jspx_th_logic_005fiterate_005f1.setName("menuList");
                    int _jspx_eval_logic_005fiterate_005f1 = _jspx_th_logic_005fiterate_005f1.doStartTag();
                    if (_jspx_eval_logic_005fiterate_005f1 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
                        java.lang.Object menu1 = null;
                        if (_jspx_eval_logic_005fiterate_005f1 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE) {
                            out = _jspx_page_context.pushBody();
                            _jspx_th_logic_005fiterate_005f1.setBodyContent((javax.servlet.jsp.tagext.BodyContent) out);
                            _jspx_th_logic_005fiterate_005f1.doInitBody();
                        }
                        menu1 = (java.lang.Object) _jspx_page_context.findAttribute("menu1");
                        do {
                            out.write("\r\n");
                            out.write("\t\t\t\t\t\t\t\t\t\t\t\t    <option value =\"");
                            out.write((java.lang.String) org.apache.jasper.runtime.PageContextImpl.proprietaryEvaluate("${menu1.menuId}", java.lang.String.class, (PageContext) _jspx_page_context, null, false));
                            out.write('"');
                            out.write('>');
                            out.write('┗');
                            out.write((java.lang.String) org.apache.jasper.runtime.PageContextImpl.proprietaryEvaluate("${menu1.menuName}", java.lang.String.class, (PageContext) _jspx_page_context, null, false));
                            out.write("</option>\r\n");
                            out.write("\t\t\t\t\t\t\t\t\t\t\t\t   ");
                            int evalDoAfterBody = _jspx_th_logic_005fiterate_005f1.doAfterBody();
                            menu1 = (java.lang.Object) _jspx_page_context.findAttribute("menu1");
                            if (evalDoAfterBody != javax.servlet.jsp.tagext.BodyTag.EVAL_BODY_AGAIN) break;
                        } while (true);
                        if (_jspx_eval_logic_005fiterate_005f1 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE) {
                            out = _jspx_page_context.popBody();
                        }
                    }
                    if (_jspx_th_logic_005fiterate_005f1.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
                        _005fjspx_005ftagPool_005flogic_005fiterate_0026_005fname_005fid.reuse(_jspx_th_logic_005fiterate_005f1);
                        return;
                    }
                    _005fjspx_005ftagPool_005flogic_005fiterate_0026_005fname_005fid.reuse(_jspx_th_logic_005fiterate_005f1);
                    out.write("\r\n");
                    out.write("\t\t\t\t\t\t\t\t\t\t   ");
                    int evalDoAfterBody = _jspx_th_logic_005fiterate_005f0.doAfterBody();
                    menu0 = (java.lang.Object) _jspx_page_context.findAttribute("menu0");
                    if (evalDoAfterBody != javax.servlet.jsp.tagext.BodyTag.EVAL_BODY_AGAIN) break;
                } while (true);
                if (_jspx_eval_logic_005fiterate_005f0 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE) {
                    out = _jspx_page_context.popBody();
                }
            }
            if (_jspx_th_logic_005fiterate_005f0.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
                _005fjspx_005ftagPool_005flogic_005fiterate_0026_005fname_005fid.reuse(_jspx_th_logic_005fiterate_005f0);
                return;
            }
            _005fjspx_005ftagPool_005flogic_005fiterate_0026_005fname_005fid.reuse(_jspx_th_logic_005fiterate_005f0);
            out.write("\r\n");
            out.write("\t\t\t\t\t\t\t\t\t\t</select>\r\n");
            out.write("\t\t\t\t\t\t</td>\r\n");
            out.write("\t\t\t\t\t</tr>\r\n");
            out.write("\t\t\t\t\t<tr onMouseOver=\"mouseOver(this)\" onMouseOut=\"mouseOut(this)\">\r\n");
            out.write("\t\t\t\t\t\t<td align=\"right\">排序：</td>\r\n");
            out.write("\t\t\t\t\t\t<td><input name=\"save_menuSort\" value=\"0\" size=\"5\" id=\"menuSort\" />　*必填 默认 0 </td>\r\n");
            out.write("\t\t\t\t\t</tr>\r\n");
            out.write("\t\t\t\t\t<tr onMouseOver=\"mouseOver(this)\" onMouseOut=\"mouseOut(this)\">\r\n");
            out.write("\t\t\t\t\t\t<td align=\"right\">链接地址：</td>\r\n");
            out.write("\t\t\t\t\t\t<td><input name=\"menuUrl\" />　非必填 例如：addform.jsp </td>\r\n");
            out.write("\t\t\t\t\t</tr>\r\n");
            out.write("\t\t\t\t\t<tr onMouseOver=\"mouseOver(this)\" onMouseOut=\"mouseOut(this)\">\r\n");
            out.write("\t\t\t\t\t\t<td align=\"right\">操作键：</td>\r\n");
            out.write("\t\t\t\t\t\t<td><input name=\"actionKey\" id=\"actionKey\" />　*必填 例如：adduser </td>\r\n");
            out.write("\t\t\t\t\t</tr>\r\n");
            out.write("\t\t\t\t\t<tr>\r\n");
            out.write("\t\t\t\t\t\t<td colspan=\"2\" class=\"form_button\" style=\"padding-top:10px;\">\r\n");
            out.write("\t\t\t\t\t\t\t<input type=\"submit\" value=\"保存\" />\r\n");
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
