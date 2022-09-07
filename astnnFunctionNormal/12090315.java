class BackupThread extends Thread {
    public boolean isZIP(URL url) {
        boolean ok = false;
        try {
            InputStream stream = url.openStream();
            try {
                byte[] buff = new byte[4];
                ok = stream.read(buff) == 4;
                if (ok) {
                    ok = (buff[0] == 0x50 && buff[1] == 0x4b && buff[2] == 0x03 && buff[3] == 0x04);
                }
            } finally {
                stream.close();
            }
        } catch (IOException e) {
        }
        return ok;
    }
}
