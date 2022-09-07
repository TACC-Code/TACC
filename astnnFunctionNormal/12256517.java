class BackupThread extends Thread {
    private String getGzipUrlStringContents(String url) throws IOException {
        return getInputStreamStringContents(new GZIPInputStream(new URL(url).openStream()));
    }
}
