class BackupThread extends Thread {
    public static AudioInputStream convertStream(AudioInputStream stream, AudioFormat desiredFormat) throws AudioConversionException {
        try {
            AudioFormat format = stream.getFormat();
            System.out.println(AudioSystem.isConversionSupported(desiredFormat, stream.getFormat()));
            if (!isPCM(format.getEncoding())) {
                if (DEBUG) out("converting to PCM...");
                AudioFormat.Encoding targetEncoding = (format.getSampleSizeInBits() == 8) ? AudioFormat.Encoding.PCM_UNSIGNED : AudioFormat.Encoding.PCM_SIGNED;
                stream = convertEncoding(targetEncoding, stream);
                if (DEBUG) out("stream: " + stream);
                if (DEBUG) out("format: " + stream.getFormat());
            }
            if (stream.getFormat().getChannels() != desiredFormat.getChannels()) {
                if (DEBUG) out("converting channels...");
                stream = convertChannels(desiredFormat.getChannels(), stream);
                if (DEBUG) out("stream: " + stream);
                if (DEBUG) out("format: " + stream.getFormat());
            }
            boolean bDoConvertSampleSize = (stream.getFormat().getSampleSizeInBits() != desiredFormat.getSampleSizeInBits());
            boolean bDoConvertEndianess = (stream.getFormat().isBigEndian() != desiredFormat.isBigEndian());
            if (bDoConvertSampleSize || bDoConvertEndianess) {
                if (DEBUG && bDoConvertSampleSize) out("converting sample size ...");
                if (DEBUG && bDoConvertEndianess) out("converting endianess ...");
                stream = convertPCMSampleSizeAndEndianess(desiredFormat.getSampleSizeInBits(), desiredFormat.getEncoding(), desiredFormat.isBigEndian(), stream);
                if (DEBUG) out("stream: " + stream);
                if (DEBUG) out("format: " + stream.getFormat());
            }
            if (!equals(stream.getFormat().getSampleRate(), desiredFormat.getSampleRate())) {
                if (DEBUG) out("converting sample rate...");
                stream = convertSampleRate(desiredFormat.getSampleRate(), stream);
                if (DEBUG) out("stream: " + stream);
                if (DEBUG) out("format: " + stream.getFormat());
            }
            return new AudioInputStream(stream, desiredFormat, stream.getFrameLength());
        } catch (Exception e) {
            throw new AudioConversionException(e.getMessage());
        }
    }
}
