class BackupThread extends Thread {
    @Override
    public Format[] getSupportedOutputFormats(Format input) {
        if (input == null) {
            return outputFormats;
        } else {
            if (!(input instanceof AudioFormat)) {
                logger.warning(this.getClass().getSimpleName() + ".getSupportedOutputFormats: input format does not match, returning format array of {null} for " + input);
                return new Format[] { null };
            }
            final AudioFormat inputCast = (AudioFormat) input;
            if (!inputCast.getEncoding().equals(AudioFormat.LINEAR) || (inputCast.getSampleSizeInBits() != 16 && inputCast.getSampleSizeInBits() != Format.NOT_SPECIFIED) || (inputCast.getChannels() != 1 && inputCast.getChannels() != Format.NOT_SPECIFIED) || (inputCast.getSigned() != AudioFormat.SIGNED && inputCast.getSigned() != Format.NOT_SPECIFIED) || (inputCast.getFrameSizeInBits() != 16 && inputCast.getFrameSizeInBits() != Format.NOT_SPECIFIED) || (inputCast.getDataType() != null && inputCast.getDataType() != Format.byteArray)) {
                logger.warning(this.getClass().getSimpleName() + ".getSupportedOutputFormats: input format does not match, returning format array of {null} for " + input);
                return new Format[] { null };
            }
            final AudioFormat result = new AudioFormat(AudioFormat.ALAW, inputCast.getSampleRate(), 8, 1, inputCast.getEndian(), AudioFormat.SIGNED, 8, inputCast.getFrameRate(), Format.byteArray);
            return new Format[] { result };
        }
    }
}
