class BackupThread extends Thread {
    private int downloadRemoteFile(File destFile, String filePath, long fileCount) {
        int ret = 0;
        if (cancelDownload) {
            return ret;
        }
        HttpURLConnection connection = null;
        try {
            URL url = new URL(updateServer + "/" + filePath);
            setupProxy();
            connection = (HttpURLConnection) url.openConnection();
            connection.setDoInput(true);
            connection.setDoOutput(true);
            connection.setUseCaches(false);
            connection.setRequestMethod("GET");
            HttpURLConnection.setFollowRedirects(true);
            connection.connect();
            int statusCode = connection.getResponseCode();
            if (statusCode != HttpURLConnection.HTTP_OK) {
                return 0;
            }
            InputStream in = connection.getInputStream();
            FileOutputStream out = new FileOutputStream(destFile);
            byte[] data = new byte[4096];
            int read = -1;
            while ((read = in.read(data, 0, 4096)) != -1) {
                out.write(data, 0, read);
                downloadCompleted += read;
                progressEvent.progress(fileCount, downloadCompleted, downloadCount, downloadSize);
                if (cancelDownload) {
                    break;
                }
            }
            out.flush();
            out.close();
            in.close();
        } catch (IOException e) {
        } finally {
            if (connection != null) {
                connection.disconnect();
            }
        }
        ret = (int) destFile.length();
        return ret;
    }
}
