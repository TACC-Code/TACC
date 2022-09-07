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
        java.lang.Object _jspx_menu2_1 = null;
        java.lang.Integer _jspx_menuIndex2_1 = null;
        java.lang.Object _jspx_menu3_2 = null;
        java.lang.Integer _jspx_menuIndex3_2 = null;
        try {
            response.setContentType("text/html; charset=utf-8");
            pageContext = _jspxFactory.getPageContext(this, request, response, null, true, 8192, true);
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
            out.write("\r\n");
            out.write("\r\n");
            out.write("\r\n");
            out.write("<html>\r\n");
            out.write("<head>\r\n");
            out.write("<meta http-equiv=\"Content-Type\" content=\"text/html; charset=utf-8\" />\r\n");
            out.write("<title>角色权限管理</title>\r\n");
            out.write("<link type=\"text/css\" rel=\"stylesheet\" href=\"/system/css/style.css\" />\r\n");
            out.write("<script type=\"text/javascript\" language=\"javascript\" src=\"/system/js/public.js\"></script>\r\n");
            out.write("<script type=\"text/javascript\" language=\"javascript\" src=\"/system/js/jquery-1.4.2.js\"></script>\r\n");
            out.write("<script type=\"text/javascript\" language=\"javascript\">\r\n");
            out.write("$(document).ready(function(){\r\n");
            out.write("\t$(\"#checkAll\").click(function(){\r\n");
            out.write("\t\t$(\"input[name='actionKey']\").attr(\"checked\",true);\r\n");
            out.write("\t});\r\n");
            out.write("\t$(\"#checkNon\").click(function(){\r\n");
            out.write("\t\t$(\"input[name='actionKey']\").attr(\"checked\",false);\r\n");
            out.write("\t});\r\n");
            out.write("});\r\n");
            out.write("function reback(url){\r\n");
            out.write("\tlocation.href=url;\r\n");
            out.write("}\r\n");
            out.write("function Menu(menuId,menuName,actionKey){\r\n");
            out.write("\tthis.menuId=menuId;\r\n");
            out.write("\tthis.menuName=menuName;\r\n");
            out.write("\tthis.actionKey=actionKey;\r\n");
            out.write("}\r\n");
            out.write("function Button(buttonId,buttonName,actionKey){\r\n");
            out.write("\tthis.buttonId=buttonId;\r\n");
            out.write("\tthis.buttonName=buttonName;\r\n");
            out.write("\tthis.actionKey=actionKey;\r\n");
            out.write("}\r\n");
            out.write("var menuArray1=new Array();\r\n");
            out.write("var menuArray2=new Array();\r\n");
            out.write("var menuArray3=new Array();\r\n");
            out.write("var buttonArray=new Array();\r\n");
            out.write("function menuClick(menuId,level,parent1,parent2){\r\n");
            out.write("\tif(level==1 && !$$(\"menu\"+menuId).checked){\r\n");
            out.write("\t\tfor(var i=0;i<menuArray1.length;i++){\r\n");
            out.write("\t\t\tif(menuArray1[i].menuId==menuId){\r\n");
            out.write("\t\t\t\tfor(var j=0;j<menuArray2[i].length;j++){\r\n");
            out.write("\t\t\t\t\t$$(\"menu\"+menuArray2[i][j].menuId).checked=false;\r\n");
            out.write("\t\t\t\t\tfor(var k=0;k<menuArray3[i][j].length;k++){\r\n");
            out.write("\t\t\t\t\t\t$$(\"menu\"+menuArray3[i][j][k].menuId).checked=false;\r\n");
            out.write("\t\t\t\t\t}\r\n");
            out.write("\t\t\t\t}\r\n");
            out.write("\t\t\t\treturn;\r\n");
            out.write("\t\t\t}\r\n");
            out.write("\t\t}\r\n");
            out.write("\t}\r\n");
            out.write("\tif(level==2 && !$$(\"menu\"+menuId).checked){\r\n");
            out.write("\t\tfor(var i=0;i<menuArray2.length;i++){\r\n");
            out.write("\t\t\tfor(var j=0;j<menuArray2[i].length;j++){\r\n");
            out.write("\t\t\t\tif(menuArray2[i][j].menuId==menuId){\r\n");
            out.write("\t\t\t\t\tfor(var k=0;k<menuArray3[i][j].length;k++){\r\n");
            out.write("\t\t\t\t\t\t$$(\"menu\"+menuArray3[i][j][k].menuId).checked=false;\r\n");
            out.write("\t\t\t\t\t}\r\n");
            out.write("\t\t\t\t\treturn;\r\n");
            out.write("\t\t\t\t}\r\n");
            out.write("\t\t\t}\r\n");
            out.write("\t\t}\r\n");
            out.write("\t}\r\n");
            out.write("\tif(level==2 && $$(\"menu\"+menuId).checked && !$$(\"menu\"+parent1).checked){\r\n");
            out.write("\t\t$$(\"menu\"+parent1).checked=true;\r\n");
            out.write("\t}\r\n");
            out.write("\tif(level==3 && $$(\"menu\"+menuId).checked){\r\n");
            out.write("\t\tif(!$$(\"menu\"+parent2).checked)\r\n");
            out.write("\t\t\t$$(\"menu\"+parent2).checked=true;\r\n");
            out.write("\t\tif(!$$(\"menu\"+parent1).checked)\r\n");
            out.write("\t\t\t$$(\"menu\"+parent1).checked=true;\r\n");
            out.write("\t}\r\n");
            out.write("}\r\n");
            out.write("var p1,p2,p3;\r\n");
            out.write("</script>\r\n");
            out.write("</head>\r\n");
            out.write("<body>\r\n");
            if (_jspx_meth_mytag_005fView_005f0(_jspx_page_context)) return;
            out.write("\r\n");
            out.write("<div class=\"main\">\r\n");
            out.write("\t<div class=\"position\">当前位置: <a href=\"/sysadm/desktop.jsp\">桌 面</a> → <a href=\"rolelist.jsp\">角色管理</a> → 权限管理</div>\r\n");
            out.write("\t<div class=\"mainbody\">\r\n");
            out.write("\t\t<div class=\"toolbar\">\r\n");
            out.write("\t\t\t工具栏：\r\n");
            out.write("\t\t\t<input id=\"checkAll\" type=\"button\" value=\"全选\" class=\"system_button\" />\r\n");
            out.write("\t\t\t<input id=\"checkNon\" type=\"button\" value=\"全不选\" class=\"system_button\" />\r\n");
            out.write("\t\t\t<input type=\"button\" value=\"返回\" class=\"system_button\" onclick=\"reback('rolelist.jsp')\" />\r\n");
            out.write("\t\t</div>\r\n");
            out.write("\t\t<div class=\"operate_info\">\r\n");
            out.write("\t\t\t<font color=\"#FF0000\"><label id=\"explaininfo\">当前选择角色：");
            out.write((java.lang.String) org.apache.jasper.runtime.PageContextImpl.proprietaryEvaluate("${role.roleName }", java.lang.String.class, (PageContext) _jspx_page_context, null, false));
            out.write("</label></font>\r\n");
            out.write("\t\t</div>\r\n");
            out.write("\t\t<div class=\"table\">\r\n");
            out.write("\t\t\t<form action=\"/system/config/action.do\" method=\"post\">\r\n");
            out.write("\t\t\t\t<input name=\"roleId\" type=\"hidden\" value=\"");
            out.write((java.lang.String) org.apache.jasper.runtime.PageContextImpl.proprietaryEvaluate("${role.roleId }", java.lang.String.class, (PageContext) _jspx_page_context, null, false));
            out.write("\" />\r\n");
            out.write("\t\t\t\t<table width=\"100%\" border=\"0\" cellpadding=\"1\" cellspacing=\"1\" class=\"table_list\">\r\n");
            out.write("\t\t\t\t\t<tr>\r\n");
            out.write("\t\t\t\t\t\t<td width=\"50%\" style=\"vertical-align:top;border:none;\">\r\n");
            out.write("\t\t\t\t\t\t\t<table width=\"100%\" border=\"0\" cellpadding=\"1\" cellspacing=\"1\" class=\"table_list\">\r\n");
            out.write("\t\t\t\t\t\t\t\t<tr>\r\n");
            out.write("\t\t\t\t\t\t\t\t\t<th>菜单权限</th>\r\n");
            out.write("\t\t\t\t\t\t\t\t</tr>\r\n");
            out.write("\t\t\t\t\t\t\t\t");
            if (_jspx_meth_mytag_005flistMenuForActionSet_005f0(_jspx_page_context)) return;
            out.write("\r\n");
            out.write("\t\t\t\t\t\t\t\t");
            if (_jspx_meth_mytag_005fList_005f0(_jspx_page_context)) return;
            out.write("\r\n");
            out.write("\t\t\t\t\t\t\t\t");
            Object[] menuActionKeys = null;
            List menuActionList = (List) pageContext.getAttribute("menuActionList");
            if (menuActionList != null && menuActionList.size() > 0) {
                menuActionKeys = menuActionList.toArray();
                Arrays.sort(menuActionKeys);
            }
            out.write("\r\n");
            out.write("\t\t\t\t\t\t\t\t");
            org.apache.struts.taglib.logic.IterateTag _jspx_th_logic_005fiterate_005f0 = (org.apache.struts.taglib.logic.IterateTag) _005fjspx_005ftagPool_005flogic_005fiterate_0026_005fname_005findexId_005fid.get(org.apache.struts.taglib.logic.IterateTag.class);
            _jspx_th_logic_005fiterate_005f0.setPageContext(_jspx_page_context);
            _jspx_th_logic_005fiterate_005f0.setParent(null);
            _jspx_th_logic_005fiterate_005f0.setId("menu1");
            _jspx_th_logic_005fiterate_005f0.setName("menuList1");
            _jspx_th_logic_005fiterate_005f0.setIndexId("menuIndex1");
            int _jspx_eval_logic_005fiterate_005f0 = _jspx_th_logic_005fiterate_005f0.doStartTag();
            if (_jspx_eval_logic_005fiterate_005f0 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
                java.lang.Object menu1 = null;
                java.lang.Integer menuIndex1 = null;
                if (_jspx_eval_logic_005fiterate_005f0 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE) {
                    out = _jspx_page_context.pushBody();
                    _jspx_th_logic_005fiterate_005f0.setBodyContent((javax.servlet.jsp.tagext.BodyContent) out);
                    _jspx_th_logic_005fiterate_005f0.doInitBody();
                }
                menu1 = (java.lang.Object) _jspx_page_context.findAttribute("menu1");
                menuIndex1 = (java.lang.Integer) _jspx_page_context.findAttribute("menuIndex1");
                do {
                    out.write("\r\n");
                    out.write("\t\t\t\t\t\t\t\t\t<tr onMouseOver=\"mouseOver(this)\" onMouseOut=\"mouseOut(this)\">\r\n");
                    out.write("\t\t\t\t\t\t\t\t\t\t<td>\r\n");
                    out.write("\t\t\t\t\t\t\t\t\t\t\t<input name=\"actionKey\" id=\"menu");
                    out.write((java.lang.String) org.apache.jasper.runtime.PageContextImpl.proprietaryEvaluate("${menu1.menuId }", java.lang.String.class, (PageContext) _jspx_page_context, null, false));
                    out.write("\" type=\"checkbox\" onclick=\"menuClick(");
                    out.write((java.lang.String) org.apache.jasper.runtime.PageContextImpl.proprietaryEvaluate("${menu1.menuId }", java.lang.String.class, (PageContext) _jspx_page_context, null, false));
                    out.write(",1,0,0)\" value=\"");
                    out.write((java.lang.String) org.apache.jasper.runtime.PageContextImpl.proprietaryEvaluate("${menu1.actionKey }", java.lang.String.class, (PageContext) _jspx_page_context, null, false));
                    out.write("\" \r\n");
                    out.write("\t\t\t\t\t\t\t\t\t\t\t");
                    Menu menu01 = (Menu) pageContext.getAttribute("menu1");
                    if (menuActionKeys != null && Arrays.binarySearch(menuActionKeys, menu01.getActionKey()) > -1) out.print("checked");
                    out.write("\r\n");
                    out.write("\t\t\t\t\t\t\t\t\t\t\t />");
                    out.write((java.lang.String) org.apache.jasper.runtime.PageContextImpl.proprietaryEvaluate("${menu1.menuName }", java.lang.String.class, (PageContext) _jspx_page_context, null, false));
                    out.write("\r\n");
                    out.write("\t\t\t\t\t\t\t\t\t\t\t<input name=\"");
                    out.write((java.lang.String) org.apache.jasper.runtime.PageContextImpl.proprietaryEvaluate("${menu1.actionKey }", java.lang.String.class, (PageContext) _jspx_page_context, null, false));
                    out.write("\" type=\"hidden\" value=\"0\" />\r\n");
                    out.write("\t\t\t\t\t\t\t\t\t\t\t<script type=\"text/javascript\" language=\"javascript\">\r\n");
                    out.write("\t\t\t\t\t\t\t\t\t\t\t\tmenuArray1[menuArray1.length]=new Menu(\"");
                    out.write((java.lang.String) org.apache.jasper.runtime.PageContextImpl.proprietaryEvaluate("${menu1.menuId }", java.lang.String.class, (PageContext) _jspx_page_context, null, false));
                    out.write('"');
                    out.write(',');
                    out.write('"');
                    out.write((java.lang.String) org.apache.jasper.runtime.PageContextImpl.proprietaryEvaluate("${menu1.menuName }", java.lang.String.class, (PageContext) _jspx_page_context, null, false));
                    out.write('"');
                    out.write(',');
                    out.write('"');
                    out.write((java.lang.String) org.apache.jasper.runtime.PageContextImpl.proprietaryEvaluate("${menu1.actionKey }", java.lang.String.class, (PageContext) _jspx_page_context, null, false));
                    out.write("\");\r\n");
                    out.write("\t\t\t\t\t\t\t\t\t\t\t</script>\r\n");
                    out.write("\t\t\t\t\t\t\t\t\t\t</td>\r\n");
                    out.write("\t\t\t\t\t\t\t\t\t</tr>\r\n");
                    out.write("\t\t\t\t\t\t\t\t\t<script type=\"text/javascript\" language=\"javascript\">\r\n");
                    out.write("\t\t\t\t\t\t\t\t\t\tp1=menuArray1.length-1;\r\n");
                    out.write("\t\t\t\t\t\t\t\t\t\tmenuArray2[p1]=new Array();\r\n");
                    out.write("\t\t\t\t\t\t\t\t\t\tmenuArray3[p1]=new Array();\r\n");
                    out.write("\t\t\t\t\t\t\t\t\t</script>\r\n");
                    out.write("\t\t\t\t\t\t\t\t\t");
                    org.apache.struts.taglib.logic.IterateTag _jspx_th_logic_005fiterate_005f1 = (org.apache.struts.taglib.logic.IterateTag) _005fjspx_005ftagPool_005flogic_005fiterate_0026_005fname_005findexId_005fid.get(org.apache.struts.taglib.logic.IterateTag.class);
                    _jspx_th_logic_005fiterate_005f1.setPageContext(_jspx_page_context);
                    _jspx_th_logic_005fiterate_005f1.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_logic_005fiterate_005f0);
                    _jspx_th_logic_005fiterate_005f1.setId("menu2");
                    _jspx_th_logic_005fiterate_005f1.setName("menuList2");
                    _jspx_th_logic_005fiterate_005f1.setIndexId("menuIndex2");
                    int _jspx_eval_logic_005fiterate_005f1 = _jspx_th_logic_005fiterate_005f1.doStartTag();
                    if (_jspx_eval_logic_005fiterate_005f1 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
                        java.lang.Object menu2 = null;
                        java.lang.Integer menuIndex2 = null;
                        if (_jspx_eval_logic_005fiterate_005f1 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE) {
                            out = _jspx_page_context.pushBody();
                            _jspx_th_logic_005fiterate_005f1.setBodyContent((javax.servlet.jsp.tagext.BodyContent) out);
                            _jspx_th_logic_005fiterate_005f1.doInitBody();
                        }
                        menu2 = (java.lang.Object) _jspx_page_context.findAttribute("menu2");
                        menuIndex2 = (java.lang.Integer) _jspx_page_context.findAttribute("menuIndex2");
                        do {
                            out.write("\r\n");
                            out.write("\t\t\t\t\t\t\t\t\t\t");
                            org.apache.taglibs.standard.tag.rt.core.IfTag _jspx_th_c_005fif_005f0 = (org.apache.taglibs.standard.tag.rt.core.IfTag) _005fjspx_005ftagPool_005fc_005fif_0026_005ftest.get(org.apache.taglibs.standard.tag.rt.core.IfTag.class);
                            _jspx_th_c_005fif_005f0.setPageContext(_jspx_page_context);
                            _jspx_th_c_005fif_005f0.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_logic_005fiterate_005f1);
                            _jspx_th_c_005fif_005f0.setTest(((java.lang.Boolean) org.apache.jasper.runtime.PageContextImpl.proprietaryEvaluate("${menu2.menuParent==menu1.menuId }", java.lang.Boolean.class, (PageContext) _jspx_page_context, null, false)).booleanValue());
                            int _jspx_eval_c_005fif_005f0 = _jspx_th_c_005fif_005f0.doStartTag();
                            if (_jspx_eval_c_005fif_005f0 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
                                do {
                                    out.write("\r\n");
                                    out.write("\t\t\t\t\t\t\t\t\t\t\t<tr onMouseOver=\"mouseOver(this)\" onMouseOut=\"mouseOut(this)\">\r\n");
                                    out.write("\t\t\t\t\t\t\t\t\t\t\t\t<td style=\"padding-left:20px;\">\r\n");
                                    out.write("\t\t\t\t\t\t\t\t\t\t\t\t\t∟<input name=\"actionKey\" id=\"menu");
                                    out.write((java.lang.String) org.apache.jasper.runtime.PageContextImpl.proprietaryEvaluate("${menu2.menuId }", java.lang.String.class, (PageContext) _jspx_page_context, null, false));
                                    out.write("\" type=\"checkbox\" onclick=\"menuClick(");
                                    out.write((java.lang.String) org.apache.jasper.runtime.PageContextImpl.proprietaryEvaluate("${menu2.menuId }", java.lang.String.class, (PageContext) _jspx_page_context, null, false));
                                    out.write(',');
                                    out.write('2');
                                    out.write(',');
                                    out.write((java.lang.String) org.apache.jasper.runtime.PageContextImpl.proprietaryEvaluate("${menu2.menuParent }", java.lang.String.class, (PageContext) _jspx_page_context, null, false));
                                    out.write(",0)\" value=\"");
                                    out.write((java.lang.String) org.apache.jasper.runtime.PageContextImpl.proprietaryEvaluate("${menu2.actionKey }", java.lang.String.class, (PageContext) _jspx_page_context, null, false));
                                    out.write("\" \r\n");
                                    out.write("\t\t\t\t\t\t\t\t\t\t\t\t\t");
                                    Menu menu02 = (Menu) pageContext.getAttribute("menu2");
                                    if (menuActionKeys != null && Arrays.binarySearch(menuActionKeys, menu02.getActionKey()) > -1) out.print("checked");
                                    out.write("\r\n");
                                    out.write("\t\t\t\t\t\t\t\t\t\t\t\t\t />");
                                    out.write((java.lang.String) org.apache.jasper.runtime.PageContextImpl.proprietaryEvaluate("${menu2.menuName }", java.lang.String.class, (PageContext) _jspx_page_context, null, false));
                                    out.write("\r\n");
                                    out.write("\t\t\t\t\t\t\t\t\t\t\t\t\t<input name=\"");
                                    out.write((java.lang.String) org.apache.jasper.runtime.PageContextImpl.proprietaryEvaluate("${menu2.actionKey }", java.lang.String.class, (PageContext) _jspx_page_context, null, false));
                                    out.write("\" type=\"hidden\" value=\"0\" />\r\n");
                                    out.write("\t\t\t\t\t\t\t\t\t\t\t\t\t<script type=\"text/javascript\" language=\"javascript\">\r\n");
                                    out.write("\t\t\t\t\t\t\t\t\t\t\t\t\t\tmenuArray2[menuArray1.length-1][menuArray2[menuArray1.length-1].length]=new Menu(\"");
                                    out.write((java.lang.String) org.apache.jasper.runtime.PageContextImpl.proprietaryEvaluate("${menu2.menuId }", java.lang.String.class, (PageContext) _jspx_page_context, null, false));
                                    out.write('"');
                                    out.write(',');
                                    out.write('"');
                                    out.write((java.lang.String) org.apache.jasper.runtime.PageContextImpl.proprietaryEvaluate("${menu2.menuName }", java.lang.String.class, (PageContext) _jspx_page_context, null, false));
                                    out.write('"');
                                    out.write(',');
                                    out.write('"');
                                    out.write((java.lang.String) org.apache.jasper.runtime.PageContextImpl.proprietaryEvaluate("${menu2.actionKey }", java.lang.String.class, (PageContext) _jspx_page_context, null, false));
                                    out.write("\");\r\n");
                                    out.write("\t\t\t\t\t\t\t\t\t\t\t\t\t</script>\r\n");
                                    out.write("\t\t\t\t\t\t\t\t\t\t\t\t</td>\r\n");
                                    out.write("\t\t\t\t\t\t\t\t\t\t\t</tr>\r\n");
                                    out.write("\t\t\t\t\t\t\t\t\t\t\t<script type=\"text/javascript\" language=\"javascript\">\r\n");
                                    out.write("\t\t\t\t\t\t\t\t\t\t\t\tp2=menuArray2[p1].length-1;\r\n");
                                    out.write("\t\t\t\t\t\t\t\t\t\t\t\tmenuArray3[p1][p2]=new Array();\r\n");
                                    out.write("\t\t\t\t\t\t\t\t\t\t\t</script>\r\n");
                                    out.write("\t\t\t\t\t\t\t\t\t\t\t");
                                    org.apache.struts.taglib.logic.IterateTag _jspx_th_logic_005fiterate_005f2 = (org.apache.struts.taglib.logic.IterateTag) _005fjspx_005ftagPool_005flogic_005fiterate_0026_005fname_005findexId_005fid.get(org.apache.struts.taglib.logic.IterateTag.class);
                                    _jspx_th_logic_005fiterate_005f2.setPageContext(_jspx_page_context);
                                    _jspx_th_logic_005fiterate_005f2.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_c_005fif_005f0);
                                    _jspx_th_logic_005fiterate_005f2.setId("menu3");
                                    _jspx_th_logic_005fiterate_005f2.setName("menuList3");
                                    _jspx_th_logic_005fiterate_005f2.setIndexId("menuIndex3");
                                    int _jspx_eval_logic_005fiterate_005f2 = _jspx_th_logic_005fiterate_005f2.doStartTag();
                                    if (_jspx_eval_logic_005fiterate_005f2 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
                                        java.lang.Object menu3 = null;
                                        java.lang.Integer menuIndex3 = null;
                                        if (_jspx_eval_logic_005fiterate_005f2 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE) {
                                            out = _jspx_page_context.pushBody();
                                            _jspx_th_logic_005fiterate_005f2.setBodyContent((javax.servlet.jsp.tagext.BodyContent) out);
                                            _jspx_th_logic_005fiterate_005f2.doInitBody();
                                        }
                                        menu3 = (java.lang.Object) _jspx_page_context.findAttribute("menu3");
                                        menuIndex3 = (java.lang.Integer) _jspx_page_context.findAttribute("menuIndex3");
                                        do {
                                            out.write("\r\n");
                                            out.write("\t\t\t\t\t\t\t\t\t\t\t\t");
                                            org.apache.taglibs.standard.tag.rt.core.IfTag _jspx_th_c_005fif_005f1 = (org.apache.taglibs.standard.tag.rt.core.IfTag) _005fjspx_005ftagPool_005fc_005fif_0026_005ftest.get(org.apache.taglibs.standard.tag.rt.core.IfTag.class);
                                            _jspx_th_c_005fif_005f1.setPageContext(_jspx_page_context);
                                            _jspx_th_c_005fif_005f1.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_logic_005fiterate_005f2);
                                            _jspx_th_c_005fif_005f1.setTest(((java.lang.Boolean) org.apache.jasper.runtime.PageContextImpl.proprietaryEvaluate("${menu3.menuParent==menu2.menuId }", java.lang.Boolean.class, (PageContext) _jspx_page_context, null, false)).booleanValue());
                                            int _jspx_eval_c_005fif_005f1 = _jspx_th_c_005fif_005f1.doStartTag();
                                            if (_jspx_eval_c_005fif_005f1 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
                                                do {
                                                    out.write("\r\n");
                                                    out.write("\t\t\t\t\t\t\t\t\t\t\t\t\t<tr onMouseOver=\"mouseOver(this)\" onMouseOut=\"mouseOut(this)\">\r\n");
                                                    out.write("\t\t\t\t\t\t\t\t\t\t\t\t\t\t<td style=\"padding-left:50px;\">\r\n");
                                                    out.write("\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t∟<input name=\"actionKey\" id=\"menu");
                                                    out.write((java.lang.String) org.apache.jasper.runtime.PageContextImpl.proprietaryEvaluate("${menu3.menuId }", java.lang.String.class, (PageContext) _jspx_page_context, null, false));
                                                    out.write("\" type=\"checkbox\" onclick=\"menuClick(");
                                                    out.write((java.lang.String) org.apache.jasper.runtime.PageContextImpl.proprietaryEvaluate("${menu3.menuId }", java.lang.String.class, (PageContext) _jspx_page_context, null, false));
                                                    out.write(',');
                                                    out.write('3');
                                                    out.write(',');
                                                    out.write((java.lang.String) org.apache.jasper.runtime.PageContextImpl.proprietaryEvaluate("${menu2.menuParent }", java.lang.String.class, (PageContext) _jspx_page_context, null, false));
                                                    out.write(',');
                                                    out.write((java.lang.String) org.apache.jasper.runtime.PageContextImpl.proprietaryEvaluate("${menu3.menuParent }", java.lang.String.class, (PageContext) _jspx_page_context, null, false));
                                                    out.write(")\" value=\"");
                                                    out.write((java.lang.String) org.apache.jasper.runtime.PageContextImpl.proprietaryEvaluate("${menu3.actionKey }", java.lang.String.class, (PageContext) _jspx_page_context, null, false));
                                                    out.write("\" \r\n");
                                                    out.write("\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t");
                                                    Menu menu03 = (Menu) pageContext.getAttribute("menu3");
                                                    if (menuActionKeys != null && Arrays.binarySearch(menuActionKeys, menu03.getActionKey()) > -1) out.print("checked");
                                                    out.write("\r\n");
                                                    out.write("\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t />");
                                                    out.write((java.lang.String) org.apache.jasper.runtime.PageContextImpl.proprietaryEvaluate("${menu3.menuName }", java.lang.String.class, (PageContext) _jspx_page_context, null, false));
                                                    out.write("\r\n");
                                                    out.write("\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t<input name=\"");
                                                    out.write((java.lang.String) org.apache.jasper.runtime.PageContextImpl.proprietaryEvaluate("${menu3.actionKey }", java.lang.String.class, (PageContext) _jspx_page_context, null, false));
                                                    out.write("\" type=\"hidden\" value=\"0\" />\r\n");
                                                    out.write("\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t<script type=\"text/javascript\" language=\"javascript\">\r\n");
                                                    out.write("\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\tmenuArray3[p1][p2][menuArray3[p1][p2].length]=new Menu(\"");
                                                    out.write((java.lang.String) org.apache.jasper.runtime.PageContextImpl.proprietaryEvaluate("${menu3.menuId }", java.lang.String.class, (PageContext) _jspx_page_context, null, false));
                                                    out.write('"');
                                                    out.write(',');
                                                    out.write('"');
                                                    out.write((java.lang.String) org.apache.jasper.runtime.PageContextImpl.proprietaryEvaluate("${menu3.menuName }", java.lang.String.class, (PageContext) _jspx_page_context, null, false));
                                                    out.write('"');
                                                    out.write(',');
                                                    out.write('"');
                                                    out.write((java.lang.String) org.apache.jasper.runtime.PageContextImpl.proprietaryEvaluate("${menu3.actionKey }", java.lang.String.class, (PageContext) _jspx_page_context, null, false));
                                                    out.write("\");\r\n");
                                                    out.write("\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t</script>\r\n");
                                                    out.write("\t\t\t\t\t\t\t\t\t\t\t\t\t\t</td>\r\n");
                                                    out.write("\t\t\t\t\t\t\t\t\t\t\t\t\t</tr>\r\n");
                                                    out.write("\t\t\t\t\t\t\t\t\t\t\t\t");
                                                    int evalDoAfterBody = _jspx_th_c_005fif_005f1.doAfterBody();
                                                    if (evalDoAfterBody != javax.servlet.jsp.tagext.BodyTag.EVAL_BODY_AGAIN) break;
                                                } while (true);
                                            }
                                            if (_jspx_th_c_005fif_005f1.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
                                                _005fjspx_005ftagPool_005fc_005fif_0026_005ftest.reuse(_jspx_th_c_005fif_005f1);
                                                return;
                                            }
                                            _005fjspx_005ftagPool_005fc_005fif_0026_005ftest.reuse(_jspx_th_c_005fif_005f1);
                                            out.write("\r\n");
                                            out.write("\t\t\t\t\t\t\t\t\t\t\t");
                                            int evalDoAfterBody = _jspx_th_logic_005fiterate_005f2.doAfterBody();
                                            menu3 = (java.lang.Object) _jspx_page_context.findAttribute("menu3");
                                            menuIndex3 = (java.lang.Integer) _jspx_page_context.findAttribute("menuIndex3");
                                            if (evalDoAfterBody != javax.servlet.jsp.tagext.BodyTag.EVAL_BODY_AGAIN) break;
                                        } while (true);
                                        if (_jspx_eval_logic_005fiterate_005f2 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE) {
                                            out = _jspx_page_context.popBody();
                                        }
                                    }
                                    if (_jspx_th_logic_005fiterate_005f2.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
                                        _005fjspx_005ftagPool_005flogic_005fiterate_0026_005fname_005findexId_005fid.reuse(_jspx_th_logic_005fiterate_005f2);
                                        return;
                                    }
                                    _005fjspx_005ftagPool_005flogic_005fiterate_0026_005fname_005findexId_005fid.reuse(_jspx_th_logic_005fiterate_005f2);
                                    out.write("\r\n");
                                    out.write("\t\t\t\t\t\t\t\t\t\t");
                                    int evalDoAfterBody = _jspx_th_c_005fif_005f0.doAfterBody();
                                    if (evalDoAfterBody != javax.servlet.jsp.tagext.BodyTag.EVAL_BODY_AGAIN) break;
                                } while (true);
                            }
                            if (_jspx_th_c_005fif_005f0.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
                                _005fjspx_005ftagPool_005fc_005fif_0026_005ftest.reuse(_jspx_th_c_005fif_005f0);
                                return;
                            }
                            _005fjspx_005ftagPool_005fc_005fif_0026_005ftest.reuse(_jspx_th_c_005fif_005f0);
                            out.write("\r\n");
                            out.write("\t\t\t\t\t\t\t\t\t");
                            int evalDoAfterBody = _jspx_th_logic_005fiterate_005f1.doAfterBody();
                            menu2 = (java.lang.Object) _jspx_page_context.findAttribute("menu2");
                            menuIndex2 = (java.lang.Integer) _jspx_page_context.findAttribute("menuIndex2");
                            if (evalDoAfterBody != javax.servlet.jsp.tagext.BodyTag.EVAL_BODY_AGAIN) break;
                        } while (true);
                        if (_jspx_eval_logic_005fiterate_005f1 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE) {
                            out = _jspx_page_context.popBody();
                        }
                    }
                    if (_jspx_th_logic_005fiterate_005f1.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
                        _005fjspx_005ftagPool_005flogic_005fiterate_0026_005fname_005findexId_005fid.reuse(_jspx_th_logic_005fiterate_005f1);
                        return;
                    }
                    _005fjspx_005ftagPool_005flogic_005fiterate_0026_005fname_005findexId_005fid.reuse(_jspx_th_logic_005fiterate_005f1);
                    out.write("\r\n");
                    out.write("\t\t\t\t\t\t\t\t");
                    int evalDoAfterBody = _jspx_th_logic_005fiterate_005f0.doAfterBody();
                    menu1 = (java.lang.Object) _jspx_page_context.findAttribute("menu1");
                    menuIndex1 = (java.lang.Integer) _jspx_page_context.findAttribute("menuIndex1");
                    if (evalDoAfterBody != javax.servlet.jsp.tagext.BodyTag.EVAL_BODY_AGAIN) break;
                } while (true);
                if (_jspx_eval_logic_005fiterate_005f0 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE) {
                    out = _jspx_page_context.popBody();
                }
            }
            if (_jspx_th_logic_005fiterate_005f0.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
                _005fjspx_005ftagPool_005flogic_005fiterate_0026_005fname_005findexId_005fid.reuse(_jspx_th_logic_005fiterate_005f0);
                return;
            }
            _005fjspx_005ftagPool_005flogic_005fiterate_0026_005fname_005findexId_005fid.reuse(_jspx_th_logic_005fiterate_005f0);
            out.write("\r\n");
            out.write("\t\t\t\t\t\t\t</table>\r\n");
            out.write("\t\t\t\t\t\t</td>\r\n");
            out.write("\t\t\t\t\t\t<td style=\"vertical-align:top;border:none;\">\r\n");
            out.write("\t\t\t\t\t\t\t<table width=\"100%\" border=\"0\" cellpadding=\"1\" cellspacing=\"1\" class=\"table_list\">\r\n");
            out.write("\t\t\t\t\t\t\t\t<tr>\r\n");
            out.write("\t\t\t\t\t\t\t\t\t<th colspan=\"2\">操作权限</th>\r\n");
            out.write("\t\t\t\t\t\t\t\t</tr>\r\n");
            out.write("\t\t\t\t\t\t\t\t");
            if (_jspx_meth_mytag_005fList_005f1(_jspx_page_context)) return;
            out.write("\r\n");
            out.write("\t\t\t\t\t\t\t\t");
            if (_jspx_meth_mytag_005fList_005f2(_jspx_page_context)) return;
            out.write("\r\n");
            out.write("\t\t\t\t\t\t\t\t");
            Object[] buttonActionKeys = null;
            List buttonActionList = (List) pageContext.getAttribute("buttonActionList");
            if (buttonActionList != null && buttonActionList.size() > 0) {
                buttonActionKeys = buttonActionList.toArray();
                Arrays.sort(buttonActionKeys);
            }
            out.write("\r\n");
            out.write("\t\t\t\t\t\t\t\t");
            org.apache.struts.taglib.logic.IterateTag _jspx_th_logic_005fiterate_005f3 = (org.apache.struts.taglib.logic.IterateTag) _005fjspx_005ftagPool_005flogic_005fiterate_0026_005fname_005fid.get(org.apache.struts.taglib.logic.IterateTag.class);
            _jspx_th_logic_005fiterate_005f3.setPageContext(_jspx_page_context);
            _jspx_th_logic_005fiterate_005f3.setParent(null);
            _jspx_th_logic_005fiterate_005f3.setId("button");
            _jspx_th_logic_005fiterate_005f3.setName("buttonList");
            int _jspx_eval_logic_005fiterate_005f3 = _jspx_th_logic_005fiterate_005f3.doStartTag();
            if (_jspx_eval_logic_005fiterate_005f3 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
                java.lang.Object button = null;
                if (_jspx_eval_logic_005fiterate_005f3 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE) {
                    out = _jspx_page_context.pushBody();
                    _jspx_th_logic_005fiterate_005f3.setBodyContent((javax.servlet.jsp.tagext.BodyContent) out);
                    _jspx_th_logic_005fiterate_005f3.doInitBody();
                }
                button = (java.lang.Object) _jspx_page_context.findAttribute("button");
                do {
                    out.write("\r\n");
                    out.write("\t\t\t\t\t\t\t\t\t<tr onMouseOver=\"mouseOver(this)\" onMouseOut=\"mouseOut(this)\">\r\n");
                    out.write("\t\t\t\t\t\t\t\t\t\t<td>\r\n");
                    out.write("\t\t\t\t\t\t\t\t\t\t\t<input name=\"actionKey\" type=\"checkbox\" value=\"");
                    out.write((java.lang.String) org.apache.jasper.runtime.PageContextImpl.proprietaryEvaluate("${button.actionKey }", java.lang.String.class, (PageContext) _jspx_page_context, null, false));
                    out.write("\" \r\n");
                    out.write("\t\t\t\t\t\t\t\t\t\t\t");
                    Button buttonMatch = (Button) pageContext.getAttribute("button");
                    if (buttonActionKeys != null && Arrays.binarySearch(buttonActionKeys, buttonMatch.getActionKey()) > -1) out.print("checked");
                    out.write("\r\n");
                    out.write("\t\t\t\t\t\t\t\t\t\t\t />");
                    out.write((java.lang.String) org.apache.jasper.runtime.PageContextImpl.proprietaryEvaluate("${button.buttonNote }", java.lang.String.class, (PageContext) _jspx_page_context, null, false));
                    out.write("\r\n");
                    out.write("\t\t\t\t\t\t\t\t\t\t\t<input name=\"");
                    out.write((java.lang.String) org.apache.jasper.runtime.PageContextImpl.proprietaryEvaluate("${button.actionKey }", java.lang.String.class, (PageContext) _jspx_page_context, null, false));
                    out.write("\" type=\"hidden\" value=\"1\" />\r\n");
                    out.write("\t\t\t\t\t\t\t\t\t\t</td>\r\n");
                    out.write("\t\t\t\t\t\t\t\t\t</tr>\r\n");
                    out.write("\t\t\t\t\t\t\t\t");
                    int evalDoAfterBody = _jspx_th_logic_005fiterate_005f3.doAfterBody();
                    button = (java.lang.Object) _jspx_page_context.findAttribute("button");
                    if (evalDoAfterBody != javax.servlet.jsp.tagext.BodyTag.EVAL_BODY_AGAIN) break;
                } while (true);
                if (_jspx_eval_logic_005fiterate_005f3 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE) {
                    out = _jspx_page_context.popBody();
                }
            }
            if (_jspx_th_logic_005fiterate_005f3.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
                _005fjspx_005ftagPool_005flogic_005fiterate_0026_005fname_005fid.reuse(_jspx_th_logic_005fiterate_005f3);
                return;
            }
            _005fjspx_005ftagPool_005flogic_005fiterate_0026_005fname_005fid.reuse(_jspx_th_logic_005fiterate_005f3);
            out.write("\r\n");
            out.write("\t\t\t\t\t\t\t</table>\r\n");
            out.write("\t\t\t\t\t\t</td>\r\n");
            out.write("\t\t\t\t\t</tr>\r\n");
            out.write("\t\t\t\t\t<tr>\r\n");
            out.write("\t\t\t\t\t\t<td colspan=\"2\" class=\"form_button\" style=\"padding-top:10px;\">\r\n");
            out.write("\t\t\t\t\t\t\t<input type=\"submit\" value=\"保存\" />\r\n");
            out.write("\t\t\t\t\t\t\t<input type=\"reset\" value=\"重置\" />\r\n");
            out.write("\t\t\t\t\t\t</td>\r\n");
            out.write("\t\t\t\t\t</tr>\r\n");
            out.write("\t\t\t\t</table>\r\n");
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
