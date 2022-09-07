class BackupThread extends Thread {
    public Image5DWindow(Image5D imp, Image5DCanvas ic) {
        super(imp, ic);
        if (ic == null) {
            throw new IllegalArgumentException("Image5DCanvas must not be null.");
        }
        i5d = (Image5D) imp;
        if (imp.getNDimensions() != nDimensions) {
            throw new IllegalArgumentException("Wrong number of dimensions.");
        }
        scrollbarsWL = new ScrollbarWithLabel[nDimensions];
        positions = new int[nDimensions];
        dimensions = i5d.getDimensions();
        remove(sliceSelector);
        remove(ic);
        setLayout(new Image5DLayout(ic));
        displayMode = ChannelControl.ONE_CHANNEL_COLOR;
        add(ic, Image5DLayout.CANVAS);
        for (int i = 1; i <= i5d.getNChannels(); i++) {
            channelCanvasses.add(new Image5DCanvas(i5d.getChannelImagePlus(i)));
            i5d.getChannelImagePlus(i).setWindow(this);
        }
        channelControl = new ChannelControl(this);
        add(channelControl, Image5DLayout.CHANNEL_SELECTOR);
        scrollbarsWL[3] = new ScrollbarWithLabel(Scrollbar.HORIZONTAL, 1, 1, 1, dimensions[3] + 1, imp.getDimensionLabel(3));
        if (i5d.getNSlices() > 1) {
            add(scrollbarsWL[3], Image5DLayout.SLICE_SELECTOR);
        }
        scrollbarsWL[4] = new ScrollbarWithLabel(Scrollbar.HORIZONTAL, 1, 1, 1, dimensions[4] + 1, imp.getDimensionLabel(4));
        if (i5d.getNFrames() > 1) {
            add(scrollbarsWL[4], Image5DLayout.FRAME_SELECTOR);
        }
        for (int i = 3; i < nDimensions; ++i) {
            scrollbarsWL[i].addAdjustmentListener(this);
            scrollbarsWL[i].setFocusable(false);
            int blockIncrement = dimensions[i] / 10;
            if (blockIncrement < 1) blockIncrement = 1;
            scrollbarsWL[i].setUnitIncrement(1);
            scrollbarsWL[i].setBlockIncrement(blockIncrement);
        }
        sliceSelector = scrollbarsWL[3].getScrollbar();
        setDisplayGrayInTiles(i5d.isDisplayGrayInTiles());
        setDisplayMode(i5d.getDisplayMode());
        pack();
        isInitialized = true;
        updateSliceSelector();
        i5d.updateAndRepaintWindow();
        i5d.updateImageAndDraw();
        done = true;
        thread.interrupt();
        while (thread.isAlive()) {
        }
        done = false;
        thread = new Thread(this, "SliceSelector");
        thread.start();
        ImageJ ij = IJ.getInstance();
        removeKeyListener(ij);
        ic.removeKeyListener(ij);
        for (int i = 0; i < i5d.getNChannels(); i++) {
            ((Image5DCanvas) channelCanvasses.get(i)).removeKeyListener(ij);
        }
        addKeyListener(this);
        ic.addKeyListener(this);
        for (int i = 0; i < i5d.getNChannels(); i++) {
            ((Image5DCanvas) channelCanvasses.get(i)).addKeyListener(this);
        }
        scrollbarsWL[3].addKeyListener(this);
        scrollbarsWL[4].addKeyListener(this);
        addKeyListener(ij);
        ic.addKeyListener(ij);
        for (int i = 0; i < i5d.getNChannels(); i++) {
            ((Image5DCanvas) channelCanvasses.get(i)).addKeyListener(ij);
        }
        scrollbarsWL[3].addKeyListener(ij);
        scrollbarsWL[4].addKeyListener(ij);
        addKeyListener(this);
        ic.addKeyListener(this);
        for (int i = 0; i < i5d.getNChannels(); i++) {
            ((Image5DCanvas) channelCanvasses.get(i)).addKeyListener(this);
        }
        scrollbarsWL[3].addKeyListener(this);
        scrollbarsWL[4].addKeyListener(this);
        ij.addKeyListener(this);
        int nIJComponents = ij.getComponentCount();
        for (int i = 0; i < nIJComponents; i++) {
            ij.getComponent(i).addKeyListener(this);
        }
        ij.getProgressBar().addKeyListener(this);
    }
}
