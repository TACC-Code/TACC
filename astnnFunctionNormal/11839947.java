class BackupThread extends Thread {
    public static void consistentNewlines(final File dir) throws IOException {
        for (final File f : dir.listFiles()) {
            if (f.isDirectory()) {
                if (!".svn".equals(f.getName())) {
                    consistentNewlines(f);
                }
            } else {
                FileUtils.writeLines(f, FileUtils.readLines(f));
            }
        }
    }
}
