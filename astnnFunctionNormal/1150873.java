class BackupThread extends Thread {
    @VisibleForTesting
    protected static boolean checkDasherDomain(String domain) throws IOException {
        String endpoint = DASHER_XRDS_URL_PREFIX + domain;
        URL url = new URL(endpoint);
        String contentType = ((HttpURLConnection) url.openConnection()).getContentType().toLowerCase();
        logger.fine("retrieve xrds, return contentType='" + contentType + "'");
        return contentType.contains(XRDS_MIME_TYPE);
    }
}
