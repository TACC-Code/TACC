class BackupThread extends Thread {
    HaptekOutputLink(String host, int port, String animationDir) throws Exception {
        try {
            log.info("Opening Connection to '" + host + ":" + port + "'");
            this.socket = new Socket(host, port);
            log.finer("Creating buffered reader & writers");
            this.input = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            this.output = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
            this.inputStrings = new ArrayList<String>();
            this.animationDir = animationDir;
            loadVoiceMap();
        } catch (Exception e) {
            log.severe("Error setting up connection: " + e);
            close();
        }
        log.info("Connection made..");
    }
}
