class BackupThread extends Thread {
    private void loadConfigFile() {
        try {
            final URL url = this.context.getResource("/WEB-INF/config/transform.conf.xml");
            final URLConnection uc = url.openConnection();
            final InputStream in = uc.getInputStream();
            final long lastModified = uc.getLastModified();
            in.close();
            if (lastModified != this.lastModified) {
                this.lastModified = lastModified;
                final CShaniDomParser parser = new CShaniDomParser();
                final Document doc = parser.parse(url);
                final NodeList nl = doc.getElementsByTagNameNS("http://www.allcolor.org/xmlns/transform", "transformer");
                synchronized (this.transformerMap) {
                    this.transformerMap.clear();
                }
                for (int i = 0; i < nl.getLength(); i++) {
                    final Element eTransformer = (Element) nl.item(i);
                    try {
                        final IPipeTransformer transformer = (IPipeTransformer) this.getClass().getClassLoader().loadClass(eTransformer.getAttribute("class")).newInstance();
                        final String name = eTransformer.getAttribute("name");
                        synchronized (this.transformerMap) {
                            this.transformerMap.put(name, transformer);
                        }
                    } catch (final Exception ignore) {
                        ;
                    }
                }
            }
        } catch (final Exception ignore) {
            ;
        }
    }
}
