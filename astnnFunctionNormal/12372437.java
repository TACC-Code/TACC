class BackupThread extends Thread {
        public void run() {
            logger.debug("Starting thread.");
            while (isConnected()) {
                len = 0;
                try {
                    len = eibReader.read(cbuf);
                    if (len > 32) {
                        logger.debug("unallowed data block length (" + len + ")");
                    } else if (len > 0) {
                        logger.debug("BCU1 receiving data.");
                        if (cbuf[0] == 0x49 || cbuf[0] == 0x4e || cbuf[0] == 0x4b) {
                            int eibdata[] = new int[len + 1];
                            int apci;
                            if (cbuf[0] == 0x4e || cbuf[0] == 0x4b) {
                                for (int i = len - 1; i >= 0; i--) {
                                    con[i] = (byte) cbuf[i];
                                }
                            }
                            if (cbuf[0] == 0x49 || cbuf[0] == 0x4e) {
                                for (int i = 0; i < len - 1; i++) {
                                    eibdata[i] = cbuf[i + 1];
                                }
                                frames.addElement(eibdata);
                                String s = "";
                                for (int i = 0; i < len; i++) {
                                    s += Integer.toHexString(cbuf[i]) + " ";
                                }
                                logger.debug("unexpected data block received (" + s + ")");
                            }
                        } else {
                            String s = "";
                            for (int i = 0; i < len; i++) {
                                s += Integer.toHexString(cbuf[i]) + " ";
                            }
                            logger.debug("unexpected data block received (" + s + ")");
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    System.out.println("cdd");
                }
            }
        }
}
