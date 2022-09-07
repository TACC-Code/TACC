class BackupThread extends Thread {
    public void read(String resourceName, IInputStreamUser user) {
        try {
            InputStream in = null;
            try {
                URL url = getClass().getClassLoader().getResource(resourceName);
                if (url == null) {
                    throw new IllegalStateException("Could not find resource '" + resourceName + "'");
                }
                in = url.openStream();
                user.use(in);
            } finally {
                if (in != null) {
                    in.close();
                }
            }
        } catch (IOException e) {
            throw new IllegalStateException("Could not read resource '" + resourceName + "' because: " + e.getMessage(), e);
        }
    }
}
