class BackupThread extends Thread {
    public void testSkipFile() {
        if (out != null) out.println("-> Filename : " + filename + " <-");
        try {
            File file = new File(filename);
            AudioFileFormat aff = AudioSystem.getAudioFileFormat(file);
            AudioInputStream in = AudioSystem.getAudioInputStream(file);
            AudioInputStream din = null;
            AudioFormat baseFormat = in.getFormat();
            if (out != null) out.println("Source Format : " + baseFormat.toString());
            AudioFormat decodedFormat = new AudioFormat(AudioFormat.Encoding.PCM_SIGNED, baseFormat.getSampleRate(), 16, baseFormat.getChannels(), baseFormat.getChannels() * 2, baseFormat.getSampleRate(), false);
            if (out != null) out.println("Target Format : " + decodedFormat.toString());
            din = AudioSystem.getAudioInputStream(decodedFormat, in);
            long toSkip = (long) (file.length() / 2);
            long skipped = skip(din, toSkip);
            if (out != null) out.println("Skip : " + skipped + "/" + toSkip + " (Total=" + file.length() + ")");
            if (out != null) out.println("Start playing");
            rawplay(decodedFormat, din);
            in.close();
            if (out != null) out.println("Played");
            assertTrue("testSkip : OK", true);
        } catch (Exception e) {
            assertTrue(e.getMessage(), false);
        }
    }
}
