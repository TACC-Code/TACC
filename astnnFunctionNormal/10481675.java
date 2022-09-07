class BackupThread extends Thread {
    public Plot3D[] plotCorrectLUT() {
        Plot3D[] plots = new Plot3D[4];
        double[][][] correctLut = eliminator.correctLut;
        for (int x = 0; x < 3; x++) {
            RGBBase.Channel ch = RGBBase.Channel.getChannelByArrayIndex(x);
            plots[x] = plot3DLUT(ch, correctLut[x], ch.color, property, " Correct LUT", "Correct code");
        }
        double[][][] correctLut2 = chVIEliminator.correctLut;
        plots[3] = plot3DLUT(channelVI, correctLut2[0], Color.black, chVIIProperty, " Correct LUT", "Correct code");
        PlotUtils.arrange(plots, 2, 2);
        PlotUtils.setVisible(plots);
        return plots;
    }
}
