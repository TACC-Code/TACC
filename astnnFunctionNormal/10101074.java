class BackupThread extends Thread {
    @Override
    public Format[] getSupportedOutputFormats(Format input) {
        if (input == null) return outputFormats; else {
            if (!(input instanceof AudioFormat)) {
                return new Format[] { null };
            }
            final AudioFormat inputCast = (AudioFormat) input;
            if (!inputCast.getEncoding().equals(AudioFormat.GSM) || (inputCast.getSampleSizeInBits() != 8 && inputCast.getSampleSizeInBits() != Format.NOT_SPECIFIED) || (inputCast.getChannels() != 1 && inputCast.getChannels() != Format.NOT_SPECIFIED) || (inputCast.getFrameSizeInBits() != 264 && inputCast.getFrameSizeInBits() != Format.NOT_SPECIFIED)) {
                return new Format[] { null };
            }
            final AudioFormat result = new AudioFormat(AudioFormat.GSM_RTP, inputCast.getSampleRate(), 8, 1, inputCast.getEndian(), inputCast.getSigned(), 264, inputCast.getFrameRate(), inputCast.getDataType());
            return new Format[] { result };
        }
    }
}
