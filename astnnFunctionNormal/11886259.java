class BackupThread extends Thread {
    @Override
    public int read(ByteBuffer dest) throws IOException {
        ensureOpen();
        if (this.xin != null) return this.xin.read(dest); else if (this.fin != null) return this.fin.getChannel().read(dest); else return super.read(dest);
    }
}
