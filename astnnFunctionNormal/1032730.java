class BackupThread extends Thread {
    byte[] getChannelData() {
        int offset = 5;
        int length = SSHInputStream.getInteger(offset, super._data);
        offset += 4;
        byte[] channel_data = new byte[length];
        System.arraycopy(super._data, offset, channel_data, 0, length);
        return channel_data;
    }
}
