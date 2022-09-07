class BackupThread extends Thread {
    public void parse(URL url, Object initialValue) {
        if (url == null) {
            throw new IllegalArgumentException("The source to the XML must not be null");
        }
        try {
            InputStream in = url.openStream();
            try {
                InputSource inputSource = new InputSource(in);
                parse(inputSource, initialValue);
            } finally {
                in.close();
            }
        } catch (IOException e) {
            throw new LocalizableException(StringCodes.STRING_XML_IO_ERROR, e, url, e.getLocalizedMessage());
        }
    }
}
