class BackupThread extends Thread {
    public static final void printBuildInfo() {
        String revision = null;
        String date = null;
        URL url = Gbl.class.getResource("/revision.txt");
        if (url != null) {
            BufferedReader reader = null;
            try {
                reader = new BufferedReader(new InputStreamReader(url.openStream()));
                revision = reader.readLine();
                date = reader.readLine();
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                if (reader != null) {
                    try {
                        reader.close();
                    } catch (IOException e) {
                        log.warn("Could not close stream.", e);
                    }
                }
            }
            if (revision == null) {
                log.info("MATSim-Build: unknown");
            } else {
                log.info("MATSim-Build: " + revision + " (" + date + ")");
            }
        } else {
            log.info("MATSim-Build: unknown");
        }
    }
}
