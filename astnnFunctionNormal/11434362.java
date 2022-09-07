class BackupThread extends Thread {
    public static void main(String[] args) {
        LoggerContext lc = (LoggerContext) LoggerFactory.getILoggerFactory();
        lc.getLogger(LoggerContext.ROOT_NAME).setLevel(Level.toLevel("DEBUG"));
        log.debug("Available ports: {}", ExecDeviceIO.getAvailablePortList());
        ExecDeviceIO serial = new ExecDeviceIO("COM3");
        if (!serial.open()) return;
        InputStream in = serial.getInputStream();
        try {
            while (true) {
                byte[] buf = new byte[1];
                int nread = in.read(buf);
                if (nread < 0) break;
                System.out.write(buf, 0, nread);
            }
        } catch (Exception e) {
            log.error("Error: {}", e.toString());
        }
    }
}
