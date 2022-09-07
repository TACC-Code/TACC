class BackupThread extends Thread {
    public InputStream getInputStream(String path) {
        URL url = getJarStream();
        try {
            JarInputStream jar = new JarInputStream(url.openStream());
            try {
                return loadStream(jar, path);
            } finally {
                jar.close();
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
