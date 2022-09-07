class BackupThread extends Thread {
    public static boolean hasInternetConnection(String address) {
        BufferedReader in = null;
        MySeriesLogger.logger.log(Level.INFO, "Checking internet availability : {0} ", address);
        try {
            URL url = new URL(address);
            in = new BufferedReader(new InputStreamReader(url.openStream()));
            MySeriesLogger.logger.log(Level.FINE, "Internet connection established");
            return true;
        } catch (IOException ex) {
            MySeriesLogger.logger.log(Level.INFO, "No internet connection");
            return false;
        }
    }
}
