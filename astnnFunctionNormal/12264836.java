class BackupThread extends Thread {
    private final void init() {
        InputStream in = null;
        try {
            final URL url = this.getClass().getClassLoader().getResource("padawan.conf.xml");
            if (url != null) {
                final URLConnection conn = url.openConnection();
                in = conn.getInputStream();
                final long newModified = conn.getLastModified();
                if ((this.lastModified != newModified) && (newModified != 0)) {
                    this.lastModified = newModified;
                    this.xmlDoc = CDocumentBuilderFactory.newParser().newDocumentBuilder().parse(in);
                    this.parseDocument(this.xmlDoc);
                }
            }
        } catch (final Exception ignore) {
            ignore.printStackTrace();
        } finally {
            try {
                in.close();
            } catch (final Exception e) {
                ;
            }
        }
    }
}
