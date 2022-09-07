class BackupThread extends Thread {
    public static String fetchUrl(String urlStr) throws IOException {
        URL url = null;
        if (urlStr == null) {
            throw new IllegalArgumentException("url cannot be null");
        }
        if (urlStr.indexOf(":", 5) > 5 && urlStr.indexOf("@") > 4) {
            String tmpUrl = "http://" + urlStr.substring(urlStr.indexOf("@") + 1);
            String uname = urlStr.substring(7, urlStr.indexOf(":", 7));
            String pass = urlStr.substring(urlStr.indexOf(":", 7) + 1, urlStr.indexOf("@"));
            java.net.Authenticator.setDefault(new MilspecAuthenticator(uname, pass));
            url = new URL(tmpUrl);
        } else {
            url = new URL(urlStr);
        }
        URLConnection urlConn = url.openConnection();
        BufferedInputStream is = new BufferedInputStream(urlConn.getInputStream());
        BufferedReader asciiReader = new BufferedReader(new InputStreamReader(is, "ASCII"));
        StringBuffer data = new StringBuffer();
        String line = "";
        while ((line = asciiReader.readLine()) != null) {
            data.append(line);
        }
        is.close();
        return data.toString();
    }
}
