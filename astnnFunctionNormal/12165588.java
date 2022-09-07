class BackupThread extends Thread {
    public static void main(String[] args) throws Exception {
        String SNUPS_SERVLET_URL = "http://localhost:8080/snups-servlet/snups?cycleNo=5";
        URL url = new URL(SNUPS_SERVLET_URL);
        URLConnection conn = url.openConnection();
        BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
        String inputLine;
        while ((inputLine = in.readLine()) != null) System.out.println(inputLine);
        in.close();
    }
}
