class BackupThread extends Thread {
    public static void sync(File source, File dest) throws IOException {
        if (!dest.exists()) {
            dest.mkdirs();
        }
        File[] files = source.listFiles();
        for (File f : files) {
            if (f.getName().startsWith(".svn")) {
                continue;
            }
            if (f.isDirectory()) {
                sync(f, new File(dest, f.getName()));
            } else {
                FileUtils.copyFileToDirectory(f, dest);
            }
        }
    }
}
