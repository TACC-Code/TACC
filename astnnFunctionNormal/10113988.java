class BackupThread extends Thread {
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {
            initConfig();
        } catch (NamingException e) {
            resp.getWriter().println("Could not access configurationManager bean: " + e);
            return;
        }
        if (!makeLogin(resp.getWriter(), req, USERLEVEL_STANDARD)) {
            return;
        }
        PrintWriter out = resp.getWriter();
        printHeader("Edit configuration", out, req);
        String errors = "";
        LinkedList<String> settingswithError = new LinkedList<String>();
        if (req.getParameter(FIELD_SAFE_EDIT_CONFIG) != null) {
            for (SettingCategory category : configManager.getCategories()) {
                for (Setting setting : category.getSettings()) {
                    String inputkey = generateInputFieldName(setting);
                    String input = req.getParameter(inputkey);
                    switch(setting.getType()) {
                        case Setting.TYPE_INT:
                            try {
                                new Integer(input).intValue();
                            } catch (NumberFormatException e) {
                                errors += "<br>" + setting.getName();
                                settingswithError.add(setting.getName());
                            }
                            break;
                        case Setting.TYPE_INT_LIST:
                            try {
                                setting.setValue(input);
                                setting.getAsListOfIntegers();
                            } catch (IllegalArgumentException e) {
                                errors += "<br>" + setting.getName();
                                settingswithError.add(setting.getName());
                            }
                            break;
                        case Setting.TYPE_URL:
                            try {
                                setting.setValue(input);
                                java.net.URL url = setting.getAsUrl();
                                url.openStream().close();
                            } catch (Exception e) {
                                settingswithError.add(setting.getName());
                            }
                            break;
                    }
                }
            }
            if (errors.length() != 0) {
                printError("incorrect input for: " + errors, out);
            }
        }
        if (req.getParameter(FIELD_SAFE_EDIT_CONFIG) != null) {
            for (Setting setting : configManager.getSettings()) {
                String key = generateInputFieldName(setting);
                if (req.getParameter(key) != null) {
                    setting.setValue(req.getParameter(key));
                    configManager.updateSetting(setting);
                }
            }
            printConfirm("Die Konfiguration wurde ge�ndert", out);
        }
        out.println("<form method=POST action=\"" + URI_EDIT_SERVLET + "\">");
        boolean isNewConfig = false;
        printHiddenField(out, FIELD_SAFE_EDIT_CONFIG, 666);
        startSubBlock("Konfiguration �ndern", out, new String[] { "Name", "Wert" });
        startSubRow(out);
        out.println("<td width=50%>Name der Konfiguration<br></td><td width=50%>");
        out.print("CSE Configuration");
        out.println("</td>");
        endSubRow(out);
        endSubBlock(out);
        isNewConfig = false;
        for (SettingCategory category : configManager.getCategories()) {
            startSubBlock(category.getDescription(), out, new String[] { "Name", "Value", "Type", "Default" });
            for (Setting setting : category.getSettings()) {
                String key = generateInputFieldName(setting);
                startSubRow(out);
                out.println("<td width=*>" + setting.getDescription() + "<br>");
                if (isSuperAdmin()) {
                    out.println("<span class=smalldescribe>" + setting.getName() + "</span>");
                }
                out.println("</td><td width=350>");
                if (setting.getAsString() == null) {
                    setting.setToDefault();
                }
                boolean hasError = settingswithError.contains(setting.getName());
                switch(setting.getType()) {
                    case Setting.TYPE_INT_LIST:
                        out.print("<textarea style=\"width:340px;\" " + (hasError ? "class=badinput" : "") + " rows=" + (setting.getAsListOfIntegers().length + 1) + " cols=30 name=\"" + key + "\">");
                        for (int n : setting.getAsListOfIntegers()) {
                            out.println(n);
                        }
                        out.print("</textarea>");
                        break;
                    case Setting.TYPE_TEXT:
                        out.print("<textarea style=\"width:340px;\" rows=7 cols=30 name=\"" + key + "\">");
                        out.println(setting.getAsString());
                        out.print("</textarea>");
                        break;
                    case Setting.TYPE_STRING_LIST:
                        out.print("<textarea style=\"width:340px;\"" + (hasError ? "class=badinput" : "") + "  rows=" + (setting.getAsListOfStrings().length + 1) + " cols=30 name=\"" + key + "\">");
                        for (String s : setting.getAsListOfStrings()) {
                            out.println(s);
                        }
                        out.print("</textarea>");
                        break;
                    case Setting.TYPE_BOOL:
                        out.print("<input class=inputnoborder type=radio value=\"" + Setting.BOOLEAN_FALSE + "\" name=\"" + key + "\"");
                        if (!setting.getAsBoolean()) {
                            out.print(" checked");
                        }
                        out.print(">");
                        printIcon(out, "choice-no.png", "", null, null);
                        out.print(" &nbsp;&nbsp;&nbsp;<input class=inputnoborder type=radio value=\"" + Setting.BOOLEAN_TRUE + "\" name=\"" + key + "\"");
                        if (setting.getAsBoolean()) {
                            out.print(" checked");
                        }
                        out.print(">");
                        printIcon(out, "choice-yes.png", "", null, null);
                        break;
                    default:
                        out.print("<input style=\"width:340px;\"" + (hasError ? "class=badinput" : "") + "  type=text value=\"" + setting.getAsString() + "\" size=40 name=\"" + key + "\">");
                }
                out.println("</td><td width=32 align=center>");
                String iconurl = "";
                switch(setting.getType()) {
                    case Setting.TYPE_INT_LIST:
                        iconurl = "type_int.png";
                        break;
                    case Setting.TYPE_INT:
                        iconurl = "type_int.png";
                        break;
                    case Setting.TYPE_URL:
                        iconurl = "type_url.png";
                        break;
                    case Setting.TYPE_PASSWORD:
                        iconurl = "type_password.png";
                        break;
                    case Setting.TYPE_BOOL:
                        iconurl = "type_bool.png";
                        break;
                    default:
                        iconurl = "type_string.png";
                }
                printIcon(out, iconurl, "", null, null);
                out.println("</td><td width=350>" + setting.getDefaultValue() + "</td>");
                endSubRow(out);
            }
            endSubBlock(out);
        }
        out.println("<table width=100%><tr>");
        out.println("<td width=50% align=center>");
        printIcon(out, "error.png", "Abbrechen", EditConfigurationServlet.URI_EDIT_SERVLET, null);
        out.println("</td><td width=50% align=center>");
        out.println("<input style=\"background-color:#ffffff;border:0px\" type=image src=\"icon/confirmed.png\">");
        out.println("</td></tr></table>");
        out.println("</form>");
        printFooter(out);
    }
}
