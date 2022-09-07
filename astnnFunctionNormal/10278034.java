class BackupThread extends Thread {
    public byte[] doPost(URL url, byte[] body) {
        byte[] response = null;
        HttpURLConnection conn = null;
        try {
            conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("POST");
            conn.setDoOutput(true);
            conn.setDoInput(true);
            conn.setUseCaches(false);
            conn.setRequestProperty("Content-Type", "application/octet-stream");
            conn.setRequestProperty("Content-Transfer-Encoding", "binary");
            conn.setRequestProperty("accept", "application/octet-stream");
            if (_sessionOpen) conn.setRequestProperty("Cookie", _sessionId);
            conn.connect();
            OutputStream post_data = conn.getOutputStream();
            post_data.write(body);
            post_data.close();
            if (conn.getResponseCode() == 200) {
                System.out.println("Response code: " + conn.getResponseCode() + " (" + conn.getResponseMessage() + ")");
                parseHeader(conn);
                response = getRequestBytes(conn.getInputStream());
            } else {
                System.out.println("Response code: " + conn.getResponseCode() + " (" + conn.getResponseMessage() + ")");
            }
        } catch (IOException e) {
            System.out.println("Error while sending POST " + e);
        } finally {
            if (null != conn) conn.disconnect();
        }
        return response;
    }
}
