class BackupThread extends Thread {
    public static AthenServletResponse callServlet(Method method, String page, String args) throws MalformedURLException, IOException, ParserConfigurationException, SAXException, XPathExpressionException {
        if (method == null) {
            throw new NullPointerException("method was null");
        }
        URL url = null;
        URLConnection urlConn = null;
        InputStreamReader input = null;
        OutputStreamWriter output = null;
        StringBuffer xml = new StringBuffer(0);
        char[] buffer = new char[1024];
        try {
            switch(method) {
                case GET:
                    if (args != null && args.length() > 0) {
                        url = new URL(page + '?' + args);
                    } else {
                        url = new URL(page);
                    }
                    break;
                case POST:
                    url = new URL(page);
                    break;
                default:
                    url = new URL(page);
                    break;
            }
            urlConn = url.openConnection();
            if (method == Method.POST) {
                urlConn.setDoOutput(true);
            }
            urlConn.connect();
            if ((method == Method.POST) && (args != null && args.length() > 0)) {
                output = new OutputStreamWriter(urlConn.getOutputStream());
                output.write(args);
                output.flush();
                output.close();
                output = null;
            }
            input = new InputStreamReader(urlConn.getInputStream());
            while (true) {
                int read = input.read(buffer);
                if (read == -1) {
                    break;
                }
                xml.append(buffer, 0, read);
            }
            input.close();
            input = null;
            return new AthenServletResponse(xml.toString());
        } finally {
            if (input != null) {
                input.close();
            }
            if (output != null) {
                output.close();
            }
        }
    }
}
