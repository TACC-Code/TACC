class BackupThread extends Thread {
    public JFreeChart createChartFromFile(String file) {
        JFreeChart chart = null;
        URL url = getClass().getResource(file);
        try {
            InputStream in = url.openStream();
            chart = createChart(in);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return chart;
    }
}
