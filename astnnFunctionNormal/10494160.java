class BackupThread extends Thread {
    static void responsecode(String[] args) throws Exception {
        String urlstr = "http://lynwood:8801/wlservlet-functests/putSessionData";
        URL url = new URL(urlstr);
        HttpURLConnection con = (HttpURLConnection) url.openConnection();
        con.connect();
        int responseCode = con.getResponseCode();
        System.out.println("responseCode: " + responseCode);
        String key = null;
        String value = null;
        int i = 1;
        while ((key = con.getHeaderFieldKey(i)) != null) {
            value = con.getHeaderField(i);
            System.out.println("PAIR: " + key + " -> " + value + " [NL]");
            i++;
        }
        responseCode = con.getResponseCode();
        System.out.println("responseCode: " + responseCode);
        InputStreamReader isr = new InputStreamReader(con.getInputStream());
        int len = -1;
        char[] ca = new char[1024];
        StringBuffer contents = new StringBuffer();
        while ((len = isr.read(ca, 0, 1024)) != -1) {
            contents.append(ca, 0, len);
        }
        String result = contents.toString().trim();
        System.out.println("--- OUTPUT STRINGBUFFER ---");
        System.out.println("--- OUTPUT STRINGBUFFER ---");
    }
}
