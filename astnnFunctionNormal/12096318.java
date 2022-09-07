class BackupThread extends Thread {
    public Reader openStream(String publicID, String systemID) throws MalformedURLException, FileNotFoundException, IOException {
        URL url = new URL(this.currentSystemID, systemID);
        StringBuffer charsRead = new StringBuffer();
        Reader reader = this.stream2reader(url.openStream(), charsRead);
        if (charsRead.length() == 0) {
            return reader;
        }
        String charsReadStr = charsRead.toString();
        PushbackReader pbreader = new PushbackReader(reader, charsReadStr.length());
        for (int i = charsReadStr.length() - 1; i >= 0; i--) {
            pbreader.unread(charsReadStr.charAt(i));
        }
        return pbreader;
    }
}
