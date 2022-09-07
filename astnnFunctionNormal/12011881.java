class BackupThread extends Thread {
    public synchronized void save() {
        if (!isSaveRequired) {
            NLogger.debug(Preferences.class, "No saving of preferences required.");
            return;
        }
        NLogger.debug(Preferences.class, "Saving preferences to: " + prefFile.getAbsolutePath());
        Properties saveProperties = new SortedProperties();
        for (Setting setting : settingMap.values()) {
            if (setting.isDefault()) {
                continue;
            }
            PreferencesFactory.serializeSetting(setting, saveProperties);
        }
        File bakFile = new File(prefFile.getParentFile(), prefFile.getName() + ".bak");
        try {
            if (prefFile.exists()) {
                FileUtils.copyFile(prefFile, bakFile);
            }
            OutputStream os = null;
            try {
                os = new BufferedOutputStream(new FileOutputStream(prefFile));
                saveProperties.store(os, "Phex Preferences");
            } finally {
                IOUtil.closeQuietly(os);
            }
        } catch (IOException exp) {
            NLogger.error(NLoggerNames.GLOBAL, exp, exp);
        }
    }
}
