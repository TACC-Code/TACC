class BackupThread extends Thread {
    private void setStandardChannelNames() {
        int channels = getNumberOfChannels();
        if (channels == 1) {
            getChannel(0).setName(GLanguage.translate("mono"));
        } else if (channels == 2) {
            getChannel(0).setName(GLanguage.translate("left"));
            getChannel(1).setName(GLanguage.translate("right"));
        }
    }
}
