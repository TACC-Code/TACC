class BackupThread extends Thread {
    private static Buffer loadWAV(URL file) {
        InputStream in = null;
        Buffer[] tmp = null;
        try {
            in = new BufferedInputStream(file.openStream());
            ByteArrayOutputStream byteOut = new ByteArrayOutputStream(1024 * 256);
            byteOut.reset();
            byte copyBuffer[] = new byte[1024 * 4];
            WavInputStream wavInput = new WavInputStream(in);
            boolean done = false;
            int bytesRead = -1;
            int length = 0;
            while (!done) {
                bytesRead = wavInput.read(copyBuffer, 0, copyBuffer.length);
                byteOut.write(copyBuffer, 0, bytesRead);
                done = (bytesRead != copyBuffer.length || bytesRead < 0);
            }
            ByteBuffer data = BufferUtils.createByteBuffer(byteOut.size());
            data.put(byteOut.toByteArray());
            data.rewind();
            if (ByteOrder.nativeOrder() == ByteOrder.BIG_ENDIAN) {
                ShortBuffer tmp2 = data.duplicate().order(ByteOrder.LITTLE_ENDIAN).asShortBuffer();
                while (tmp2.hasRemaining()) data.putShort(tmp2.get());
                data.rewind();
            }
            int channels = wavInput.channels();
            tmp = Buffer.generateBuffers(1);
            float time = (byteOut.size()) / (float) (wavInput.rate() * channels * 2);
            LoggingSystem.getLogger().log(Level.INFO, "Wav estimated time " + time + "  rate: " + wavInput.rate() + "  channels: " + channels + "  depth: " + wavInput.depth());
            tmp[0].configure(data, getChannels(wavInput), wavInput.rate(), time);
            data.clear();
            data = null;
            wavInput.close();
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
        return tmp[0];
    }
}
