class BackupThread extends Thread {
    void openCrawler(URL url) {
        try {
            setCrawler(loadCrawler(Access.getAccess().openConnection(url).getInputStream()));
            currentFilename = "";
        } catch (Exception e) {
            PopupDialog.warn(workbenchPanel, "Error", e.toString());
        }
    }
}
