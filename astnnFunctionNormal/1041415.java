class BackupThread extends Thread {
    private InputStream openFeed() throws IOException {
        URL url = new URL(feed);
        URLConnection connection = url.openConnection();
        connection.setDoInput(true);
        if (user != null) {
            String userPassword = user + ":" + password;
            String encoding = new sun.misc.BASE64Encoder().encode(userPassword.getBytes());
            connection.setRequestProperty("Authorization", "Basic " + encoding);
        }
        return connection.getInputStream();
    }
}
