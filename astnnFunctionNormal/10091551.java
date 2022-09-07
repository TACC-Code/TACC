class BackupThread extends Thread {
    public AbstractPreferenceAction(String text, int style, ReportEditor reportEditor, GraphicalViewer viewer) {
        super(text, style);
        this.reportEditor = reportEditor;
        this.viewer = viewer;
    }
}
