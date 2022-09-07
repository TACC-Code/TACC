class BackupThread extends Thread {
    public boolean parse(final URL url) throws IOException {
        InputStream stream = null;
        int length = -1;
        try {
            final URLConnection connection = url.openConnection();
            stream = connection.getInputStream();
            length = connection.getContentLength();
        } catch (IOException e) {
            if (stream != null) {
                stream.close();
            }
        }
        itsStream = new BufferedInputStream(stream);
        try {
            for (int i = 0; i < COMMENT_SIZE; i++) {
                itsStream.read();
            }
            itsNumOfObjects = 1;
            itsNumOfFacets = new int[] { LittleEndianConverter.read4ByteBlock(itsStream) };
            itsNames = new String[1];
            if (length != -1) {
                if (length != itsNumOfFacets[0] * RECORD_SIZE + HEADER_SIZE) {
                    throw new IOException("File size does not match.");
                }
            }
        } catch (IOException e) {
            close();
            throw e;
        }
        return false;
    }
}
