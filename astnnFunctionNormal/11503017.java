class BackupThread extends Thread {
    private void performResumableUpload(String path, final String uploadUrlStr, String mimeType, String title) throws MalformedURLException, ProtocolException, IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        FileInputStream fis = new FileInputStream(getFileForPath(path));
        byte[] buf = new byte[8192];
        int read = -1;
        do {
            read = fis.read(buf);
            baos.write(buf, 0, read);
        } while (read == 8192);
        fis.close();
        URL uploadUrl = new URL(uploadUrlStr);
        HttpURLConnection conn = (HttpURLConnection) uploadUrl.openConnection();
        if (title == null) {
            conn.setRequestMethod("PUT");
        } else {
            conn.setRequestMethod("POST");
        }
        HttpAuthToken authToken = (HttpAuthToken) service.getAuthTokenFactory().getAuthToken();
        String header = authToken.getAuthorizationHeader(uploadUrl, conn.getRequestMethod());
        conn.setRequestProperty("Authorization", header);
        conn.setRequestProperty("GData-Version", "3.0");
        conn.setRequestProperty("Content-Type", mimeType);
        if (title != null) {
            conn.setRequestProperty("Slug", title);
        } else {
            conn.setRequestProperty("If-Match", "*");
        }
        long contentLength = baos.size();
        conn.setRequestProperty("X-Upload-Content-Length", "" + contentLength);
        conn.setRequestProperty("X-Upload-Content-Type", "" + mimeType);
        conn.setFixedLengthStreamingMode(0);
        conn.setDoOutput(true);
        conn.connect();
        BufferedReader br2_ = new BufferedReader(new InputStreamReader(conn.getInputStream()));
        String line2_ = "";
        while ((line2_ = br2_.readLine()) != null) {
            System.out.println(line2_);
        }
        String location = conn.getHeaderField("Location");
        conn.disconnect();
        conn = (HttpURLConnection) new URL(location).openConnection();
        conn.setRequestMethod("PUT");
        conn.setRequestProperty("Content-Length", "" + contentLength);
        conn.setRequestProperty("Content-Range", "bytes 0-" + (contentLength - 1) + "/" + contentLength);
        conn.setDoOutput(true);
        conn.connect();
        conn.getOutputStream().write(baos.toByteArray());
        BufferedReader br2 = new BufferedReader(new InputStreamReader(conn.getInputStream()));
        String line2 = "";
        while ((line2 = br2.readLine()) != null) {
            System.out.println(line2);
        }
    }
}
