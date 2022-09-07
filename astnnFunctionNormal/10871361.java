class BackupThread extends Thread {
    static boolean setupLocking(final Logger logger) {
        try {
            final File lockFile = new File(Global.LOCK_FILE);
            LogController.setUpClientLogging(Global.LOG_DIR);
            final FileChannel lockChannel = new RandomAccessFile(lockFile, "rw").getChannel();
            final FileLock lock = lockChannel.tryLock();
            if (lock == null) {
                lockChannel.close();
                final String message = "Another client is running on your computer.\n" + "You have to quit the other client.\n" + "This client will quit itself now.";
                if (GraphicsEnvironment.isHeadless()) {
                    System.err.println(message.replace("\n", " "));
                } else {
                    JOptionPane.showMessageDialog(null, message, "Error", JOptionPane.ERROR_MESSAGE);
                }
                return false;
            } else {
                Runtime.getRuntime().addShutdownHook(new Thread() {

                    @Override
                    public void run() {
                        try {
                            if (lock != null) {
                                lock.release();
                                lockChannel.close();
                                lockFile.delete();
                            }
                            logger.info("Client shuting down");
                        } catch (IOException ex) {
                            logger.severe(null, ex);
                        }
                    }
                });
                return true;
            }
        } catch (IOException ex) {
            logger.severe(null, ex);
            return true;
        }
    }
}
