class BackupThread extends Thread {
    public void wideCopySpectrogramSelection(GEditableArea orig) {
        for (int i = 0; i < getNumberOfChannels(); i++) {
            AChannel ch = getChannel(i);
            GPSpectrogramSelect.Cookie c = ((GPSpectrogramSelect.Cookie) ch.getCookies().getCookie("spectrogramSelect"));
            if (c != null) {
                c.area.copy(orig);
            }
        }
    }
}
