class BackupThread extends Thread {
    public HttpResponse execute(long startPosition, long endPosition) throws IOException {
        return execute(startPosition, endPosition, 5000);
    }
}
