class BackupThread extends Thread {
    private synchronized FileChannel open() throws FontFormatException {
        if (disposerRecord.channel == null) {
            if (FontUtilities.isLogging()) {
                FontUtilities.getLogger().info("open TTF: " + platName);
            }
            try {
                RandomAccessFile raf = (RandomAccessFile) java.security.AccessController.doPrivileged(new java.security.PrivilegedAction() {

                    public Object run() {
                        try {
                            return new RandomAccessFile(platName, "r");
                        } catch (FileNotFoundException ffne) {
                        }
                        return null;
                    }
                });
                disposerRecord.channel = raf.getChannel();
                fileSize = (int) disposerRecord.channel.size();
                FontManager fm = FontManagerFactory.getInstance();
                if (fm instanceof SunFontManager) {
                    ((SunFontManager) fm).addToPool(this);
                }
            } catch (NullPointerException e) {
                close();
                throw new FontFormatException(e.toString());
            } catch (ClosedChannelException e) {
                Thread.interrupted();
                close();
                open();
            } catch (IOException e) {
                close();
                throw new FontFormatException(e.toString());
            }
        }
        return disposerRecord.channel;
    }
}
