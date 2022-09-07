class BackupThread extends Thread {
    public static Collection getEntries(URL url) throws ZipException, IOException {
        JarURLConnection conn = (JarURLConnection) url.openConnection();
        return getEntries(conn.getJarFile());
    }
}
