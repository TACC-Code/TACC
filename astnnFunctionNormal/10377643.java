class BackupThread extends Thread {
    void setAudioSampleRate(double srate) {
        if (theAudioFormat == null) theAudioFormat = new AudioFormat(null, Format.NOT_SPECIFIED, Format.NOT_SPECIFIED, 1); else theAudioFormat = new AudioFormat(theAudioFormat.getEncoding(), srate, theAudioFormat.getSampleSizeInBits(), theAudioFormat.getChannels());
    }
}
