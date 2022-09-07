class BackupThread extends Thread {
    public InputSource resolveEntity(String publicId, String systemId) {
        String resolved = null;
        if (systemId != null && systemMap.containsKey(systemId)) {
            resolved = (String) systemMap.get(systemId);
        } else if (publicId != null && publicMap.containsKey(publicId)) {
            resolved = (String) publicMap.get(publicId);
        }
        if (resolved != null) {
            try {
                InputSource iSource = new InputSource(resolved);
                iSource.setPublicId(publicId);
                URL url = new URL(resolved);
                InputStream iStream = url.openStream();
                iSource.setByteStream(iStream);
                return iSource;
            } catch (Exception e) {
                return null;
            }
        }
        return null;
    }
}
