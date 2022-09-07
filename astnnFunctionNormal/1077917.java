class BackupThread extends Thread {
    public static boolean isInternetReachable() {
        try {
            int i = action.test_connectivity.length;
            String test = action.test_connectivity[utils.math.RandomInteger(0, i - 1)];
            debug("Testing connection to " + test);
            URL url = new URL(test);
            HttpURLConnection urlConnect = (HttpURLConnection) url.openConnection();
            Object objData = urlConnect.getContent();
            objData.toString();
        } catch (UnknownHostException e) {
            return false;
        } catch (IOException e) {
            return false;
        }
        return true;
    }
}
