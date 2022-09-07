class BackupThread extends Thread {
            public void run() {
                try {
                    ResourceDownloaderFactory rdf;
                    URL url_get = new URL(url);
                    URLConnection urlcon = url_get.openConnection();
                    String type = urlcon.getContentType();
                    if (type != null && type.startsWith("text/xml")) {
                        RSSReader.parseRSS(url, pluginInterface);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
}
