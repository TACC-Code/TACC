class BackupThread extends Thread {
    private String readString(InputStream input) throws IOException, EOFException {
        int length = EndianUtils.readSwappedUnsignedShort(input);
        length *= 2;
        byte[] data = new byte[length];
        int read = input.read(data);
        if (read != length) throw new EOFException("Unexpected end of file");
        for (int i = 0; i < length; i += 2) {
            byte x = data[i];
            data[i] = data[i + 1];
            data[i + 1] = x;
        }
        return new String(data, "UTF-16");
    }
}
