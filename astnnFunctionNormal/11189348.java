class BackupThread extends Thread {
    private void install() {
        try {
            FileUtils.copyFiles(DEFAULT_HOME_DIR, USER_RAPTOR_DIR);
            if (!new File(preferences.getString(APP_PGN_FILE)).exists()) {
                new File(preferences.getString(APP_PGN_FILE)).createNewFile();
            }
        } catch (IOException ioe) {
            throw new RuntimeException(ioe);
        }
    }
}
