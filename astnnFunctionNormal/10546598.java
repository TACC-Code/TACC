class BackupThread extends Thread {
    public static String makeSampleSummary(SampleDescriptor sampleDescriptor) {
        if (sampleDescriptor != null) {
            StringBuffer s = new StringBuffer("<html><sp>");
            s.append(sampleDescriptor.getFormattedSampleRateInKhz());
            s.append("<br><sp>");
            s.append(sampleDescriptor.getChannelDescription());
            s.append("<br><sp>");
            s.append(sampleDescriptor.getLengthInSampleFrames() + " samples");
            s.append("<br><sp>");
            s.append(sampleDescriptor.getFormattedDurationInSeconds());
            s.append("<br><sp>");
            s.append(sampleDescriptor.getFormattedSize());
            s.append("</html>");
            return s.toString();
        } else return null;
    }
}
