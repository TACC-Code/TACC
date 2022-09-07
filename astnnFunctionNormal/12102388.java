class BackupThread extends Thread {
    public static long lastModified(URL url, String libfile) throws Exception {
        if (url.getProtocol().equals("jar")) {
            return ((JarURLConnection) url.openConnection()).getJarFile().getEntry(libfile).getTime();
        } else {
            return url.openConnection().getLastModified();
        }
    }
}
