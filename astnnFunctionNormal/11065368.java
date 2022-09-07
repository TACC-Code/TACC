class BackupThread extends Thread {
    public synchronized AbstractPolicy readPolicy(URL url) throws ParsingException {
        try {
            return readPolicy(url.openStream());
        } catch (IOException ioe) {
            throw new ParsingException("Failed to resolve the URL: " + url.toString(), ioe);
        }
    }
}
