class BackupThread extends Thread {
    public String open(File file) throws IOException {
        mFile = file;
        this.stream = new FileOutputStream(file);
        this.channel = this.stream.getChannel();
        CharsetEncoder charSet = (mCharSet == null ? java.nio.charset.Charset.defaultCharset().newEncoder() : java.nio.charset.Charset.forName(mCharSet).newEncoder());
        ResourcePool.LogMessage(Thread.currentThread(), ResourcePool.INFO_MESSAGE, "Writing to file " + file.getAbsolutePath() + ", character set " + charSet.charset().displayName());
        os = java.nio.channels.Channels.newOutputStream(this.channel);
        bos = new BufferedOutputStream(os, miOutputBufferSize);
        zos = mZip ? new GZIPOutputStream(bos) : null;
        this.writer = new OutputStreamWriter(mZip ? zos : bos, charSet);
        return file.getAbsolutePath();
    }
}
