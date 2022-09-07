class BackupThread extends Thread {
    protected SystemManager(File rootDir, boolean create) throws IOException {
        this.rootDir = rootDir;
        logger.info((create ? "creating " : "loading ") + "managed segments under " + rootDir.getName());
        logger.finer("segment root directory: " + rootDir);
        if (create) rootDir.mkdirs();
        if (!rootDir.isDirectory()) validator.fail("the specified root directory could not be " + (create ? "created: " : "found: ") + rootDir);
        if (create) {
            File versionFile = new File(rootDir, FileConventions.VERSION_FILE);
            validator.isTrue(!versionFile.exists(), FileConventions.VERSION_FILE + " already exists");
            FileChannel channel = new FileOutputStream(versionFile).getChannel();
            ByteBuffer buffer = ByteBuffer.wrap(FileConventions.VERSION_PREFIX.getBytes());
            channelHelper.writeRemaining(channel, buffer);
            buffer = ByteBuffer.allocate(8);
            buffer.putLong(VERSION.toLong()).flip();
            channelHelper.writeRemaining(channel, buffer);
            channel.close();
        }
        this.collector = new Collector(new File(rootDir, FileConventions.COMMITED_DIR), create);
        this.pendingDir = new File(rootDir, FileConventions.PENDING_DIR);
        if (create) {
            pendingDir.mkdir();
            validator.isTrue(pendingDir.isDirectory(), "failed to create pending directory");
            validator.isTrue(pendingDir.list().length == 0, "non-empty pending directory");
        } else validator.isTrue(pendingDir.isDirectory(), "pending directory not found");
    }
}
