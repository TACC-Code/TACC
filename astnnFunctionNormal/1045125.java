class BackupThread extends Thread {
    private ChannelNamesAndColors getChannelNamesAndColors(RandomAccessStream stream, long position, long channelCount) {
        ChannelNamesAndColors channelNamesAndColors = new ChannelNamesAndColors();
        try {
            stream.seek((int) position);
            channelNamesAndColors.BlockSize = ReaderToolkit.swap(stream.readInt());
            channelNamesAndColors.NumberColors = ReaderToolkit.swap(stream.readInt());
            channelNamesAndColors.NumberNames = ReaderToolkit.swap(stream.readInt());
            channelNamesAndColors.ColorsOffset = ReaderToolkit.swap(stream.readInt());
            channelNamesAndColors.NamesOffset = ReaderToolkit.swap(stream.readInt());
            channelNamesAndColors.Mono = ReaderToolkit.swap(stream.readInt());
            stream.seek((int) channelNamesAndColors.NamesOffset + (int) position);
            channelNamesAndColors.ChannelNames = new String[(int) channelCount];
            for (int j = 0; j < channelCount; j++) {
                long size = ReaderToolkit.swap(stream.readInt());
                channelNamesAndColors.ChannelNames[j] = ReaderToolkit.readSizedNULLASCII(stream, size);
            }
            stream.seek((int) channelNamesAndColors.ColorsOffset + (int) position);
            channelNamesAndColors.Colors = new int[(int) (channelNamesAndColors.NumberColors)];
            for (int j = 0; j < (int) (channelNamesAndColors.NumberColors); j++) {
                channelNamesAndColors.Colors[j] = ReaderToolkit.swap(stream.readInt());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return channelNamesAndColors;
    }
}
