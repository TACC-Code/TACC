class BackupThread extends Thread {
    protected byte[] httpGetNonNative(TreeLogger branch, String userAgent, String url) {
        Throwable caught;
        InputStream is = null;
        try {
            URL urlToGet = new URL(url);
            URLConnection conn = urlToGet.openConnection();
            conn.setRequestProperty("User-Agent", userAgent);
            is = conn.getInputStream();
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            byte[] buffer = new byte[4096];
            int bytesRead;
            while ((bytesRead = is.read(buffer)) != -1) {
                baos.write(buffer, 0, bytesRead);
            }
            byte[] response = baos.toByteArray();
            return response;
        } catch (MalformedURLException e) {
            caught = e;
        } catch (IOException e) {
            caught = e;
        } finally {
            if (is != null) {
                try {
                    is.close();
                } catch (IOException e) {
                }
            }
        }
        if (System.getProperty(PROPERTY_DEBUG_HTTP_GET) != null) {
            branch.log(CHECK_ERROR, "Exception in HTTP request", caught);
        }
        return null;
    }
}
