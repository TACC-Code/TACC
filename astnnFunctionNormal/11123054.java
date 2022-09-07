class BackupThread extends Thread {
    public void fixPaths() throws IOException, JDOMException {
        getEventListener().message("Fixing paths");
        if (!getXMLFile().getParentFile().equals(getFileBasePath())) {
            final File newFile = FileUtils.copyFileToDirectory(getXMLFile(), getFileBasePath());
            getXMLFile().delete();
            overwriteFiles(DocumentComponent.XML, newFile);
            createDocuments(DocumentComponent.XML, true);
        }
        if (!getNCXFile().getParentFile().equals(getFileBasePath())) {
            final File newFile = FileUtils.copyFileToDirectory(getNCXFile(), getFileBasePath());
            getNCXFile().delete();
            overwriteFiles(DocumentComponent.NCX, newFile);
            createDocuments(DocumentComponent.NCX, true);
        }
        if (!getSMILFile().getParentFile().equals(getFileBasePath())) {
            final File newFile = FileUtils.copyFileToDirectory(getSMILFile(), getFileBasePath());
            getSMILFile().delete();
            overwriteFiles(DocumentComponent.SMIL, getSMILFile());
            createDocuments(DocumentComponent.SMIL, true);
        }
    }
}
