class BackupThread extends Thread {
            public void run() {
                try {
                    if (reportingActivity) return;
                    reportingActivity = true;
                    URL url = new URL(url_string);
                    HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                    connection.getResponseMessage();
                    connection.disconnect();
                    Settings.getDefault().setSetting("rachota.reported.week", "" + currentWeek);
                    analyticsView.updateChart();
                    reportingActivity = false;
                } catch (Exception e) {
                    System.out.println("Error: Can't connect to Rachota Analytics server.");
                    Settings.getDefault().setSetting("rachota.reported.week", "-1");
                    reportingActivity = false;
                }
            }
}
