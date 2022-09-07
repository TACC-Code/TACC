class BackupThread extends Thread {
    public static Manifest getManifestOfJar(String a_filename) throws Exception {
        URL url = new URL("jar:file:" + a_filename + "!/");
        JarURLConnection jarConnection = (JarURLConnection) url.openConnection();
        Manifest manifest = jarConnection.getManifest();
        jarConnection.getJarFile().close();
        return manifest;
    }
}
