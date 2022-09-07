class BackupThread extends Thread {
        public void flip() {
            byte[] rawData = null;
            if (outputList.size() == 1) {
                rawData = outputList.firstElement();
            } else {
                int offSet = 0;
                rawData = new byte[totalBytes];
                for (byte[] byteArr : outputList) {
                    System.arraycopy(byteArr, 0, rawData, offSet, byteArr.length);
                    offSet += byteArr.length;
                }
            }
            if (fmt.isBigEndian()) {
                switchEndian(rawData, 0, rawData.length);
                fmt = new javax.sound.sampled.AudioFormat(fmt.getEncoding(), fmt.getSampleRate(), fmt.getSampleSizeInBits(), fmt.getChannels(), fmt.getFrameSize(), fmt.getFrameRate(), false);
            }
            duration = (long) (totalBytes * 1000 / (fmt.getSampleSizeInBits() / 8 * fmt.getSampleRate()));
            if (fmt.getSampleRate() != 8000) {
                double originalFrequency = fmt.getSampleRate();
                double targetFrequency = 8000;
                double targetDX = 1 / targetFrequency;
                double originalDX = 1 / originalFrequency;
                int byteCount = fmt.getSampleSizeInBits() / 8;
                int originalSampleCount = totalBytes / byteCount;
                int[] originalSignal = new int[originalSampleCount];
                int j = 0;
                for (int i = 0; i < originalSignal.length; i++) {
                    originalSignal[i] = (rawData[j++] & 0xff) | (rawData[j++] << 8);
                }
                double ratio = fmt.getSampleRate() / 8000;
                int count = (int) (originalSignal.length / ratio);
                int[] resampledSignal = new int[count];
                for (int k = 0; k < count; k++) {
                    double xk = (double) targetDX * k;
                    int i = (int) (xk / originalDX);
                    double tang = (originalSignal[i + 1] - originalSignal[i]) / (originalDX);
                    double resampled = originalSignal[i] + (xk - i * originalDX) * tang;
                    resampledSignal[k] = (int) resampled;
                }
                rawData = new byte[resampledSignal.length * 2];
                j = 0;
                for (int i = 0; i < resampledSignal.length; i++) {
                    rawData[j++] = (byte) (resampledSignal[i]);
                    rawData[j++] = (byte) (resampledSignal[i] >> 8);
                }
                javax.sound.sampled.AudioFormat targetFormat = new javax.sound.sampled.AudioFormat(fmt.getEncoding(), 8000, fmt.getSampleSizeInBits(), fmt.getChannels(), fmt.getFrameSize(), 8000, fmt.isBigEndian());
            }
            InputStream is = new ByteArrayInputStream(rawData);
            stream = new AudioInputStream(is, fmt, rawData.length / fmt.getFrameSize());
        }
}
