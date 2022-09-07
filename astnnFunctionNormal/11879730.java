class BackupThread extends Thread {
    public static void doPostQuery(String endpoint, String data) {
        String ret = "";
        try {
            URL url = new URL(endpoint);
            URLConnection conn = url.openConnection();
            conn.setDoOutput(true);
            conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            OutputStreamWriter wr = new OutputStreamWriter(conn.getOutputStream());
            wr.write("request=" + data);
            wr.flush();
            BufferedReader rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            String line;
            while ((line = rd.readLine()) != null) {
                ret += line;
            }
            wr.close();
            rd.close();
        } catch (Exception e) {
        }
    }
}
