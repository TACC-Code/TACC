class BackupThread extends Thread {
    public void testPlayFile() {
        try {
            if (out != null) out.println("---  Start : " + filename + "  ---");
            File file = new File(filename);
            AudioFileFormat aff = AudioSystem.getAudioFileFormat(file);
            if (out != null) out.println("Audio Type : " + aff.getType());
            AudioInputStream in = AudioSystem.getAudioInputStream(file);
            AudioInputStream din = null;
            if (in != null) {
                AudioFormat baseFormat = in.getFormat();
                if (out != null) out.println("Source Format : " + baseFormat.toString());
                AudioFormat decodedFormat = new AudioFormat(AudioFormat.Encoding.PCM_SIGNED, baseFormat.getSampleRate(), 16, baseFormat.getChannels(), baseFormat.getChannels() * 2, baseFormat.getSampleRate(), false);
                if (out != null) out.println("Target Format : " + decodedFormat.toString());
                din = AudioSystem.getAudioInputStream(decodedFormat, in);
                if (din instanceof PropertiesContainer) {
                    assertTrue("PropertiesContainer : OK", true);
                } else {
                    assertTrue("Wrong PropertiesContainer instance", false);
                }
                rawplay(decodedFormat, din);
                in.close();
                if (out != null) out.println("---  Stop : " + filename + "  ---");
                assertTrue("testPlay : OK", true);
            }
        } catch (Exception e) {
            assertTrue("testPlay : " + e.getMessage(), false);
        }
    }
}
