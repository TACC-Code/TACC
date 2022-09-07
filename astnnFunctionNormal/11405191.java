class BackupThread extends Thread {
    private InputStream getResource(String resourceName) throws IOException {
        InputStream is = null;
        String baseName;
        if (resourceName.isEmpty()) {
            return null;
        }
        if (resourceName.startsWith("/")) {
            baseName = resourceName.substring(1);
        } else {
            baseName = resourceName;
            resourceName = "/" + resourceName;
        }
        try {
            File file = new File(".", baseName);
            URL url = file.toURI().toURL();
            is = url.openStream();
            if (is != null) {
                LOG.info("Found configuration " + url);
                return is;
            }
        } catch (Exception ignore) {
        }
        try {
            File file = new File(resourceName);
            URL url = file.toURI().toURL();
            is = url.openStream();
            if (is != null) {
                LOG.info("Found configuration " + url);
                return is;
            }
        } catch (Exception ignore) {
        }
        try {
            is = this.getClass().getResourceAsStream(resourceName);
            if (is != null) {
                LOG.info("Found configuration as resource " + resourceName);
                return is;
            }
        } catch (Exception ignore) {
        }
        throw new IOException("Cannot find " + resourceName);
    }
}
