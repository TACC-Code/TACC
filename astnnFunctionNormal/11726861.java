class BackupThread extends Thread {
    private String downloadPacContent(String url) throws IOException {
        if (url == null) {
            throw new IOException("Invalid PAC script URL: null");
        }
        HttpURLConnection con = (HttpURLConnection) new URL(url).openConnection(Proxy.NO_PROXY);
        con.setInstanceFollowRedirects(true);
        con.setRequestProperty("accept", "application/x-ns-proxy-autoconfig, */*;q=0.8");
        if (con.getResponseCode() != 200) {
            throw new IOException("Server returned: " + con.getResponseCode() + " " + con.getResponseMessage());
        }
        this.expireAtMillis = con.getExpiration();
        String charsetName = parseCharsetFromHeader(con.getContentType());
        BufferedReader r = new BufferedReader(new InputStreamReader(con.getInputStream(), charsetName));
        try {
            StringBuilder result = new StringBuilder();
            try {
                String line;
                while ((line = r.readLine()) != null) {
                    result.append(line).append("\n");
                }
            } finally {
                r.close();
                con.disconnect();
            }
            return result.toString();
        } finally {
            r.close();
        }
    }
}
