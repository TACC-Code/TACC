class BackupThread extends Thread {
    public PropertyBoxParserImpl(String... customProperties) {
        InputStream is = new BufferedInputStream(getClass().getResourceAsStream("/isoparser-default.properties"));
        try {
            mapping = new Properties();
            try {
                mapping.load(is);
                Enumeration<URL> enumeration = Thread.currentThread().getContextClassLoader().getResources("isoparser-custom.properties");
                while (enumeration.hasMoreElements()) {
                    URL url = enumeration.nextElement();
                    InputStream customIS = new BufferedInputStream(url.openStream());
                    try {
                        mapping.load(customIS);
                    } finally {
                        customIS.close();
                    }
                }
                for (String customProperty : customProperties) {
                    mapping.load(new BufferedInputStream(getClass().getResourceAsStream(customProperty)));
                }
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        } finally {
            try {
                is.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
