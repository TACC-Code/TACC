class BackupThread extends Thread {
    public AbstractPreferenceAction(String text, ImageDescriptor image, ReportEditor reportEditor, GraphicalViewer viewer) {
        super(text, image);
        this.reportEditor = reportEditor;
        this.viewer = viewer;
    }
}
