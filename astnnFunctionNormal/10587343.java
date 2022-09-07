class BackupThread extends Thread {
    public static void open(final URL url) {
        try {
            InputStream stream = (isLocalFile(url) ? url.openStream() : getInputStream(url.openStream()));
            TGSong song = TGFileFormatManager.instance().getLoader().load(TuxGuitar.instance().getSongManager().getFactory(), stream);
            TuxGuitar.instance().fireNewSong(song, url);
        } catch (Throwable throwable) {
            TuxGuitar.instance().newSong();
            MessageDialog.errorMessage(new TGFileFormatException(TuxGuitar.getProperty("file.open.error", new String[] { url.toString() }), throwable));
        }
    }
}
