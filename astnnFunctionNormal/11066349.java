class BackupThread extends Thread {
    public static Manifest getManifest() throws IOException, FileNotFoundException {
        URL url = OpEnvironmentManager.class.getResource("");
        System.out.println("Looking for Manifest at: " + url);
        Manifest mf = null;
        URLConnection conn = url.openConnection();
        if (conn instanceof JarURLConnection) {
            JarURLConnection jconn = (JarURLConnection) conn;
            mf = jconn.getManifest();
        } else {
            File mfFile = new File("META-INF/MANIFEST.MF");
            if (mfFile.exists()) {
                FileInputStream in = new FileInputStream(mfFile);
                mf = new Manifest(in);
                in.close();
            }
        }
        if (mf == null) {
            System.out.println("Manifest NOT found at: " + url);
        }
        return mf;
    }
}
