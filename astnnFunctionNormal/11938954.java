class BackupThread extends Thread {
    private boolean overwriteDatabase() {
        File db = new File(MySeriesOptions._USER_DIR_ + Paths.DATABASES_PATH + name + Database.EXT);
        if (db.isFile()) {
            MySeriesLogger.logger.log(Level.INFO, "Overwrite database?");
            return MyMessages.confirm("File Exists", "File already exists.\nOverwrite it?", true) == JOptionPane.NO_OPTION ? true : false;
        } else {
            return false;
        }
    }
}
