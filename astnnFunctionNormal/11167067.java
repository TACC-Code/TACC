class BackupThread extends Thread {
    public static Reader makeReader(InputSource source, EncodingSnifferIF sniffer) throws IOException {
        Reader reader = source.getCharacterStream();
        if (reader == null) {
            if (source.getByteStream() != null) reader = makeReader(source.getByteStream(), source.getEncoding(), sniffer); else {
                URL url = new URL(source.getSystemId());
                reader = makeReader(url.openStream(), null, sniffer);
            }
        }
        return reader;
    }
}
