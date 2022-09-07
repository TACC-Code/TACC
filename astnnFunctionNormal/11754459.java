class BackupThread extends Thread {
    @SuppressWarnings("static-access")
    public void connect() {
        try {
            URL url = new URL(m_mjpgURL);
            m_httpConn = (HttpURLConnection) url.openConnection();
            @SuppressWarnings("unused") Map mp = m_httpConn.getHeaderFields();
            Hashtable headers = utils.readHttpHeaders(m_httpConn);
            for (Object key : headers.keySet()) {
                System.out.println(key.toString() + " | " + headers.get(key).toString());
            }
            InputStream is = m_httpConn.getInputStream();
            connected = true;
            BufferedInputStream bis = new BufferedInputStream(is);
            m_dis = new DataInputStream(bis);
            utils = new Utils(m_dis);
            m_ctype = (String) headers.get("content-type");
            int bidx = m_ctype.indexOf("boundary=");
            m_boundary = m_ctype.substring(bidx + 9);
            System.err.println(m_boundary);
            if (!initCompleted) initDisplay();
        } catch (Exception e) {
            System.err.println("Baglanti Problemi !");
            e.printStackTrace();
        }
    }
}
