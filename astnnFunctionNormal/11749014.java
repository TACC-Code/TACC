class BackupThread extends Thread {
    public static void downloadFromFtp(String user, String password, String folder, String file, String destination) {
        try {
            FileOutputStream out = new FileOutputStream(new File(destination));
            URL url = new URL(String.format(FTP_FORMAT, user, password, folder, file));
            URLConnection connection = url.openConnection();
            InputStream input = connection.getInputStream();
            int b = input.read();
            while (b != -1) out.write(b);
        } catch (Exception e) {
        }
    }
}
