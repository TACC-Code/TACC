class BackupThread extends Thread {
    public static boolean testDecoder() {
        boolean success = true;
        try {
            System.out.println("Positive tests that setting up a decoded stream works.");
            int[] bitsOK = { 8, 16, 24 };
            for (int channel = 1; channel <= 2; channel++) {
                for (int bit = 0; bit < bitsOK.length; bit++) {
                    AudioFormat srcFormat = new AudioFormat(org.kc7bfi.jflac.sound.spi.FlacEncoding.FLAC, 16000, bitsOK[bit], channel, -1, -1, false);
                    System.out.print("can convert 1: " + channel + "-channel, " + bitsOK[bit] + "-bit FLAC to PCM...");
                    if (!checkDirect(srcFormat, false)) {
                        success = false;
                    }
                    System.out.print("can convert 2: " + channel + "-channel, " + bitsOK[bit] + "-bit FLAC to PCM...");
                    if (!checkConversion(srcFormat, AudioFormat.Encoding.PCM_SIGNED, false)) {
                        success = false;
                    }
                }
            }
            System.out.println();
            System.out.println("Negative tests that the decoder does not claim to be able to convert non-supported formats.");
            int[] bitsCorrupt = { 0, 4, 10, 20, 32 };
            for (int channel = 2; channel <= 3; channel++) {
                for (int bit = 0; bit < bitsCorrupt.length; bit++) {
                    AudioFormat srcFormat = new AudioFormat(org.kc7bfi.jflac.sound.spi.FlacEncoding.FLAC, 16000, bitsCorrupt[bit], channel, -1, -1, false);
                    System.out.print("cannot convert 1: " + channel + "-channel, " + bitsCorrupt[bit] + "-bit FLAC to PCM...");
                    if (!checkDirect(srcFormat, true)) {
                        success = false;
                    }
                    System.out.print("cannot convert 2: " + channel + "-channel, " + bitsCorrupt[bit] + "-bit FLAC to PCM...");
                    if (!checkConversion(srcFormat, AudioFormat.Encoding.PCM_SIGNED, true)) {
                        success = false;
                    }
                }
            }
            int[] channelsCorrupt = { 0, 3, 5, 10 };
            for (int i = 0; i < channelsCorrupt.length; i++) {
                for (int bit = 16; bit < 40; bit += 16) {
                    AudioFormat srcFormat = new AudioFormat(org.kc7bfi.jflac.sound.spi.FlacEncoding.FLAC, 16000, bit, channelsCorrupt[i], -1, -1, false);
                    System.out.print("cannot convert 1: " + channelsCorrupt[i] + "-channel, " + bit + "-bit FLAC to PCM...");
                    if (!checkDirect(srcFormat, true)) {
                        success = false;
                    }
                    System.out.print("cannot convert 2: " + channelsCorrupt[i] + "-channel, " + bit + "-bit FLAC to PCM...");
                    if (!checkConversion(srcFormat, AudioFormat.Encoding.PCM_SIGNED, true)) {
                        success = false;
                    }
                }
            }
            System.out.println();
            System.out.println("Negative tests that the decoder does not claim to be able to convert bits, sample rate, or channels");
            float[] sampleRatesOK = { 16000, 22050, 44100, 96000 };
            for (int srcChannel = 1; srcChannel <= 2; srcChannel++) {
                for (int targetChannel = 1; targetChannel <= 2; targetChannel++) {
                    for (int srcBitIndex = 0; srcBitIndex < bitsOK.length; srcBitIndex++) {
                        for (int targetBitIndex = 0; targetBitIndex < bitsOK.length; targetBitIndex++) {
                            for (int srcSampleRateIndex = 0; srcSampleRateIndex < sampleRatesOK.length; srcSampleRateIndex++) {
                                for (int targetSampleRateIndex = 0; targetSampleRateIndex < sampleRatesOK.length; targetSampleRateIndex++) {
                                    int srcBit = bitsOK[srcBitIndex];
                                    int targetBit = bitsOK[targetBitIndex];
                                    float srcSampleRate = sampleRatesOK[srcSampleRateIndex];
                                    float targetSampleRate = sampleRatesOK[targetSampleRateIndex];
                                    if ((srcBit != targetBit) || (srcChannel != targetChannel) || (srcSampleRate != targetSampleRate)) {
                                        AudioFormat srcFormat = new AudioFormat(org.kc7bfi.jflac.sound.spi.FlacEncoding.FLAC, srcSampleRate, srcBit, srcChannel, -1, -1, false);
                                        AudioFormat targetFormat = new AudioFormat(targetSampleRate, targetBit, targetChannel, true, false);
                                        System.out.print("cannot convert: " + srcChannel + "-channel, " + srcBit + "-bit, " + srcSampleRate + "Hz FLAC to " + targetChannel + "-channel, " + targetBit + "-bit, " + targetSampleRate + "Hz PCM...");
                                        if (!checkConversion(srcFormat, targetFormat, true)) {
                                            success = false;
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
            System.out.println();
            System.out.println("Negative tests that the decoder does not claim to be able to decode to big endian");
            for (int srcChannel = 1; srcChannel <= 2; srcChannel++) {
                for (int srcBitIndex = 0; srcBitIndex < bitsOK.length; srcBitIndex++) {
                    int srcBit = bitsOK[srcBitIndex];
                    float srcSampleRate = 22050;
                    AudioFormat srcFormat = new AudioFormat(org.kc7bfi.jflac.sound.spi.FlacEncoding.FLAC, srcSampleRate, srcBit, srcChannel, -1, -1, false);
                    AudioFormat targetFormat = new AudioFormat(srcSampleRate, srcBit, srcChannel, true, true);
                    System.out.print("cannot convert: " + srcChannel + "-channel, " + srcBit + "-bit" + " FLAC to " + srcChannel + "-channel, " + srcBit + "-bit," + " big-endian PCM...");
                    if (!checkConversion(srcFormat, targetFormat, true)) {
                        success = false;
                    }
                }
            }
        } catch (Throwable t) {
            t.printStackTrace();
            success = false;
        }
        return success;
    }
}
