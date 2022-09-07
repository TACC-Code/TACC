class BackupThread extends Thread {
    public static AudioFormat complete(AudioFormat f) {
        if (f.getSampleSizeInBits() > 8 && f.getEndian() == Format.NOT_SPECIFIED) {
            return new AudioFormat(f.getEncoding(), f.getSampleRate(), f.getSampleSizeInBits(), f.getChannels(), AudioFormat.BIG_ENDIAN, f.getSigned(), f.getFrameSizeInBits(), f.getFrameRate(), f.getDataType());
        }
        return f;
    }
}
