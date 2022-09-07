class BackupThread extends Thread {
    public void downloadFile(DownloadFile downloadFile, boolean isTestMode, DownloaderProxy proxy) {
        String sUrl = downloadFile.getUrl();
        this.logger.finer("Preparing to download the URL: " + sUrl);
        if (sUrl == null) {
            downloadFile.setDownloadStatus("<html>font color='green'>No URL selected</font></html>");
            this.logger.fine("Nothing to download. URL not set.");
            return;
        }
        URL url = null;
        try {
            url = new URL(sUrl);
        } catch (MalformedURLException ex) {
            this.logger.log(Level.SEVERE, null, ex);
            downloadFile.setDownloadStatus("<html>font color='red'>Malformed URL</font></html>");
            LoggingBoard.logDownloaderMessage("Malformed URL: '" + sUrl + "'. Exception: '" + ex.toString() + "'. Message of Exception is: '" + ex.getMessage() + "'.");
            return;
        }
        String remoteFile = url.getFile();
        this.logger.finer("Trying to find a file name in the URL: " + sUrl);
        String[] splittees = remoteFile.split("/");
        int length = splittees.length;
        String filename = splittees[length - 1];
        this.logger.finer("Found the file name: " + filename + " in the URL: " + sUrl);
        String localDirectoryRelativ = downloadFile.getLocalDirectoryRelativ();
        XPlanetRessourceFinder rf = new XPlanetRessourceFinder();
        String downloaderRootDir = rf.getRootDirectoryForDownloads();
        String localDirectoryAbsolute = downloaderRootDir + File.separator + localDirectoryRelativ;
        File dir = new File(localDirectoryAbsolute);
        if (!dir.exists()) {
            this.logger.warning("Making a directory '" + localDirectoryAbsolute + "' to download because it does not exist...");
            LoggingBoard.logDownloaderMessage("Making a directory '" + localDirectoryAbsolute + "' to download because it does not exist...");
            boolean b = dir.mkdirs();
            if (!b) {
                this.logger.warning("Faild to make directory '" + dir + "' to download because it does not exist.");
                LoggingBoard.logDownloaderMessage("Faild to make directory '" + dir + "' to download because it does not exist.");
            }
        }
        String localFile = localDirectoryAbsolute + "/" + filename;
        this.logger.finer("Trying to download the remote file '" + sUrl + "' to '" + localFile + "'...");
        HttpURLConnection http = null;
        long byteCount = 0;
        BufferedInputStream in = null;
        BufferedOutputStream out = null;
        try {
            String proxySet = proxy.getProxySet();
            if (proxySet.equalsIgnoreCase("true")) {
                Properties systemSettings = System.getProperties();
                String proxyHost = proxy.getProxyHost();
                String proxyPort = proxy.getProxyPort();
                String proxyUser = proxy.getProxyUser();
                String proxyPass = proxy.getProxyPass();
                systemSettings.put("proxySet", "true");
                systemSettings.put("http.proxyHost", proxyHost);
                systemSettings.put("http.proxyPort", proxyPort);
                if (proxyUser != null && !proxyUser.equals("")) {
                    Authenticator.setDefault(new SimpleAuthenticator(proxyUser, proxyPass));
                }
            }
            http = (HttpURLConnection) url.openConnection();
            http.setRequestMethod("HEAD");
            int responseCode = http.getResponseCode();
            String responseMessage = http.getResponseMessage();
            String longResponseMessage = responseCode + ", " + responseMessage;
            if (responseCode == HttpURLConnection.HTTP_OK) {
                this.logger.finer("File exists on server. URL: " + sUrl);
            } else {
                downloadFile.setDownloadStatus("<html><font color='red'>" + longResponseMessage + "</font></html>");
                String message = "File not found on server or problems with proxy: '" + sUrl + "'. Server Response '" + longResponseMessage + "'.";
                this.logger.info(message);
                LoggingBoard.logDownloaderMessage(message);
                return;
            }
            long lastModifiedOnServer = http.getLastModified();
            long lastModifiedOnDisk = downloadFile.getLastModified();
            String timeOnServer = TimeStamp.getFullTimstamp(new Date(lastModifiedOnServer));
            String timeOnDisc = TimeStamp.getFullTimstamp(new Date(lastModifiedOnDisk));
            if (lastModifiedOnServer > lastModifiedOnDisk) {
                this.logger.finer("File on server is newer '" + timeOnServer + "' than on local disc '" + timeOnDisc + "'. Downloading url '" + sUrl + "'...");
            } else if (lastModifiedOnServer == lastModifiedOnDisk) {
                if (lastModifiedOnServer == 0) {
                    this.logger.warning("The Server gave a lastModified() of '0'. It this happens for Maps it might be ok. Proceeding with download.");
                    LoggingBoard.logDownloaderMessage("WATCH THIS: Server responded with '0' for lastModified(). Proceed download of " + sUrl);
                } else if (isTestMode) {
                    downloadFile.setDownloadStatus("<html><font color='green'>Download with same Date as on Server</font></html>");
                    this.logger.finer("File on server is as old '" + timeOnServer + "' as on local disc '" + timeOnDisc + "'. No need to download url '" + sUrl + "'.");
                } else {
                    downloadFile.setDownloadStatus("<html><font color='red'>File on Server was not updated</font></html>");
                    this.logger.finer("File on server is as old '" + timeOnServer + "' as on local disc '" + timeOnDisc + "'. No need to download url '" + sUrl + "'.");
                    return;
                }
            } else {
                if (isTestMode) {
                    downloadFile.setDownloadStatus("<html><font color='red'>Download, File on Server older than local copy</font></html>");
                    String message = "File on server '" + timeOnServer + "' is older than local disc '" + timeOnDisc + "'. Did you change the server or URL? URL: '" + sUrl + "'...";
                    this.logger.info(message);
                    LoggingBoard.logDownloaderMessage(message);
                } else {
                    downloadFile.setDownloadStatus("<html><font color='red'>File on Server older than local copy</font></html>");
                    String message = "File on server '" + timeOnServer + "' is older than local disc '" + timeOnDisc + "'. Did you change the server? How ever, no need to download '" + sUrl + "'...";
                    this.logger.info(message);
                    LoggingBoard.logDownloaderMessage(message);
                    return;
                }
            }
            this.logger.finer("Opening connection for url: " + sUrl);
            in = new BufferedInputStream(new DataInputStream(url.openStream()));
            out = new BufferedOutputStream(new DataOutputStream(new FileOutputStream(localFile)));
            byte[] bbuf = new byte[4096];
            length = -1;
            while ((length = in.read(bbuf)) != -1) {
                out.write(bbuf, 0, length);
                byteCount = byteCount + length;
            }
            this.logger.finest("Wrote '" + byteCount + "' bytes into file '" + localFile + "'.");
            in.close();
            in = null;
            out.close();
            out = null;
            http.disconnect();
            http = null;
            downloadFile.setLastModified(lastModifiedOnServer);
            downloadFile.setDownloadStatus("<html><font color='green'>File up to date with Server</font></html>");
            this.logger.info("Downloaded file '" + sUrl + "' to '" + localFile + "'.");
            LoggingBoard.logDownloaderMessage("Downloaded: " + sUrl);
            if (isTestMode) {
                LoggingBoard.logDownloaderMessage("Copied to: " + localFile);
            }
        } catch (java.net.UnknownHostException ex) {
            downloadFile.setDownloadStatus("<html><font color='red'>Connection failed</font></html>");
            this.logger.finer("Download failed, URL: " + sUrl + ". Exception: " + ex.toString() + ". Original messages: " + ex.getLocalizedMessage());
            if (isTestMode) {
                LoggingBoard.logDownloaderMessage("Connection failed: " + sUrl);
                LoggingBoard.logDownloaderMessage("Connection Error: " + ex.toString());
            }
        } catch (Exception ex) {
            downloadFile.setDownloadStatus("<html><font color='red'>Download failed</font></html>");
            this.logger.finer("Download failed, URL: " + sUrl + ". Exception: " + ex.toString() + ". Original messages: " + ex.getLocalizedMessage());
            if (isTestMode) {
                LoggingBoard.logDownloaderMessage("Download failed: " + sUrl);
                LoggingBoard.logDownloaderMessage("Original Error: " + ex.toString());
            }
        } finally {
            if (in != null) {
                try {
                    in.close();
                } catch (Exception e) {
                }
            }
            if (out != null) {
                try {
                    out.close();
                } catch (Exception e) {
                }
            }
            if (http != null) {
                try {
                    http.disconnect();
                } catch (Exception e) {
                }
            }
        }
    }
}
