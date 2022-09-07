class BackupThread extends Thread {
    public void run(String arg) {
        Dimension imgSize = new Dimension(688, 520);
        ImageStack stack = new ImageStack(imgSize.width, imgSize.height);
        for (int i = 0; i < 2; ++i) {
            for (int j = 0; j < 2; ++j) {
                for (int k = 0; k < 3; ++k) {
                    Polygon p = new Polygon(new int[] { 10 + j * 50, 100 + i * 100, 100 + k * 100, 10 }, new int[] { 10, 10, 200, 200 }, 4);
                    ByteProcessor proc = new ByteProcessor(imgSize.width, imgSize.height);
                    proc.setValue(127);
                    proc.fillPolygon(p);
                    stack.addSlice("ch" + k + " z" + j + " t" + i, proc);
                }
            }
        }
        Image5D img = new Image5D("test", stack, 3, 2, 2);
        img.setCurrentPosition(0, 0, 0, 0, 0);
        img.show();
        Image5DCanvas can = new Image5DCanvas(img.getChannelImagePlus(1));
        img.getWindow().add(can, Image5DLayout.CANVAS);
        img.getWindow().add(new Image5DCanvas(img.getChannelImagePlus(2)), Image5DLayout.CANVAS);
        img.getWindow().add(new Image5DCanvas(img.getChannelImagePlus(3)), Image5DLayout.CANVAS);
        img.getWindow().pack();
        IJ.wait(2000);
        img.getWindow().remove(can);
        img.getWindow().pack();
    }
}
