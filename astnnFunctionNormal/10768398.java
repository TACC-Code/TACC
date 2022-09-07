class BackupThread extends Thread {
    public static void copyResourceFile(Configuration configuration, String resourcefile, boolean overwrite) {
        String destdir = configuration.destDirName;
        String destresourcesdir = destdir + "resources";
        DirectoryManager.createDirectory(configuration, destresourcesdir);
        File destfile = new File(destresourcesdir, resourcefile);
        if (destfile.exists() && (!overwrite)) return;
        try {
            InputStream in = Configuration.class.getResourceAsStream("resources/" + resourcefile);
            if (in == null) return;
            OutputStream out = new FileOutputStream(destfile);
            byte[] buf = new byte[2048];
            int n;
            while ((n = in.read(buf)) > 0) out.write(buf, 0, n);
            in.close();
            out.close();
        } catch (Throwable t) {
        }
    }
}
