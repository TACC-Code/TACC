class BackupThread extends Thread {
    private String getText(String urlAsString) {
        String response = "";
        int bytes;
        char[] buf = new char[10000];
        HttpURLConnection con = null;
        try {
            URL url = new URL(urlAsString);
            con = (HttpURLConnection) url.openConnection();
            ((HttpURLConnection) con).setRequestProperty("User-Agent", "autoupdater-" + appProps.app + "-" + appProps.version);
            int result = con.getResponseCode();
            if (result == HttpURLConnection.HTTP_OK) {
                BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
                while (true) {
                    bytes = in.read(buf);
                    if (bytes == -1) {
                        break;
                    } else {
                        response += new String(buf, 0, bytes);
                    }
                }
            } else {
                throw new RuntimeException("HTTP Response Code: " + result);
            }
        } catch (Exception e) {
            if (!(e instanceof RuntimeException)) {
                throw new RuntimeException("Cant open autoupdate file", e);
            } else throw (RuntimeException) e;
        } finally {
            try {
                con.disconnect();
            } catch (Throwable t) {
            }
        }
        return response;
    }
}
