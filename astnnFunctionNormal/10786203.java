class BackupThread extends Thread {
    public byte[] getBinaryResource(String resourceName, InputStream resource) {
        if (resourceName == null) throw new IllegalArgumentException("null resourceName");
        if (resource == null) throw new IllegalArgumentException("null resource");
        byte[] cached = files.get(resourceName);
        if (cached != null) return cached;
        byte[] toReturn;
        try {
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            int read = 0;
            byte[] buffer = new byte[resource.available()];
            while ((resource.available() > 0) && (read > -1)) {
                if (resource.available() > buffer.length) buffer = new byte[resource.available()];
                read = resource.read(buffer);
                out.write(buffer, 0, read);
            }
            toReturn = out.toByteArray();
        } catch (IOException exception) {
            Log.out("IOException reading resource '" + resource + "' - " + exception.getMessage());
            return null;
        }
        if (caching) files.put(resourceName, toReturn);
        return toReturn;
    }
}
