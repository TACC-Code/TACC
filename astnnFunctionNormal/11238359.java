class BackupThread extends Thread {
    @Override
    public String getCodecDetails() {
        StringBuffer sb = new StringBuffer();
        sb.append("<html><b>Encoding Type: </b>");
        sb.append(getEncodingType().toUpperCase());
        sb.append("<br><b>Sampling rate: </b>");
        sb.append(getSampleRate() + " Hz");
        sb.append("<br><b>Bitrate: </b>");
        sb.append(getBitRate() + " Kbps");
        sb.append("<br><b>Channels: </b>");
        sb.append(getChannels());
        if (size != -1) {
            sb.append("<br><b>Size: </b>");
            sb.append(Utilities.byteCountToDisplaySize(size));
        }
        sb.append("</html>");
        return sb.toString();
    }
}
