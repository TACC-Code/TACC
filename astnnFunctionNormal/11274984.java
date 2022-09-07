class BackupThread extends Thread {
    private static Buffer loadOGG(URL file) {
        InputStream in = null;
        Buffer[] tmp = null;
        try {
            in = new BufferedInputStream(file.openStream());
            ByteArrayOutputStream byteOut = new ByteArrayOutputStream(1024 * 256);
            byteOut.reset();
            byte copyBuffer[] = new byte[1024 * 4];
            OggInputStream oggInput = new OggInputStream(in);
            boolean done = false;
            int bytesRead = -1;
            int length = 0;
            while (!done) {
                bytesRead = oggInput.read(copyBuffer, 0, copyBuffer.length);
                byteOut.write(copyBuffer, 0, bytesRead);
                done = (bytesRead != copyBuffer.length || bytesRead < 0);
            }
            ByteBuffer data = BufferUtils.createByteBuffer(byteOut.size());
            data.put(byteOut.toByteArray());
            data.rewind();
            int channels = oggInput.getInfo().channels;
            tmp = Buffer.generateBuffers(1);
            float time = (byteOut.size()) / (float) (oggInput.getInfo().rate * channels * 2);
            tmp[0].configure(data, getChannels(oggInput.getInfo()), oggInput.getInfo().rate, time);
            LoggingSystem.getLogger().log(Level.INFO, "Ogg estimated time " + time);
            LoggingSystem.getLogger().info("ogg loaded - time: " + time + "  channels: " + channels);
            data.clear();
            data = null;
            oggInput.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return tmp[0];
    }
}
