class BackupThread extends Thread {
    public void run() {
        try {
            while (activate) {
                if (in.available() > 0) {
                    int numReads = in.read(byteBuffer);
                    buffer.append(new String(byteBuffer, 0, numReads));
                    int idx = buffer.indexOf("</IFX>");
                    if (idx >= 0) {
                        connector.onMessage(new IFXPackager().unpack(buffer.substring(0, idx + 6)));
                        idx = buffer.indexOf("<", idx + 7);
                        if (idx >= 0) buffer = new StringBuffer(buffer.substring(idx)); else buffer = new StringBuffer();
                    }
                }
            }
            logger.info(connector.getChannelName() + "| Reader IFX detenido.|");
        } catch (IOException e) {
            logger.error(connector.getChannelName() + "| Problema de lectura, " + e.getMessage() + "|");
        }
        notify();
    }
}
