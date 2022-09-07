class BackupThread extends Thread {
    public static void initialiseJobDatabase(String webappURL, final String username, final String password) throws IOException {
        Authenticator.setDefault(new Authenticator() {

            public PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(username, password.toCharArray());
            }
        });
        URL url = new URL(webappURL + "/admin/jobs");
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("POST");
        conn.setDoOutput(true);
        conn.setDoInput(true);
        DataOutputStream out = new DataOutputStream(conn.getOutputStream());
        String content = "ACTION=" + URLEncoder.encode("INITIALIZE", "UTF-8");
        out.writeBytes(content);
        out.flush();
        out.close();
        BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
        String line;
        while ((line = in.readLine()) != null) {
        }
        in.close();
    }
}
