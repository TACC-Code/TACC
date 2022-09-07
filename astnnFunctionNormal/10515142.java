class BackupThread extends Thread {
    protected void copySettings(final File checkpointDir) throws IOException {
        final List files = this.settingsHandler.getListOfAllFiles();
        boolean copiedSettingsDir = false;
        final File settingsDir = new File(this.disk, "settings");
        for (final Iterator i = files.iterator(); i.hasNext(); ) {
            File f = new File((String) i.next());
            if (f.getAbsolutePath().startsWith(settingsDir.getAbsolutePath())) {
                if (copiedSettingsDir) {
                    continue;
                }
                copiedSettingsDir = true;
                FileUtils.copyFiles(settingsDir, new File(checkpointDir, settingsDir.getName()));
                continue;
            }
            FileUtils.copyFiles(f, f.isDirectory() ? checkpointDir : new File(checkpointDir, f.getName()));
        }
    }
}
