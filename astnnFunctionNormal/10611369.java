class BackupThread extends Thread {
    protected String getSeabirdStatus() throws IOException {
        String cmd = "ds";
        writeToBird.write("\n\r");
        writeToBird.flush();
        logger.finest("Clear line:" + readFromBird.readLine());
        writeToBird.write(cmd, 0, cmd.length());
        writeToBird.write("\n\r");
        writeToBird.flush();
        logger.finest("Wrote command:" + cmd);
        StringBuffer readBuffer = new StringBuffer();
        String lineRead = null;
        String lastLineString = "output salinity = yes, output sound velocity = no";
        boolean lastLine = false;
        while (!lastLine && ((lineRead = readFromBird.readLine()) != null)) {
            readBuffer.append(lineRead);
            readBuffer.append("\n");
            lastLine = lineRead.matches(lastLineString) || lineRead.matches("S>time out");
        }
        if (writeFile) {
            fileOut.write(readBuffer.toString().getBytes());
        }
        return readBuffer.toString();
    }
}
