class BackupThread extends Thread {
    private String createAudioMediaDescription(File file) throws UnsupportedAudioFileException, IOException {
        int payload = -1;
        String rtpMap = null;
        StringBuffer sb = new StringBuffer();
        String filePath = file.getPath();
        if (filePath.endsWith(Extension.WAV)) {
            AudioFileFormat auFileFmt = AudioSystem.getAudioFileFormat(file);
            AudioFormat auFmt = auFileFmt.getFormat();
            Encoding encoding = auFmt.getEncoding();
            if (encoding == Encoding.ALAW) {
                payload = 8;
                rtpMap = "8 pcma/8000";
            } else if (encoding == Encoding.ULAW) {
                payload = 0;
                rtpMap = "0 pcmu/8000";
            } else if (encoding == Encoding.PCM_SIGNED) {
                int sampleSize = auFmt.getSampleSizeInBits();
                if (sampleSize != 16) {
                    throw new UnsupportedAudioFileException("Found unsupported Format " + auFileFmt);
                }
                int sampleRate = (int) auFmt.getSampleRate();
                if (sampleRate == 44100) {
                    int channels = auFmt.getChannels();
                    if (channels == 1) {
                        payload = 11;
                        rtpMap = "11 l16/44100/1";
                    } else {
                        payload = 10;
                        rtpMap = "10 l16/44100/2";
                    }
                }
            }
        } else if (filePath.endsWith(Extension.GSM)) {
            payload = 3;
            rtpMap = "3 gsm/8000";
        }
        sb.append("m=audio 0 RTP/AVP ").append(payload).append("\n");
        sb.append("a=rtpmap:").append(rtpMap).append("\n");
        return sb.toString();
    }
}
