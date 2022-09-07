class BackupThread extends Thread {
    @Test
    public void testJresHandler1() {
        try {
            URL url = new URL("jres://ProtocolHandlerTest/test1.txt");
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.setRequestMethod("POST");
            con.setDoInput(true);
            con.setDoOutput(true);
            con.connect();
            OutputStream out = con.getOutputStream();
            PrintWriter p = new PrintWriter(out);
            p.println("This is my request");
            p.close();
            InputStream in = con.getInputStream();
            BufferedReader r = new BufferedReader(new InputStreamReader(in));
            assertEquals("This is my response", r.readLine());
            r.close();
            con.disconnect();
        } catch (Exception ex) {
            fail(ex.getMessage());
        }
    }
}
