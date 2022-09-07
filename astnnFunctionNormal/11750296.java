class BackupThread extends Thread {
    protected Map<String, Object> extractParameters(AudioFileFormat audioFileFormat, URL url) {
        Map<String, Object> parameters = new LinkedHashMap<String, Object>();
        parameters.put("file", url);
        parameters.put("type", audioFileFormat.getType().toString());
        parameters.put("sampleRateInHz", audioFileFormat.getFormat().getSampleRate());
        parameters.put("resolutionInBits", audioFileFormat.getFormat().getSampleSizeInBits());
        parameters.put("encoding", audioFileFormat.getFormat().getEncoding());
        parameters.put("encoding", audioFileFormat.getFormat().getEncoding());
        parameters.put("channels", audioFileFormat.getFormat().getChannels());
        parameters.put("bigEdian", audioFileFormat.getFormat().isBigEndian());
        parameters.put("bytes", audioFileFormat.getByteLength());
        Float totalTime = (audioFileFormat.getFrameLength() / audioFileFormat.getFormat().getFrameRate());
        parameters.put("lengthInTime", totalTime);
        return parameters;
    }
}
