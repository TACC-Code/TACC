class BackupThread extends Thread {
    public int read(int[] buffer, int off, int len) {
        int written = 0;
        try {
            while (len > 0) {
                if (samplesLeft == 0) {
                    Header h = bitstream.readFrame();
                    if (h == null) {
                        if (written == 0) return -1;
                        return written;
                    }
                    SampleBuffer output = (SampleBuffer) decoder.decodeFrame(h, bitstream);
                    samples = output.getBuffer();
                    samplePos = 0;
                    channels = output.getChannelCount();
                    pitch = (int) (((float) output.getSampleFrequency() / device.getSampleRate()) * 65536);
                    samplesLeft = ((output.getBufferLength() / channels) << 16) / pitch;
                    bitstream.closeFrame();
                }
                int read = Math.min(samplesLeft, len);
                for (int i = 0; i < read; i++) {
                    int realPos = (samplePos >> 16) * channels;
                    int l = clamp(samples[realPos]) & 0xffff;
                    int r = l;
                    if (channels == 2) r = clamp(samples[realPos + 1]) & 0xffff;
                    samplePos += pitch;
                    buffer[off + i] = l | r << 16;
                }
                off += read;
                len -= read;
                samplesLeft -= read;
                written += read;
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return written;
    }
}
