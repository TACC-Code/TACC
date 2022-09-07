class BackupThread extends Thread {
    @Override
    protected URLConnection openConnection(URL u) throws IOException {
        String path = u.getHost() + "/" + u.getPath();
        ClassPathResource resource = new ClassPathResource(path);
        URL url = resource.getURL();
        return url == null ? null : url.openConnection();
    }
}
