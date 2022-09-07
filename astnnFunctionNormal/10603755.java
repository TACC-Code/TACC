class BackupThread extends Thread {
    public boolean supports(SoundDataFormat format) {
        boolean rateSupported = false;
        int rate = format.getRate();
        for (int n = 0; n < supportedRates.length; n++) if (supportedRates[n] == rate) rateSupported = true;
        boolean bitsSupported = false;
        int bits = format.getBits();
        for (int n = 0; n < supportedBits.length; n++) if (supportedBits[n] == bits) bitsSupported = true;
        boolean channelsSupported = false;
        int channels = format.getChannels();
        for (int n = 0; n < supportedChannels.length; n++) if (supportedChannels[n] == channels) channelsSupported = true;
        return rateSupported && bitsSupported && channelsSupported;
    }
}
