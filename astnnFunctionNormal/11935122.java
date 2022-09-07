class BackupThread extends Thread {
    public String getChannels(String pathToFile) {
        String channels = null;
        try {
            File file = new File(pathToFile);
            AudioFile audioFile = AudioFileIO.read(file);
            AudioHeader audioHeader = audioFile.getAudioHeader();
            if (audioHeader != null) {
                channels = audioHeader.getChannels();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return channels;
    }
}
