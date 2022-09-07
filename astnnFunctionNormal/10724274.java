class BackupThread extends Thread {
    public VLCChannel getChannel() {
        Object value = webBrowserObject.getObjectProperty("audio.channel");
        if (value == null) {
            return null;
        }
        switch(((Number) value).intValue()) {
            case 1:
                return VLCChannel.STEREO;
            case 2:
                return VLCChannel.REVERSE_STEREO;
            case 3:
                return VLCChannel.LEFT;
            case 4:
                return VLCChannel.RIGHT;
            case 5:
                return VLCChannel.DOLBY;
        }
        return null;
    }
}
