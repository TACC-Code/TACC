class BackupThread extends Thread {
    protected synchronized void writeConfig() {
        Thread writer = new Thread("Config_Writer") {

            public void run() {
                synchronized (PreferenceMap.this) {
                    try {
                        FileWriter out = new FileWriter(dataFile);
                        for (String key : prefs.keySet()) {
                            String value = prefs.get(key);
                            out.write(key + "=" + value + "\r\n");
                        }
                        out.close();
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        };
        writer.setDaemon(false);
        writer.start();
    }
}
