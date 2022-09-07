class BackupThread extends Thread {
    public SaveDatabase(String dbName) {
        try {
            String source = MySeriesOptions._USER_DIR_ + Paths.DATABASES_PATH + dbName;
            String dest = MySeriesOptions._USER_DIR_ + Paths.DATABASES_PATH + dbName + Database.BACK_UP_EXT;
            MySeriesLogger.logger.log(Level.INFO, "Taking a backup of the database");
            if (MyUsefulFunctions.copyfile(source, dest)) {
                MySeriesLogger.logger.log(Level.INFO, "Database backed up!!!");
                MyMessages.message("Database backed up", "A back up of the older database was taken");
                backUp = true;
                return;
            } else {
            }
        } catch (FileNotFoundException ex) {
            MySeriesLogger.logger.log(Level.SEVERE, "Database file was not found", ex);
        } catch (IOException ex) {
            MySeriesLogger.logger.log(Level.SEVERE, "Could not read/write to database", ex);
        }
    }
}
