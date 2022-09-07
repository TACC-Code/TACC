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
            response.setContentType("text/html");
            pageContext = _jspxFactory.getPageContext(this, request, response, null, true, 8192, true);
            _jspx_page_context = pageContext;
            application = pageContext.getServletContext();
            config = pageContext.getServletConfig();
            session = pageContext.getSession();
            out = pageContext.getOut();
            _jspx_out = out;
            out.write("\r\n");
            out.write("\r\n");
            out.write("\r\n");
            out.write("\r\n");
            out.write("\r\n");
            out.write("\r\n");
            out.write("\r\n");
            out.write("\r\n");
            out.write("<script src=\"./js/geral.js\" type=\"text/javascript\"></script>\r\n");
            out.write("<script src=\"./js/jQuery.js\" type=\"text/javascript\"></script>\r\n");
            out.write("<script src=\"./js/calendar.js\" type=\"text/javascript\"></script>\r\n");
            out.write("<script src=\"./js/jquery.alphanumeric.pack.js\" type=\"text/javascript\"></script>\r\n");
            out.write("<script src=\"./js/jquery.tablesorter.js\" type=\"text/javascript\"></script>\r\n");
            out.write("<script src=\"./js/linhaTabela.js\" type=\"text/javascript\"></script>\r\n");
            out.write("\r\n");
            out.write("<script>\r\n");
            out.write("\t\r\n");
            out.write("\tfunction carregarMascaras() {\r\n");
            out.write("\r\n");
            out.write("\t\tmesMask = new Mask(\"##\", \"number\");\r\n");
            out.write("\t\tmesMask.attach(document.getElementById('mesInicial'));\r\n");
            out.write("\t\tmesMask.attach(document.getElementById('mesFinal'));\r\n");
            out.write("\t\t\r\n");
            out.write("\t\tanoMask = new Mask(\"####\", \"number\");\r\n");
            out.write("\t\tanoMask.attach(document.getElementById('anoInicial'));\r\n");
            out.write("\t\tanoMask.attach(document.getElementById('anoFinal'));\r\n");
            out.write("\t\t\r\n");
            out.write("\t\t\r\n");
            out.write("\t}\r\n");
            out.write("\t\r\n");
            out.write("\r\n");
            out.write("\tfunction imprimir(id){\r\n");
            out.write("\t\tdocument.getElementById('metodo').value = \"imprimir\";\r\n");
            out.write("\t\tdocument.getElementById('reciboId').value = id;\r\n");
            out.write("\t\tdocument.forms[0].submit(); \r\n");
            out.write("\t}\r\n");
            out.write("\t\r\n");
            out.write("\tfunction vizualizar(){\r\n");
            out.write("\t\tdocument.getElementById('metodo').value = \"vizualizar\";\r\n");
            out.write("\t\tdocument.forms[0].submit()\r\n");
            out.write("\t}\r\n");
            out.write("\t\r\n");
            out.write("</script>\r\n");
            out.write("\r\n");
            out.write("<div id=\"corpo\">\r\n");
            out.write("\r\n");
            out.write("<div class=\"breadcrumb\">\r\n");
            out.write("\t");
            if (_jspx_meth_html_005flink_005f0(_jspx_page_context)) return;
            out.write("\r\n");
            out.write("\t\t&raquo;<a class=\"ativo\" href=\"#\">Relatório de Situação das Parcelas</a> </div>\r\n");
            out.write("\r\n");
            if (_jspx_meth_html_005fform_005f0(_jspx_page_context)) return;
            out.write("\r\n");
            out.write("\r\n");
            out.write("<script>\r\n");
            out.write("\tcarregarMascaras();\r\n");
            out.write("</script>\r\n");
            out.write("</div>");
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
