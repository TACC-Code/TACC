class BackupThread extends Thread {
    public DaapAudioResponseNIO(DaapRequest request, Song song, FileInputStream in, long pos, long end) {
        super(request, song, in, pos, end);
        headerBuffer = ByteBuffer.wrap(header);
        this.connection = (DaapConnectionNIO) request.getConnection();
        fileChannel = in.getChannel();
    }
}
