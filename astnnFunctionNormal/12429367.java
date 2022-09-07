class BackupThread extends Thread {
    private void reloadZoomFrame() {
        AChannelPlotter chp = getFocussedClip().getSelectedLayer().getChannel(0).getPlotter();
        scaleX.setData(chp.getXLength());
        scaleY.setData(chp.getYLength());
        offsetX.setData(chp.getXOffset());
        offsetY.setData(chp.getYOffset());
    }
}
