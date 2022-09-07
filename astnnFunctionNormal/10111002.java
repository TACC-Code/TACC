class BackupThread extends Thread {
    public static boolean redirectCheck(URL url) {
        boolean retVal = false;
        try {
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setInstanceFollowRedirects(false);
            if (connection.getResponseCode() == 302) {
                retVal = true;
            } else {
                retVal = false;
            }
            connection.disconnect();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return retVal;
    }
}
