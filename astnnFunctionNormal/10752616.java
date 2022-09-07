class BackupThread extends Thread {
    public void run() {
        try {
            HttpURLConnection huc = (HttpURLConnection) url.openConnection();
            huc.setConnectTimeout(1000);
            huc.setReadTimeout(1000);
            int code = huc.getResponseCode();
            if (uu != null) uu.isAlive(true);
        } catch (Exception e) {
            if (uu != null) uu.isAlive(false);
        }
    }
}
