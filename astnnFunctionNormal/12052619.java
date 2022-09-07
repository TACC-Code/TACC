class BackupThread extends Thread {
    public void write(char[] buf, int offset, int len) throws IOException {
        synchronized (lock) {
            if (enc == null) throw new IOException("writer already closed");
            int lastLen = -1;
            while (len > 0) {
                int allowed = Math.min(charBuffer.remaining(), len);
                charBuffer.put(buf, offset, allowed);
                offset += allowed;
                len -= allowed;
                charBuffer.flip();
                if (len == lastLen) {
                    if (len <= charBuffer.remaining()) {
                        charBuffer.put(buf, offset, len);
                        charBuffer.flip();
                    } else {
                        CharBuffer ncb = CharBuffer.allocate(charBuffer.length() + len);
                        ncb.put(charBuffer);
                        ncb.put(buf, offset, len);
                        charBuffer = ncb;
                    }
                    break;
                }
                lastLen = len;
                byteBuffer.clear();
                CoderResult res = enc.encode(charBuffer, byteBuffer, false);
                charBuffer.compact();
                if (res.isError() || res.isMalformed() || res.isUnmappable()) res.throwException();
                writeBuffer();
            }
        }
    }
}
