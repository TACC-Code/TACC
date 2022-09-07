class BackupThread extends Thread {
    private String getTrailerRealUrl(String trailerUrl) {
        try {
            URL url = new URL(trailerUrl);
            HttpURLConnection connection = (HttpURLConnection) (url.openConnection());
            InputStream inputStream = connection.getInputStream();
            byte buf[] = new byte[1024];
            int len;
            len = inputStream.read(buf);
            if (len == 1024) return trailerUrl;
            String mov = new String(buf);
            int pos = 44;
            String realUrl = "";
            while (mov.charAt(pos) != 0) {
                realUrl += mov.charAt(pos);
                pos++;
            }
            String absRealURL = getAbsUrl(trailerUrl, realUrl);
            return absRealURL;
        } catch (Exception e) {
            logger.severe("Error : " + e.getMessage());
            return Movie.UNKNOWN;
        }
    }
}
