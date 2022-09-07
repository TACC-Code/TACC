class BackupThread extends Thread {
    private void addEmlFile() throws IOException {
        setState(STATE.METADATA);
        FileUtils.copyFile(dataDir.resourceEmlFile(resource.getShortname(), null), new File(dwcaFolder, "eml.xml"));
        archive.setMetadataLocation("eml.xml");
        addMessage(Level.INFO, "EML file added");
    }
}
