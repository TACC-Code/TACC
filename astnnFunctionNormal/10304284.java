class BackupThread extends Thread {
    public Content loadContent(String urlString) {
        try {
            URL url = new URL(urlString);
            InputStream is = url.openStream();
            if (is == null) {
                throw new RuntimeException("Cannot open url stream (null).");
            }
            File contentFile = makeCacheFile(urlString);
            OutputStream os = new FileOutputStream(contentFile);
            StreamHelper.copy(is, os);
            os.close();
            return new Content(contentFile);
        } catch (MalformedURLException e) {
            throw new RuntimeException("An error occured during getting content: " + e.getMessage(), e);
        } catch (IOException e) {
            throw new RuntimeException("An error occured during getting content: " + e.getMessage(), e);
        }
    }
}
