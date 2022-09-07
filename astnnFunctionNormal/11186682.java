class BackupThread extends Thread {
    public int saveDocument(final AbstractDocument doc, String sFileName) {
        assert doc != null;
        int r = BaseDlg.OK;
        final ServiceApp app = ServiceFactory.getInstance();
        String sCurName = doc.getLocalPath();
        if (sCurName.equals(sFileName) == false) {
            File f = new File(sFileName);
            if (f.exists()) {
                r = app.readUsrYesNoCancel(sFileName + " already exists.\n\nOverwrite existing file?");
                if (r == BaseDlg.CANCEL) {
                } else if (r == BaseDlg.NO) {
                    sFileName = ProxyUtil.showSaveDocFileChooser(ProxyUtil.getUserDocsDir(), sFileName);
                    if (sFileName != null) {
                        r = BaseDlg.OK;
                    } else {
                    }
                } else {
                    r = BaseDlg.OK;
                }
            }
        }
        if (r == BaseDlg.OK) {
            doc.setLocalPath(sFileName);
            doc.setRemotePath(ProxyUtil.getRemotePath(sFileName));
            app.setWaitCursor();
            r = ProxyUtil.saveLocalSyncObj(new SyncResource(doc.getLocalPath(), doc.getRemotePath(), doc));
            if (r == BaseDlg.OK) {
                doc.setModified(false);
            }
            app.setDefCursor();
            pushToUpload(new SyncClass(SyncType.SYNC_OUT_DOC, doc));
            syncUpload();
        }
        return r;
    }
}
