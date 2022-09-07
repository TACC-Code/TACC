class BackupThread extends Thread {
    public byte[] doGET(URL url) {
        byte[] response = null;
        HttpURLConnection conn = null;
        try {
            conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.setDoInput(true);
            conn.setUseCaches(false);
            conn.setRequestProperty("Content-Type", "application/octet-stream");
            conn.setRequestProperty("Content-Transfer-Encoding", "binary");
            conn.setRequestProperty("accept", "application/octet-stream");
            if (_sessionOpen) conn.setRequestProperty("Cookie", _sessionId);
            conn.connect();
            if (conn.getResponseCode() == 200) {
                System.out.println("Response code: " + conn.getResponseCode() + " (" + conn.getResponseMessage() + ")");
                parseHeader(conn);
                response = getRequestBytes(conn.getInputStream());
            } else {
                System.out.println("Response code: " + conn.getResponseCode() + " (" + conn.getResponseMessage() + ")");
            }
        } catch (IOException e) {
            System.out.println("GET failed!" + e);
        } finally {
        }
        return response;
    }
}
