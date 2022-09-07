class BackupThread extends Thread {
    public Dictionary getManifest() throws BundleException {
        if (manifest == null) {
            synchronized (this) {
                if (manifest == null) {
                    URL url = getEntry(Constants.OSGI_BUNDLE_MANIFEST);
                    if (url == null) {
                        throw new BundleException(NLS.bind(AdaptorMsg.MANIFEST_NOT_FOUND_EXCEPTION, Constants.OSGI_BUNDLE_MANIFEST, getLocation()));
                    }
                    try {
                        manifest = Headers.parseManifest(url.openStream());
                    } catch (IOException e) {
                        throw new BundleException(NLS.bind(AdaptorMsg.MANIFEST_NOT_FOUND_EXCEPTION, Constants.OSGI_BUNDLE_MANIFEST, getLocation()), e);
                    }
                }
            }
        }
        return manifest;
    }
}
