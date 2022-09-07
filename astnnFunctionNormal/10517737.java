class BackupThread extends Thread {
    public void read(URL url, Thread afterReading) throws Exception {
        URLConnection xmlConn = url.openConnection();
        SFSInputStream sfsInputStream = new SFSInputStream(xmlConn.getInputStream(), xmlConn.getContentLength());
        read(url.toString(), sfsInputStream, afterReading);
    }
}
