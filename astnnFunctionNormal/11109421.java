class BackupThread extends Thread {
    public final void digest(byte[] out, int off) {
        md.digest(tmp);
        md.update(k_xor_opad);
        md.update(tmp);
        md.digest(tmp);
        System.arraycopy(tmp, 0, out, off, size);
        md.update(k_xor_ipad);
    }
}
