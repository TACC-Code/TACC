class BackupThread extends Thread {
    private void readURL(String yyyy, String[] ignore, URL url, PrintWriter out) throws IOException {
        HttpURLConnection con = (HttpURLConnection) url.openConnection();
        InputStream in = con.getInputStream();
        if (con.getResponseCode() != HttpURLConnection.HTTP_OK) throw new IOException(con.getResponseMessage());
        readPage(yyyy, ignore, in, out);
        con.disconnect();
    }
}
