class BackupThread extends Thread {
    public static String post(String p_url, byte[] p_message) throws Exception {
        System.out.println("posting to: " + p_url);
        URL x_url = new URL(p_url);
        URLConnection x_conn = x_url.openConnection();
        x_conn.setDoInput(true);
        x_conn.setDoOutput(true);
        x_conn.setUseCaches(false);
        DataOutputStream x_printout;
        DataInputStream x_input;
        x_printout = new DataOutputStream(x_conn.getOutputStream());
        x_printout.write(p_message, 0, p_message.length);
        x_printout.flush();
        x_printout.close();
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
