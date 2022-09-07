class BackupThread extends Thread {
    protected Serializer(StreamResult result, OutputConfig outputConfig) throws IllegalArgumentException, IOException, UnsupportedEncodingException {
        this.outputConfig = outputConfig;
        if (!outputConfig.cdata_section_elements.isEmpty()) {
            throw new IllegalArgumentException("cdata_section_elements is not supported");
        }
        this.systemId = result.getSystemId();
        OutputStream os = result.getOutputStream();
        Writer w = result.getWriter();
        if (os != null) {
            out = new XMLCharacterEncoder(os, outputConfig.encoding);
            doClose = false;
        } else if (w != null) {
            out = new XMLCharacterEncoder(w);
            doClose = false;
        } else if (systemId != null) {
            OutputStream _os;
            try {
                URI uri = new URI(systemId);
                if (!uri.isAbsolute()) {
                    File file = new File(systemId);
                    _os = new FileOutputStream(file);
                } else if (uri.getScheme().equals("file")) {
                    File file = new File(uri);
                    _os = new FileOutputStream(file);
                } else {
                    URL url = uri.toURL();
                    URLConnection urlConn = url.openConnection();
                    urlConn.setDoOutput(true);
                    _os = urlConn.getOutputStream();
                }
            } catch (URISyntaxException e) {
                throw new IllegalArgumentException("Invalid systemId: " + e.getMessage());
            }
            os = new BufferedOutputStream(_os);
            out = new XMLCharacterEncoder(os, outputConfig.encoding);
            doClose = true;
        } else {
            throw new IllegalArgumentException("Empty StreamResult");
        }
    }
}
