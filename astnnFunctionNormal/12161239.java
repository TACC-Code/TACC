class BackupThread extends Thread {
    protected byte[] readUrlContent(URL url) {
        byte[] content = null;
        ByteArrayOutputStream output = null;
        InputStream input = null;
        try {
            URLConnection conn = url.openConnection();
            conn.connect();
            if (conn instanceof HttpURLConnection) {
                HttpURLConnection httpConn = (HttpURLConnection) conn;
                int httpResponseCode = httpConn.getResponseCode();
                String httpResponseMessage = httpConn.getResponseMessage();
                if (httpResponseCode != HttpURLConnection.HTTP_OK) {
                    if (log.isErrorEnabled()) {
                        log.error("Error opening URL connection for '" + url.toString() + "', HTTP status: " + httpResponseCode + ", HTTP response message: " + httpResponseMessage);
                    }
                    return null;
                }
            }
        } catch (Exception e) {
            if (log.isErrorEnabled()) {
                log.error("Error opening URL connection for '" + url.toString() + "'");
            }
            return null;
        }
        try {
            output = new ByteArrayOutputStream();
            input = url.openStream();
            byte[] buf = new byte[512];
            for (int len = 0; (len = input.read(buf)) != -1; output.write(buf, 0, len)) ;
            content = output.toByteArray();
        } catch (Exception e) {
            if (log.isErrorEnabled()) {
                log.error("Error reading contents of URL *" + url.toString() + "'");
            }
        } finally {
            if (output != null) {
                try {
                    output.close();
                } catch (Throwable t) {
                } finally {
                    output = null;
                }
            }
            if (input != null) {
                try {
                    input.close();
                } catch (Throwable t) {
                } finally {
                    input = null;
                }
            }
        }
        return content;
    }
}
