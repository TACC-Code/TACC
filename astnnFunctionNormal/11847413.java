class BackupThread extends Thread {
    private void download_file(String url, String filename) throws Exception {
        URLConnection connection;
        OutputStream fstream;
        InputStream istream;
        byte[] buffer;
        int size;
        System.out.println("Downloading " + url + "...");
        fstream = new FileOutputStream(filename);
        connection = new URL(url).openConnection();
        connection.setDoInput(true);
        connection.setUseCaches(false);
        istream = connection.getInputStream();
        buffer = new byte[8192];
        while ((size = istream.read(buffer)) > 0) {
            fstream.write(buffer, 0, size);
        }
        istream.close();
        fstream.close();
    }
}
