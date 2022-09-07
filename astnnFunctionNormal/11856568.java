class BackupThread extends Thread {
    public static String computeHash(File file, HashType type) throws IOException, InterruptedException {
        Hash hash = type.newHash();
        InputStream in = new FileInputStream(file);
        try {
            byte[] buffer = new byte[32 * 1024];
            int len = 0;
            while ((len = in.read(buffer)) >= 0) {
                hash.update(buffer, 0, len);
                if (Thread.interrupted()) throw new InterruptedException();
            }
        } finally {
            in.close();
        }
        return hash.digest();
    }
}
