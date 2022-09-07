class BackupThread extends Thread {
    public static void handle(IOException ex) {
        ex.printStackTrace();
        showError("I/O Error", "RoboResearch could not read or write data to a required resource");
    }
}
