class BackupThread extends Thread {
    private void writeMP3AudioStreamFormat(AVIMP3AudioStreamFormat strf) throws IOException {
        output.writeInt16(strf.getFormatTag());
        output.writeInt16(strf.getChannels());
        output.writeInt32(strf.getSamplesPerSecond());
        output.writeInt32(strf.getAvgBytesPerSec());
        output.writeInt16(strf.getBlockAlign());
        output.writeInt16(strf.getBitsPerSample());
        output.writeInt16(strf.getExtraSize());
        output.writeInt16(strf.getID());
        output.writeInt32(strf.getFlags());
        output.writeInt16(strf.getBlockSize());
        output.writeInt16(strf.getFramesPerBlock());
        output.writeInt16(strf.getCodecDelay());
        if (strf.getExtraSize() != AVIAudioStreamFormat.AVI_AUDIO_STREAM_FORMAT_EXTRA_SIZE_MP3) throw new IOException();
    }
}
