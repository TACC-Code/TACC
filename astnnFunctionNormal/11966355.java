class BackupThread extends Thread {
        @Override
        public int processAudio(AudioBuffer chunk) {
            try {
                int sampCheck = 0;
                if (nible == null) {
                    fs = chunk.getSampleRate();
                    T = 1.0 / fs;
                    chunkSize = chunk.getSampleCount();
                    nible = new AudioBuffer("nibble", 1, chunkSize, fs);
                    dtChunk = chunkSize / fs;
                    nibBuff = nible.getChannel(0);
                }
                processPendingAudioEvents();
                float chn[] = chunk.getChannel(0);
                double nextAudioTime = getNextAudioTime();
                double dt = nextAudioTime - currentTime;
                double endOfChunkTime = currentTime + dtChunk;
                if (dt > dtChunk) {
                    client.processAudio(chunk);
                    currentTime += dtChunk;
                    sampCheck += chunkSize;
                } else {
                    int ptr = 0;
                    boolean done = false;
                    int nibSize1 = (int) (dt * fs);
                    while (true) {
                        assert (dt >= 0);
                        assert (nibSize1 > 0);
                        assert (nibSize1 < chunkSize);
                        nible.changeSampleCount(nibSize1, false);
                        nible.makeSilence();
                        client.processAudio(nible);
                        nibBuff = nible.getChannel(0);
                        for (int i = 0; i < nibSize1; i++, ptr++) {
                            chn[ptr] += nibBuff[i];
                        }
                        assert (nibBuff == nible.getChannel(0));
                        sampCheck += nibSize1;
                        if (done) {
                            currentTime = endOfChunkTime;
                            break;
                        }
                        currentTime = nextAudioTime;
                        processPendingAudioEvents();
                        nextAudioTime = getNextAudioTime();
                        if (nextAudioTime == Double.MAX_VALUE) {
                            nibSize1 = chunkSize - ptr;
                            assert (nibSize1 != 0);
                            done = true;
                        } else {
                            dt = nextAudioTime - currentTime;
                            nibSize1 = (int) (dt * fs);
                            if (ptr + nibSize1 >= chunkSize) {
                                nibSize1 = chunkSize - ptr;
                                assert (nibSize1 >= 0);
                                done = true;
                            }
                        }
                    }
                }
                if (chunk.getChannelCount() == 2) {
                    System.arraycopy(chn, 0, chunk.getChannel(1), 0, chunkSize);
                }
                cccc++;
                assert (Math.abs(currentTime - cccc * dtChunk) < T);
            } catch (AssertionError e) {
                e.printStackTrace();
            }
            return AUDIO_OK;
        }
}
