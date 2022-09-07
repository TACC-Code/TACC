class BackupThread extends Thread {
    public void connect() throws IOException {
        if (!connected) {
            String file = getURL().getFile();
            int p = file.indexOf('!');
            String url = file.substring(0, p);
            String entryName = file.substring(p + 1);
            if (url.startsWith("/")) url = url.substring(1);
            if (entryName.startsWith("/")) entryName = entryName.substring(1);
            stream = openStream(new URL(url), entryName);
            connected = true;
        }
    }
}
