class BackupThread extends Thread {
    public static String getAudioFormatData(AudioFormat audio_format) {
        String encoding = audio_format.getEncoding().toString() + "\n";
        String endian;
        if (audio_format.isBigEndian()) endian = "big-endian\n"; else endian = "little-endian\n";
        String sampling_rate = (audio_format.getSampleRate() / 1000) + " kHz\n";
        String bit_depth = audio_format.getSampleSizeInBits() + " bits\n";
        String channels;
        if (audio_format.getChannels() == 1) channels = "mono\n"; else if (audio_format.getChannels() == 2) channels = "stereo\n"; else channels = audio_format.getChannels() + " channels\n";
        String frame_size = (8 * audio_format.getFrameSize()) + " bits\n";
        String frame_rate = audio_format.getFrameRate() + " frames per second\n";
        String additional_properties = audio_format.properties() + "\n";
        String data = new String();
        data += new String("SAMPLING RATE: " + sampling_rate);
        data += new String("BIT DEPTH: " + bit_depth);
        data += new String("CHANNELS: " + channels);
        data += new String("FRAME SIZE: " + frame_size);
        data += new String("FRAME RATE: " + frame_rate);
        data += new String("ENCODING: " + encoding);
        data += new String("BYTE ORDER: " + endian);
        data += new String("PROPERTIES: " + additional_properties);
        return data;
    }
}
