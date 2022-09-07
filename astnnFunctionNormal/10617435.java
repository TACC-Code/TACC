class BackupThread extends Thread {
    public void paintMask(Graphics2D g2d) {
        getChannelModel().getMask().paintOntoClip(g2d, rectangle);
    }
}
