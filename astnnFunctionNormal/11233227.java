class BackupThread extends Thread {
    public synchronized void updateFederationMetadata() {
        try {
            URL url = new URL(LionShareApplicationSettings.FEDERATION_METADATA_URL.getValue());
            long fileUpdateTime = 0;
            File mdFile = this.getMetadataFilename();
            if (mdFile.exists()) {
                fileUpdateTime = mdFile.lastModified();
            }
            URLConnection conn = null;
            try {
                conn = url.openConnection();
                conn.setIfModifiedSince(fileUpdateTime);
                conn.connect();
            } catch (Exception exp) {
                LOG.error("Could load read updated federation metadata", exp);
                url = Thread.currentThread().getContextClassLoader().getResource("edu/psu/its/lionshare/security/InCommon-metadata.xml");
                conn = url.openConnection();
                conn.setIfModifiedSince(fileUpdateTime);
                conn.connect();
            }
            if (conn instanceof HttpURLConnection) {
                HttpURLConnection httpConn = (HttpURLConnection) conn;
                int responseCode = httpConn.getResponseCode();
                if (responseCode != 200) {
                    LOG.error("Unable to refresh federation metadata - received: " + httpConn.getResponseMessage());
                    return;
                }
            }
            int contentLength = conn.getContentLength();
            if (contentLength > 0) {
                LOG.info("Updating federation metadata file with new version from : " + new Date(conn.getLastModified()));
                BufferedInputStream bis = new BufferedInputStream(conn.getInputStream());
                FileOutputStream fos = new FileOutputStream(mdFile);
                byte[] buffer = new byte[20480];
                int count;
                while ((count = bis.read(buffer)) > -1) {
                    fos.write(buffer, 0, count);
                }
                fos.close();
            }
            if (conn instanceof HttpURLConnection) {
                ((HttpURLConnection) conn).disconnect();
            }
        } catch (Exception ex) {
            LOG.fatal("Unable to update federation metadata", ex);
        }
    }
}
