class BackupThread extends Thread {
    public void downmixPCM() {
        int lsum = 0;
        int rsum = 0;
        int numsamps = 0;
        int lDC = 0;
        int rDC = 0;
        short lsample;
        short rsample;
        int readpos = 0;
        if (myBitsPerSample == 16) {
            if (myNumberOfChannels == 2) {
                LittleEndianByteBuffer storeBuffer = new LittleEndianByteBuffer(myStoreBuffer);
                while (readpos < (myNumBytesWritten / 2)) {
                    lsample = storeBuffer.getShort(readpos++);
                    rsample = storeBuffer.getShort(readpos++);
                    lsum += lsample;
                    rsum += rsample;
                    numsamps++;
                }
                lDC = -(lsum / numsamps);
                rDC = -(rsum / numsamps);
                readpos = 0;
                while (readpos < (myNumBytesWritten / 2)) {
                    storeBuffer.setShort(readpos, (short) (storeBuffer.getShort(readpos) + lDC));
                    readpos++;
                    storeBuffer.setShort(readpos, (short) (storeBuffer.getShort(readpos) + rDC));
                    readpos++;
                }
            } else {
                LittleEndianByteBuffer storeBuffer = new LittleEndianByteBuffer(myStoreBuffer);
                while (readpos < myNumBytesWritten / 2) {
                    lsample = storeBuffer.getShort(readpos++);
                    lsum += lsample;
                    numsamps++;
                }
                lDC = -(lsum / numsamps);
                readpos = 0;
                while (readpos < myNumBytesWritten / 2) {
                    storeBuffer.setShort(readpos, (short) (storeBuffer.getShort(readpos) + lDC));
                    readpos++;
                }
            }
        } else {
            if (myNumberOfChannels == 2) {
                while (readpos < (myNumBytesWritten)) {
                    lsample = myStoreBuffer[readpos++];
                    rsample = myStoreBuffer[readpos++];
                    lsum += lsample;
                    rsum += rsample;
                    numsamps++;
                }
                lDC = -(lsum / numsamps);
                rDC = -(rsum / numsamps);
                readpos = 0;
                while (readpos < (myNumBytesWritten)) {
                    myStoreBuffer[readpos] = (short) (myStoreBuffer[readpos] + lDC);
                    readpos++;
                    myStoreBuffer[readpos] = (short) (myStoreBuffer[readpos] + rDC);
                    readpos++;
                }
            } else {
                while (readpos < myNumBytesWritten) {
                    lsample = myStoreBuffer[readpos++];
                    lsum += lsample;
                    numsamps++;
                }
                lDC = -(lsum / numsamps);
                readpos = 0;
                while (readpos < myNumBytesWritten) {
                    myStoreBuffer[readpos] = (short) (myStoreBuffer[readpos] + lDC);
                    readpos++;
                }
            }
        }
        if (myDownmixBuffer == null) {
            myDownmixBuffer = new short[NUM_SAMPLES_NEEDED];
        }
        myDownmixSize = myNumBytesWritten;
        if (mySamplesPerSecond != 11025) {
            myDownmixSize = (int) ((float) myDownmixSize * (11025.0 / (float) mySamplesPerSecond));
        }
        if (myBitsPerSample == 16) {
            myDownmixSize /= 2;
        }
        if (myNumberOfChannels != 1) {
            myDownmixSize /= 2;
        }
        int maxwrite = myDownmixSize;
        int writepos = 0;
        float rateChange = mySamplesPerSecond / 11025.0f;
        if (myBitsPerSample == 8) {
            LittleEndianByteBuffer tempbuf = new LittleEndianByteBuffer(new short[myNumBytesWritten]);
            readpos = 0;
            while (readpos < myNumBytesWritten) {
                int samp = myStoreBuffer[readpos];
                samp = (samp - 128) * 256;
                if (samp >= Short.MAX_VALUE) {
                    samp = Short.MAX_VALUE;
                } else if (samp <= Short.MIN_VALUE) {
                    samp = Short.MIN_VALUE;
                }
                tempbuf.setShort(readpos, (short) samp);
                readpos++;
            }
            myNumBytesWritten *= 2;
            myStoreBuffer = tempbuf.getByteBuffer();
            myBitsPerSample = 16;
        }
        if (myNumberOfChannels == 2) {
            LittleEndianByteBuffer tempbuf = new LittleEndianByteBuffer(new short[myNumBytesWritten / 2]);
            LittleEndianByteBuffer storeBuffer = new LittleEndianByteBuffer(myStoreBuffer);
            readpos = 0;
            writepos = 0;
            while (writepos < myNumBytesWritten / 4) {
                long ls = storeBuffer.getShort(readpos++);
                long rs = storeBuffer.getShort(readpos++);
                tempbuf.setShort(writepos, (short) ((ls + rs) / 2));
                writepos++;
            }
            myNumBytesWritten /= 2;
            myStoreBuffer = tempbuf.getByteBuffer();
        }
        LittleEndianByteBuffer storeBuffer = new LittleEndianByteBuffer(myStoreBuffer);
        writepos = 0;
        while ((writepos < maxwrite) && (myNumSamplesWritten < NUM_SAMPLES_NEEDED)) {
            readpos = (int) ((float) writepos * rateChange);
            short ls = storeBuffer.getShort(readpos++);
            myDownmixBuffer[myNumSamplesWritten] = ls;
            myNumSamplesWritten++;
            writepos++;
        }
        myStoreBuffer = null;
    }
}
