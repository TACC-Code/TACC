class BackupThread extends Thread {
    @Override
    protected boolean onEncode(Buffer buffer) {
        boolean ret = doEncode(buffer);
        if (!ret) {
            return false;
        }
        BufferHelper.writeVarInt(buffer, headers.size());
        for (KeyValuePair<String, String> kv : headers) {
            BufferHelper.writeVarString(buffer, kv.getName());
            BufferHelper.writeVarString(buffer, kv.getValue());
        }
        BufferHelper.writeVarInt(buffer, content.readableBytes());
        int idx = content.getReadIndex();
        buffer.write(content, content.readableBytes());
        content.setReadIndex(idx);
        return false;
    }
}
