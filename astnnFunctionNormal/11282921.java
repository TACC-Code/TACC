class BackupThread extends Thread {
    public static String get(String p_url, String p_message) throws Exception {
        System.out.println("getting from: " + p_url);
        URL x_url = new URL(p_url + p_message);
        System.out.println("get to --> " + x_url.toString());
        URLConnection x_conn = x_url.openConnection();
        InputStreamReader is_reader = new InputStreamReader(x_conn.getInputStream());
        System.out.println("Got input stream reader");
        BufferedReader reader = new BufferedReader(is_reader);
        System.out.println("Got reader");
        String line = null;
        StringBuffer x_buf = new StringBuffer();
        System.out.println("*******************RESPONSE******************************");
        while ((line = reader.readLine()) != null) {
            System.out.println(line);
            x_buf.append(line).append("\n");
        }
        return x_buf.toString();
    }
}
