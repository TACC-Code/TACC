class BackupThread extends Thread {
    private static boolean checkDirect(AudioFormat srcFormat, boolean neg) {
        AudioFormat targetFormat = new AudioFormat(srcFormat.getSampleRate(), srcFormat.getSampleSizeInBits(), srcFormat.getChannels(), true, false);
        return checkConversion(srcFormat, targetFormat, neg);
    }
}
