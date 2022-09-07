class BackupThread extends Thread {
    public static byte[] cryptPassword(byte[] password) {
        if (MESSAGE_DIGEST == null) logger.fatal("You must call the configure method before calling cryptPassword.");
        byte ret[] = MESSAGE_DIGEST.digest(password);
        MESSAGE_DIGEST.reset();
        return ret;
    }
}
