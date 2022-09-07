class BackupThread extends Thread {
    public static void deploy(String server, String webappPath, String contextFile, String warFile, final String username, final String password) throws IOException {
        Authenticator.setDefault(new Authenticator() {

            public PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(username, password.toCharArray());
            }
        });
        URL url = new URL(server + "/manager/deploy?path=" + webappPath + "&config=" + contextFile);
        InputStream in = url.openStream();
        byte[] buffer = new byte[1024];
        int read = in.read(buffer);
        while (read >= 0) {
            read = in.read(buffer);
        }
        in.close();
        System.out.println("Successfully deployed webapp " + webappPath);
    }
}
