class BackupThread extends Thread {
    private void createRunPage() {
        createHeader();
        writer.println("<h1>Crawler Servlet</h1>");
        if (!manager.areRunning()) {
            manager.startAllCrawler();
            writer.println("<p>Crawler jobs have been startet. Look at the log file for further information.</p>");
        } else {
            writer.println("<p>Crawler are currently allready runnung.</p>");
        }
        createFooter();
    }
}
