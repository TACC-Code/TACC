class BackupThread extends Thread {
    public boolean turnSpeakerOn() {
        System.out.println(getClass().getName() + ".turnSpeakerOn");
        boolean bRet = false;
        if (bIsSpeakerOn) {
            System.out.println(getClass().getName() + ".turnSpeakerOn -- speaker already on");
            return true;
        }
        messageDataSource = new MessageDataSource(audioObservable);
        ads = new AudioAtomicDataSource(messageDataSource, this.decoder);
        LevelDataSource speakerLevelDataSource = new LevelDataSource(ads, "Speaker");
        speakerLevelDataSource.setLevelListener(speakerLevelListener);
        String strJVM = System.getProperty("java.version");
        try {
            if (strJVM.charAt(2) > '2') {
                Class classJava2Speaker = Class.forName("com.sts.webmeet.content.client.audio.Java2Speaker");
                speaker = (Speaker) classJava2Speaker.newInstance();
            } else {
                speaker = new AuSpeaker();
            }
            speaker.startPlaying(audioFormat.getChannelCount(), (new Float(audioFormat.getSamplesPerSecond())).floatValue(), audioFormat.getBitsPerSample(), speakerLevelDataSource);
            bRet = true;
        } catch (Exception e) {
            e.printStackTrace();
            speaker = new NullSpeaker();
            try {
                speaker.startPlaying(audioFormat.getChannelCount(), (new Float(audioFormat.getSamplesPerSecond())).floatValue(), audioFormat.getBitsPerSample(), speakerLevelDataSource);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
        bIsSpeakerOn = bRet;
        return bRet;
    }
}
