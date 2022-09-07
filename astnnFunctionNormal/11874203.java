class BackupThread extends Thread {
    public static Bundle installBundle(BundleContext bc, String resource) throws BundleException {
        try {
            System.out.println("installBundle(" + resource + ")");
            URL url = bc.getBundle().getResource(resource);
            if (url == null) {
                throw new BundleException("No resource " + resource);
            }
            InputStream in = url.openStream();
            if (in == null) {
                throw new BundleException("No resource " + resource);
            }
            return bc.installBundle("internal:" + resource, in);
        } catch (IOException e) {
            throw new BundleException("Failed to get input stream for " + resource + ": " + e);
        }
    }
}
