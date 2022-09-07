class BackupThread extends Thread {
    public void downloadImage(File imageFile, String imageURL) throws IOException {
        if (mjbProxyHost != null) {
            System.getProperties().put("proxySet", "true");
            System.getProperties().put("proxyHost", mjbProxyHost);
            System.getProperties().put("proxyPort", mjbProxyPort);
        }
        URL url = new URL(imageURL);
        Semaphore s = getSemaphore(url.getHost().toLowerCase());
        s.acquireUninterruptibly();
        try {
            URLConnection cnx = url.openConnection();
            if (imageURL.toLowerCase().indexOf("thetvdb") > 0) cnx.setRequestProperty("Referer", "http://forums.thetvdb.com/");
            if (mjbProxyUsername != null) {
                cnx.setRequestProperty("Proxy-Authorization", mjbEncodedPassword);
            }
            sendHeader(cnx);
            readHeader(cnx);
            FileTools.copy(cnx.getInputStream(), new FileOutputStream(imageFile));
        } finally {
            s.release();
        }
    }
}
