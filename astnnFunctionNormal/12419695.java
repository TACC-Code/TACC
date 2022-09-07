class BackupThread extends Thread {
    public void createPartControl(Composite parent) {
        Splitter splitter = new Splitter(parent, SWT.HORIZONTAL);
        createPaletteViewer(splitter);
        createGraphicalViewer(splitter);
        splitter.maintainSize(getPaletteViewer().getControl());
        splitter.setFixedSize(getInitialPaletteSize() - 1);
        splitter.setFixedSize(getInitialPaletteSize());
        splitter.addFixedSizeChangeListener(new PropertyChangeListener() {

            public void propertyChange(PropertyChangeEvent evt) {
                handlePaletteResized(((Splitter) evt.getSource()).getFixedSize());
            }
        });
    }
}
