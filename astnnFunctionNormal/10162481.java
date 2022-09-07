class BackupThread extends Thread {
    protected void createTrayViewer(Composite parent) {
        trayViewer = new ScrollingGraphicalViewer();
        trayViewer.createControl(parent);
        final FigureCanvas canvas = (FigureCanvas) trayViewer.getControl();
        canvas.setScrollBarVisibility(FigureCanvas.NEVER);
        canvas.getViewport().setLayoutManager(new ViewportLayout() {

            @Override
            public void layout(IFigure figure) {
                Viewport viewport = (Viewport) figure;
                IFigure contents = viewport.getContents();
                if (contents == null) return;
                Point p = viewport.getClientArea().getLocation();
                p.translate(viewport.getViewLocation().getNegated());
                Dimension newSize = viewport.getClientArea().getSize();
                contents.setBounds(new Rectangle(p, newSize));
            }
        });
        trayViewer.setKeyHandler(new TrayKeyHandler(trayViewer));
        initializeTrayViewer();
    }
}
