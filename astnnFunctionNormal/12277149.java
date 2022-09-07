class BackupThread extends Thread {
    private String sendImpl(String from, String destNumber, String text) throws IOException {
        final URL url = new URL(BULKSMS_GATEWAY_URL);
        final HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        final StringBuilder data = new StringBuilder();
        data.append("username=").append(URLEncoder.encode(username, "ISO-8859-1"));
        data.append("&password=").append(URLEncoder.encode(password, "ISO-8859-1"));
        data.append("&message=").append(URLEncoder.encode(text, "ISO-8859-1"));
        data.append("&msisdn=").append(destNumber);
        data.append("&sender=").append(URLEncoder.encode(from, "ISO-8859-1"));
        data.append("&routing_group=").append(routingGroup.getId());
        data.append("&repliable=0");
        final String dataStr = data.toString();
        conn.setRequestMethod("POST");
        conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
        conn.setRequestProperty("Content-Length", Integer.toString(dataStr.length()));
        conn.setDoOutput(true);
        conn.connect();
        final OutputStreamWriter writer = new OutputStreamWriter(conn.getOutputStream());
        try {
            writer.write(dataStr);
        } finally {
            writer.close();
        }
        final BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream(), "UTF-8"));
        try {
            return reader.readLine();
        } finally {
            reader.close();
        }
    }
}
