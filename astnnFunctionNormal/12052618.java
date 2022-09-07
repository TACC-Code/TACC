class BackupThread extends Thread {
    public void close() throws IOException {
        synchronized (lock) {
            if (enc == null) throw new IOException("writer already closed");
            byteBuffer.clear();
            charBuffer.flip();
            CoderResult res = enc.encode(charBuffer, byteBuffer, true);
            if (res.isError() || res.isMalformed() || res.isUnmappable()) res.throwException();
            writeBuffer();
            byteBuffer.clear();
            res = enc.flush(byteBuffer);
            if (res.isError() || res.isMalformed() || res.isUnmappable()) res.throwException();
            writeBuffer();
            enc = null;
        }
    }
}
