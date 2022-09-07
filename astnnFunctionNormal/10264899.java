class BackupThread extends Thread {
    public void setSoundFile(File soundFile) {
        try {
            AudioFileFormat aFormat = AudioSystem.getAudioFileFormat(soundFile);
            AudioFormat format = aFormat.getFormat();
            durationText.setText(getDuration(aFormat, format));
            formatTypeText.setText(aFormat.getType().toString());
            byteLengthText.setText(Integer.toString(aFormat.getByteLength()));
            encodingTypeText.setText(format.getEncoding().toString());
            sampleRateText.setText(Float.toString(format.getSampleRate()));
            sampleSizeInBitsText.setText(Integer.toString(format.getSampleSizeInBits()));
            channelsText.setText(Integer.toString(format.getChannels()));
            isBigEndianText.setText(getBooleanString(format.isBigEndian()));
            setFtableText(soundFile, aFormat.getByteLength());
            fTableText.copy();
        } catch (java.io.IOException ioe) {
            JOptionPane.showMessageDialog(null, BlueSystem.getString("soundfile.infoPanel.error.couldNotOpenFile") + " " + soundFile.getAbsolutePath());
            clearAudioInfo();
            return;
        } catch (javax.sound.sampled.UnsupportedAudioFileException uae) {
            JOptionPane.showMessageDialog(null, BlueSystem.getString("soundfile.infoPanel.error.unsupportedAudio") + " " + uae.getLocalizedMessage());
            clearAudioInfo();
            return;
        }
    }
}
