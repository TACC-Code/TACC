class BackupThread extends Thread {
    public static File getResource(String resource) throws IOException, URISyntaxException {
        final URL resourceURL = TestHelper.class.getResource(resource);
        final File orig = new File(resourceURL.toURI());
        final File copy = File.createTempFile(orig.getName(), "");
        FileUtils.copyFile(orig, copy);
        return copy;
    }
}
