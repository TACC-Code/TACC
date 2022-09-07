class BackupThread extends Thread {
    public String getString() throws SQLException {
        if (data == null) {
            if (reader != null) {
                CharArrayWriter charArrayWriter = new CharArrayWriter();
                writeClob(chunkSize, reader, charArrayWriter);
                data = charArrayWriter.toString();
            } else if (inputStream != null) {
                ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
                writeClob(chunkSize, inputStream, outputStream);
                if (charsetName == null) {
                    data = new String(outputStream.toByteArray());
                } else {
                    try {
                        data = new String(outputStream.toByteArray(), charsetName);
                    } catch (UnsupportedEncodingException e) {
                        throw new OdalRuntimePersistencyException("Unsupported charset [" + charsetName + "]", e);
                    }
                }
            }
        }
        return data;
    }
}
