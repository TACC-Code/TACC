class BackupThread extends Thread {
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Enumeration<Locale> it = request.getLocales();
        String path = request.getRequestURI();
        path = path.substring(request.getContextPath().length());
        if (path.equals("/")) path = "/main"; else {
            InputStream in = null;
            while ((in == null) && it.hasMoreElements()) in = getClass().getResourceAsStream("resources/" + it.nextElement().toString() + path);
            if (in == null) in = getClass().getResourceAsStream("resources/root" + path);
            if (in != null) {
                byte[] b = new byte[4096];
                int count;
                ServletOutputStream out = response.getOutputStream();
                while ((count = in.read(b)) >= 0) out.write(b, 0, count);
                out.close();
                return;
            }
        }
        path += ".xml";
        InputStream in = null;
        while ((in == null) && it.hasMoreElements()) in = getClass().getResourceAsStream("resources/" + it.nextElement().toString() + path);
        if (in == null) in = getClass().getResourceAsStream("resources/root" + path);
        if (in == null) {
            response.sendError(404, Text.get("Resource \"{0}\" not found.", path));
            return;
        }
        XmlDocument doc = Tools.Xml.toDocument(new InputSource(in));
        XmlNode root = doc.getRoot();
        if (!"layer".equals(root.getName())) {
            response.sendError(500, Text.get("Resource \"{0}\" doesn't have the main layer.", path));
            return;
        }
        String html = createRoot(root, request);
        response.getWriter().println(html);
    }
}
