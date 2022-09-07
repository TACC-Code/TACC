class BackupThread extends Thread {
    public void run(Session session) throws IOException, UnsupportedEncodingException {
        URLConnection urlConn = sqlURL.openConnection();
        urlConn.setUseCaches(false);
        InputStream is = urlConn.getInputStream();
        try {
            run(session, is);
        } finally {
            is.close();
        }
    }
}
