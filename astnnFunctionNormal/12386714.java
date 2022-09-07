class BackupThread extends Thread {
    private javax.sound.sampled.Clip loadSound(String sound_name) {
        File f = new File(sound_name);
        javax.sound.sampled.AudioInputStream stream = null;
        javax.sound.sampled.Clip clip = null;
        try {
            stream = javax.sound.sampled.AudioSystem.getAudioInputStream(f);
        } catch (Exception e1) {
            e1.printStackTrace();
            return null;
        }
        try {
            javax.sound.sampled.AudioFormat format = stream.getFormat();
            if ((format.getEncoding() == javax.sound.sampled.AudioFormat.Encoding.ULAW) || (format.getEncoding() == javax.sound.sampled.AudioFormat.Encoding.ALAW)) {
                javax.sound.sampled.AudioFormat tmp = new javax.sound.sampled.AudioFormat(javax.sound.sampled.AudioFormat.Encoding.PCM_SIGNED, format.getSampleRate(), format.getSampleSizeInBits() * 2, format.getChannels(), format.getFrameSize() * 2, format.getFrameRate(), true);
                stream = javax.sound.sampled.AudioSystem.getAudioInputStream(tmp, stream);
                format = tmp;
            }
            javax.sound.sampled.DataLine.Info info = new javax.sound.sampled.DataLine.Info(javax.sound.sampled.Clip.class, stream.getFormat(), ((int) stream.getFrameLength() * format.getFrameSize()));
            clip = (javax.sound.sampled.Clip) javax.sound.sampled.AudioSystem.getLine(info);
            clip.open(stream);
        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }
        return clip;
    }
}
