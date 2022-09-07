class BackupThread extends Thread {
    protected void blockUpdate(byte[] buf, int pos, int len) {
        internalDigest.reset();
        internalDigest.update((byte) 0);
        internalDigest.update(buf, pos, len);
        if ((len == 0) && (nodes.size() > 0)) return;
        byte[] digest = internalDigest.digest();
        push(digest);
    }
}
