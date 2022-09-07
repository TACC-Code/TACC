class BackupThread extends Thread {
    public void run() {
        try {
            Header header = null;
            Decoder decoder = null;
            while ((audioPlayerThread != null) && ((header = bitstream.readFrame()) != null)) {
                if (decoder == null) {
                    decoder = new Decoder(header, bitstream);
                }
                SampleBuffer sampleBuffer = decoder.decodeFrame();
                if (!audioDevice.isOpened()) {
                    audioDevice.open(sampleBuffer.getSampleFrequency(), sampleBuffer.getChannelCount());
                }
                if (!readyToPlay && audioDevice.isReady()) {
                    synchronized (this) {
                        readyToPlay = true;
                        notifyAll();
                    }
                }
                if (sampleBuffer.size() > 0) {
                    audioDevice.write(sampleBuffer.getBuffer(), sampleBuffer.size());
                }
                bitstream.closeFrame();
            }
        } catch (InterruptedIOException ioex) {
        } catch (EOFException ex) {
        } catch (IOException ex) {
        } finally {
            decoding = false;
            readyToPlay = true;
            audioPlayerThread = null;
        }
    }
}
