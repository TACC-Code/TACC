class BackupThread extends Thread {
    void setResourceHTML(Bundle bundle, String path) {
        StringBuffer sb = new StringBuffer();
        sb.append("<html>");
        URL url = bundle.getResource(path);
        sb.append("<table border=0>");
        sb.append("<tr><td width=\"100%\" bgcolor=\"#eeeeee\">");
        startFont(sb, "-1");
        sb.append("#" + bundle.getBundleId() + " " + path);
        sb.append("</font>\n");
        sb.append("</td>\n");
        sb.append("</tr>\n");
        sb.append("<tr>");
        sb.append("<td>");
        sb.append("<pre>");
        sb.append("<font size=\"-1\">");
        try {
            byte[] bytes = Util.readStream(url.openStream());
            String value = new String(bytes);
            value = Strings.replace(value, "<", "&lt;");
            value = Strings.replace(value, ">", "&gt;");
            sb.append(value);
        } catch (Exception e) {
            StringWriter sw = new StringWriter();
            PrintWriter pw = new PrintWriter(sw);
            e.printStackTrace(pw);
            sb.append(sw.toString());
        }
        sb.append("</font>\n");
        sb.append("</pre>");
        sb.append("</td>");
        sb.append("</tr>");
        sb.append("</table>");
        sb.append("</html>");
        setHTML(sb.toString());
    }
}
