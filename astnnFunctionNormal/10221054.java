class BackupThread extends Thread {
        private static java.io.File downloadTempLocally(URL url) throws IOException {
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            if (connection.getResponseCode() == 200) {
                java.io.File temp = java.io.File.createTempFile("urlToVfs", "tmp");
                FileOutputStream out = new FileOutputStream(temp);
                DataInputStream in = new DataInputStream(connection.getInputStream());
                int len;
                byte ch[] = new byte[1024];
                while ((len = in.read(ch)) != -1) {
                    out.write(ch, 0, len);
                }
                connection.disconnect();
                return temp;
            }
            return null;
        }
}
