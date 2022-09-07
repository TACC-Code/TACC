class BackupThread extends Thread {
    private void loadFile() {
        try {
            long size = fis.getChannel().size();
            if (size > Integer.MAX_VALUE) {
                throw new InvalidParameterException("Cannot handle file: too big !");
            }
            this.length = (int) size;
            this.buffer = new byte[(int) size];
            int cread = 0;
            cread += fis.read(buffer);
            while (cread < size) {
                cread += fis.read(buffer);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
