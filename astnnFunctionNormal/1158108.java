class BackupThread extends Thread {
    private long establishLastModified(Resource resource) {
        if (resource == null) return -1;
        long lastModified;
        try {
            URLConnection urlc = resource.getURL().openConnection();
            urlc.setDoInput(false);
            urlc.setDoOutput(false);
            lastModified = urlc.getLastModified();
        } catch (FileNotFoundException fnfe) {
            lastModified = -1;
        } catch (IOException e) {
            lastModified = -1;
        }
        return lastModified;
    }
}
