class BackupThread extends Thread {
    private String getDuration(AudioFileFormat aFormat, AudioFormat format) {
        float duration = aFormat.getByteLength() / (format.getSampleRate() * (format.getSampleSizeInBits() / 8) * format.getChannels());
        int hours = (int) duration / 3600;
        duration = duration - (hours * 3600);
        int minutes = (int) duration / 60;
        duration = duration - (minutes * 60);
        String h = Integer.toString(hours);
        String m = Integer.toString(minutes);
        String s = Float.toString(duration);
        if (hours < 10) {
            h = "0" + h;
        }
        if (minutes < 10) {
            m = "0" + m;
        }
        if (duration < 10) {
            s = "0" + s;
        }
        return h + ":" + m + ":" + s;
    }
}
