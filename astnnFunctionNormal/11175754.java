class BackupThread extends Thread {
    public DosboxLaunchInfo(RoomData roomData) {
        super(roomData);
        dosboxBinaryPath = Settings.getDOSBoxExecutable();
        gameBinaryPath = GameDatabase.getLocalExecutablePath(GameDatabase.getIDofGame(roomData.getChannel()));
        dosboxParameters = "-noconsole -c \"mount X -u\" -c \"mount X " + GameDatabase.getLocalInstallPath(GameDatabase.getIDofGame(roomData.getChannel())) + "\" -c \"X:\"";
        if (roomData.isHost()) {
            dosboxParameters += " " + GameDatabase.getHostPattern(roomData.getChannel(), roomData.getModName());
        } else {
            dosboxParameters += " " + GameDatabase.getJoinPattern(roomData.getChannel(), roomData.getModName());
        }
        if (Settings.getDOSBoxFullscreen()) {
            dosboxParameters += " -fullscreen";
        }
        dosboxParameters += " -c \"exit\"";
    }
}
