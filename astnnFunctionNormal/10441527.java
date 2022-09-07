class BackupThread extends Thread {
    public String getContent(URL url) {
        StringBuffer content = new StringBuffer();
        try {
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestProperty("domain", "www.peppuseksi.fi");
            connection.setRequestProperty("User-Agent", "Robot/Spider");
            connection.setRequestProperty("pathinfo", "/test2");
            connection.setRequestProperty("locale", "fi_FI");
            connection.setRequestProperty("themes", "porno");
            connection.setDoInput(true);
            InputStream is = connection.getInputStream();
            byte[] buffer = new byte[2048];
            int count;
            while (-1 != (count = is.read(buffer))) {
                content.append(new String(buffer, 0, count));
            }
        } catch (IOException e) {
            return null;
        }
        return content.toString();
    }
}
