class BackupThread extends Thread {
    private String getChannelHandle(MixedAsciiInputStream in, String streamId) throws IOException {
        final String HANDLE_PREFIX = "handle: ";
        String line;
        do {
            line = in.nextLine();
            log.info(String.format("line (%s) received: %s", streamId, line));
        } while (!line.startsWith(HANDLE_PREFIX));
        return line.substring(HANDLE_PREFIX.length());
    }
}
