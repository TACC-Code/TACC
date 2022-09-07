class BackupThread extends Thread {
    private void visitJar(URL url) throws IOException {
        JarURLConnection conn = (JarURLConnection) url.openConnection();
        _visitJar(conn.getJarFile());
    }
}
