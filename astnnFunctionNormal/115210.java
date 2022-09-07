class BackupThread extends Thread {
    public void create(String[] args, int off) throws IOException {
        if (uid == null || uid.length() == 0) {
            uid = UIDGenerator.getInstance().createUID();
        }
        File rootDir = dirFile.getParentFile();
        if (rootDir != null && !rootDir.exists()) {
            rootDir.mkdirs();
        }
        DirWriter writer = fact.newDirWriter(dirFile, uid, id, readMeFile, readMeCharset, encodeParam());
        try {
            build(writer, args, off);
        } finally {
            writer.close();
        }
    }
}
