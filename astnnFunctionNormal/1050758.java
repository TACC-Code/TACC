class BackupThread extends Thread {
    public void run() {
        URL url = null;
        try {
            url = new URL(infoURL);
        } catch (Exception e) {
            error("infoURL is not valid: " + e);
            infoURL = null;
            return;
        }
        while (url != null && infoThread != null) {
            try {
                BufferedReader content = new BufferedReader(new InputStreamReader(url.openStream()));
                try {
                    String line;
                    while ((line = content.readLine()) != null) {
                        if (line.startsWith("#")) {
                            String color = line.substring(1, 7);
                            line = line.substring(8);
                            host.setForeground(Color.decode("#" + color));
                        }
                        host.setText(line);
                        infoThread.sleep(10 * interval);
                    }
                } catch (IOException e) {
                    error("error while loading info ...");
                }
                infoThread.sleep(100 * interval);
            } catch (Exception e) {
                error("error retrieving info content: " + e);
                e.printStackTrace();
                host.setForeground(Color.red);
                host.setText("error retrieving info content");
                infoURL = null;
                return;
            }
        }
    }
}
