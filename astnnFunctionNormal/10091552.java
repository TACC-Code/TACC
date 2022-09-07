class BackupThread extends Thread {
    public AbstractPreferenceAction(String text, ReportEditor reportEditor, GraphicalViewer viewer) {
        super(text);
        this.reportEditor = reportEditor;
        this.viewer = viewer;
    }
}
