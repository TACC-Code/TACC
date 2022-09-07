class BackupThread extends Thread {
    public static byte[] pseudoUniqueSecureByteSequence16() {
        byte[] bytes = pseudoUniqueByteSequence16();
        synchronized (md) {
            return md.digest(bytes);
        }
    }
}
