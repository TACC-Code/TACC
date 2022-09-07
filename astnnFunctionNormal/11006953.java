class BackupThread extends Thread {
    @Test
    public void writeHead() throws Exception {
        User u = new User();
        u.setId(id);
        URL url = new URL("http://www.youxiy.com/uploads/allimg/081107/2159580.jpg");
        URLConnection conn = url.openConnection();
        conn.connect();
        fsus.writeFace(conn.getInputStream(), u);
    }
}
