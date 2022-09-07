class BackupThread extends Thread {
    private static void copyFile(URL url, File destFile) throws IOException {
        InputStream in = url.openStream();
        FileOutputStream out = new FileOutputStream(destFile);
        byte[] buffer = new byte[4096];
        int no = 0;
        try {
            while ((no = in.read(buffer)) != -1) out.write(buffer, 0, no);
        } finally {
            in.close();
            out.close();
        }
    }
}
