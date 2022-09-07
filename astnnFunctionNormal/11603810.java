class BackupThread extends Thread {
    public CharStream createLexerInput() {
        try {
            return new ANTLRInputStream(url.openStream());
        } catch (IOException e) {
            throw new InputTypeNotSupportedException("Could not load url %s.", e, url);
        }
    }
}
