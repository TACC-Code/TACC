class BackupThread extends Thread {
    public void checkForUpdate() {
        URL url;
        try {
            url = new URL("http://pixelace.sourceforge.net/updateCheck_PixelAce.html");
            HttpURLConnection urlConn = (HttpURLConnection) url.openConnection();
            urlConn.connect();
            InputStream in = urlConn.getInputStream();
            BufferedReader reader = new BufferedReader(new InputStreamReader(in));
            String version = reader.readLine();
            String type = reader.readLine();
            if (isProgramUpToDate(version)) {
                if (!onlyShowMessageWhenUpdateAvailable) {
                    JOptionPane.showMessageDialog(parentFrame, "Program is up to date.");
                }
            } else {
                if (type.trim().equalsIgnoreCase("critical")) {
                    int result = JOptionPane.showConfirmDialog(parentFrame, "There is a critical update available.\n Would you like to download the update now?", "Critical update available", JOptionPane.YES_NO_CANCEL_OPTION);
                    if (result == 0) {
                        downloadUpdate();
                    }
                } else {
                    int result = JOptionPane.showConfirmDialog(parentFrame, "There is an update available.\n Would you like to download the update now?", "Update available", JOptionPane.YES_NO_CANCEL_OPTION);
                    if (result == 0) {
                        downloadUpdate();
                    }
                }
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(parentFrame, "Problem checking for update.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}
