class BackupThread extends Thread {
    private byte[] RGBAtoARGB(byte[] pixels) {
        for (int i = 0; i < pixels.length; i += BPP) {
            byte r = pixels[i];
            pixels[i] = pixels[i + 2];
            pixels[i + 2] = r;
        }
        return pixels;
    }
}
