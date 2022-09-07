class BackupThread extends Thread {
    public AsciiParser(URL url) throws GridBagException {
        try {
            InputStream instream = url.openStream();
            constraints = getLines(instream);
            instream.close();
        } catch (IOException ie1) {
            throw new GridBagException("Cannot read from source Reader.");
        }
    }
}
