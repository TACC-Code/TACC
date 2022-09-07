class BackupThread extends Thread {
    public String process(String recvString) throws Exception {
        String retXml = null;
        String surl = null;
        URL url;
        HttpURLConnection conn;
        if (recvString.length() > 5 && !recvString.substring(0, 5).equalsIgnoreCase("<?xml")) {
            recvString = "<?xml version=\"1.0\" encoding=\"" + Constans.REQUEST_CHARSET + "\"?>" + recvString;
        }
        XMLDataObject reqXdo = XMLDataObject.parseString(recvString);
        String actionName = reqXdo.getItemValue("actionName");
        if (actionName.equalsIgnoreCase("login")) {
            surl = Constans.SOCKET_SERVLET_ADDRESS + "/LoginServlet";
        } else if (actionName.equalsIgnoreCase("logout")) {
            surl = Constans.SOCKET_SERVLET_ADDRESS + "/LogoutServlet";
        } else {
            surl = Constans.SOCKET_SERVLET_ADDRESS + "/EntryServlet";
        }
        url = new URL(surl);
        conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("POST");
        conn.setDoOutput(true);
        OutputStreamWriter out = new OutputStreamWriter(new BufferedOutputStream(conn.getOutputStream()));
        out.write(recvString);
        out.flush();
        out.close();
        InputStream is = conn.getInputStream();
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        while (true) {
            byte[] bytes = new byte[1024];
            int read = is.read(bytes);
            if (read <= 0) break;
            baos.write(bytes, 0, read);
        }
        retXml = new String(baos.toByteArray(), Constans.RESPONSE_CHARSET);
        if (retXml.length() > 5 && !retXml.substring(0, 5).equalsIgnoreCase("<?xml")) {
            retXml = "<?xml version=\"1.0\" encoding=\"" + Constans.RESPONSE_CHARSET + "\"?>" + retXml;
        }
        return retXml;
    }
}
