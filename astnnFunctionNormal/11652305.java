class BackupThread extends Thread {
    @Override
    public void gather(Object arg) throws IOException {
        if (this.isReReadOnUpdate()) {
            is.close();
            is = null;
            is = url.openStream();
        }
        data = new char[getDataSize()];
        int read = -1;
        int currentPos = 0;
        while (is.available() > 0 && (read = is.read()) != -1) {
            char c = (char) read;
            if (currentPos == getDataSize()) {
                notifyListeners(new String(data, 0, currentPos));
                currentPos = 0;
                data = null;
                data = new char[getDataSize()];
                data[currentPos++] = c;
            } else {
                data[currentPos++] = c;
            }
        }
        if (data != null && data.length > 0) {
            notifyListeners(new String(data, 0, currentPos));
        }
    }
}
