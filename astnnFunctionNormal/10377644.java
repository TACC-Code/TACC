class BackupThread extends Thread {
    void setAudioBits(int bits) {
        if (theAudioFormat == null) theAudioFormat = new AudioFormat(null, Format.NOT_SPECIFIED, bits, 1); else theAudioFormat = new AudioFormat(theAudioFormat.getEncoding(), theAudioFormat.getSampleRate(), bits, theAudioFormat.getChannels());
    }
}
