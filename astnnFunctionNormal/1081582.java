class BackupThread extends Thread {
            public int processAudio(AudioBuffer buffer) {
                if (buffer == null) return 0;
                if (streamBuffer == null || streamBuffer.length != buffer.getSampleCount() * 8) {
                    ByteBuffer bytebuffer = ByteBuffer.allocate(buffer.getSampleCount() * 8).order(ByteOrder.nativeOrder());
                    streamBuffer = bytebuffer.array();
                    floatArray = new float[buffer.getSampleCount() * 2];
                    floatBuffer = bytebuffer.asFloatBuffer();
                }
                try {
                    ais.read(streamBuffer, 0, buffer.getSampleCount() * 8);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                floatBuffer.position(0);
                floatBuffer.get(floatArray);
                float[] left = buffer.getChannel(0);
                float[] right = buffer.getChannel(1);
                for (int n = 0; n < buffer.getSampleCount() * 2; n += 2) {
                    left[n / 2] = floatArray[n];
                    right[n / 2] = floatArray[n + 1];
                }
                return AUDIO_OK;
            }
}
