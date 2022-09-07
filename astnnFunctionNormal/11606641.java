class BackupThread extends Thread {
    public void init(InputSource in, String name, InputEntity stack, boolean isPE) throws IOException, SAXException {
        input = in;
        this.isPE = isPE;
        reader = in.getCharacterStream();
        if (reader == null) {
            InputStream bytes = in.getByteStream();
            if (bytes == null) {
                String systemId = in.getSystemId();
                URL url;
                try {
                    url = new URL(systemId);
                } catch (MalformedURLException e) {
                    String urlString = convertToFileURL(systemId);
                    in.setSystemId(urlString);
                    url = new URL(urlString);
                }
                reader = XmlReader.createReader(url.openStream());
            } else if (in.getEncoding() != null) reader = XmlReader.createReader(in.getByteStream(), in.getEncoding()); else reader = XmlReader.createReader(in.getByteStream());
        }
        next = stack;
        buf = new char[BUFSIZ];
        this.name = name;
        checkRecursion(stack);
    }
}
