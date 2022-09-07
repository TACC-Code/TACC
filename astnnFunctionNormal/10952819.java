class BackupThread extends Thread {
    public void readFrom(InputStream in) throws IOException {
        if (type != SSH.MSG_CHANNEL_DATA && type != SSH.CMSG_STDIN_DATA) throw new IOException("Trying to read raw data into non-data PDU");
        PduByteArrayOutputStream bytes = (PduByteArrayOutputStream) out;
        readFromRawData = bytes.getBuf();
        readFromOff = bytes.size() + 4;
        readFromSize = in.read(readFromRawData, readFromOff, mtu - readFromOff);
        if (readFromSize == -1) throw new IOException("EOF");
        writeInt(readFromSize);
        bytes.setCount(readFromOff + readFromSize);
    }
}
