class BackupThread extends Thread {
    protected int writeConv(byte[] data, int ofs, int len) {
        double grad = (double) INPUT_RATE / format.getRate();
        int length = 0;
        if (format.getBits() == 8 && format.getChannels() == 1) length = mono8(data, ofs, len, grad); else if (format.getBits() == 8 && format.getChannels() == 2) length = stereo8(data, ofs, len, grad); else if (format.getBits() == 16 && format.getChannels() == 1) length = mono16(data, ofs, len, grad); else if (format.getBits() == 16 && format.getChannels() == 2) length = stereo16(data, ofs, len, grad);
        return length;
    }
}
