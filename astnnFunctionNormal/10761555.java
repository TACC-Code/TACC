class BackupThread extends Thread {
    private void wireUpSslWithRemoteService(String host, SslRelay listener) throws IOException {
        File keystore = File.createTempFile("selenium-rc-" + host, "keystore");
        String urlString = "http://dangerous-certificate-authority.openqa.org/genkey.jsp?padding=" + _sslMap.size() + "&domain=" + host;
        URL url = new URL(urlString);
        URLConnection conn = url.openConnection();
        conn.connect();
        InputStream is = conn.getInputStream();
        byte[] buffer = new byte[1024];
        int length;
        FileOutputStream fos = new FileOutputStream(keystore);
        while ((length = is.read(buffer)) != -1) {
            fos.write(buffer, 0, length);
        }
        fos.close();
        is.close();
        listener.setKeystore(keystore.getAbsolutePath());
        listener.setNukeDirOrFile(keystore);
    }
}
