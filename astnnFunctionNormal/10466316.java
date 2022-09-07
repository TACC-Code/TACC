class BackupThread extends Thread {
    public ALayer(ALayer l) {
        super();
        type = l.type;
        plotType = SAMPLE_CURVE_TYPE;
        selection = new ALayerSelection(this);
        plotter = new ALayerPlotter(this);
        getPlotter().setColor(l.getPlotter().getColor());
        this.name = GLanguage.translate("copyOf") + " " + l.name;
        for (int i = 0; i < l.getNumberOfChannels(); i++) {
            add(new AChannel(l.getChannel(i)));
        }
    }
}
