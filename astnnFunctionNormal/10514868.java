class BackupThread extends Thread {
    public static void download(String url, String dest) {
        try {
            URL source = new URL(url);
            InputStream in = new BufferedInputStream(source.openStream());
            OutputStream out = new BufferedOutputStream(new FileOutputStream(dest));
            int readbyte = in.read();
            while (readbyte >= 0) {
                out.write(readbyte);
                readbyte = in.read();
            }
            in.close();
            out.close();
        } catch (Exception e) {
            System.out.println("Impossibile scaricare: " + url);
        }
    }
}
