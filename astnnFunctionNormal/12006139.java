class BackupThread extends Thread {
    private boolean trailerDownload(Movie movie, String trailerUrl, File trailerFile) {
        Timer timer = new Timer();
        timer.schedule(new TimerTask() {

            public void run() {
                stats.print();
            }
        }, 1000, 1000);
        try {
            logger.fine("AppleTrailers Plugin: Download trailer for " + movie.getBaseName());
            URL url = new URL(trailerUrl);
            stats = WebStats.make(url);
            HttpURLConnection connection = (HttpURLConnection) (url.openConnection());
            InputStream inputStream = connection.getInputStream();
            int code = connection.getResponseCode();
            if (code != HttpURLConnection.HTTP_OK) {
                logger.severe("AppleTrailers Plugin: Download Failed");
                return false;
            }
            OutputStream out = new FileOutputStream(trailerFile);
            byte buf[] = new byte[1024 * 1024];
            int len;
            while ((len = inputStream.read(buf)) > 0) {
                out.write(buf, 0, len);
                stats.bytes(len);
            }
            out.close();
            return true;
        } catch (Exception e) {
            logger.severe("AppleTrailers Plugin: Download Exception");
            return false;
        } finally {
            stats.print();
            System.out.print("\n");
            timer.cancel();
        }
    }
}
