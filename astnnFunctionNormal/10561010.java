class BackupThread extends Thread {
    private void onUpdate() {
        AOHistogram h = new AOHistogram();
        ALayerSelection ls = getFocussedClip().getSelectedLayer().getSelection();
        ls.operateEachChannel(h);
        AClip c = histogramClipEditor.getClip();
        AChannel ch = c.getLayer(0).getChannel(0);
        ch.setSamples(h.getHistogram());
        ch.markChange();
        try {
            c.getHistory().store(loadIcon(), GLanguage.translate(getName()));
        } catch (NullPointerException npe) {
        }
        c.getPlotter().autoScale();
        histogramClipEditor.reload();
    }
}
