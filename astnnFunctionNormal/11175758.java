class BackupThread extends Thread {
    public String getDosboxParameters() {
        String ret = dosboxParameters;
        String executableName = new File(gameBinaryPath).getName();
        ret = ret.replace("{GAMEEXE}", executableName);
        ret = ret.replace("{HOSTIP}", roomData.getIP());
        if (GameDatabase.getNoSpacesFlag(roomData.getChannel(), roomData.getModName())) {
            ret = ret.replace("{NAME}", Globals.getThisPlayerInGameName().replace(" ", "_"));
        } else {
            ret = ret.replace("{NAME}", Globals.getThisPlayerInGameName());
        }
        ret = ret.replace("{ROOMNAME}", roomData.getRoomName());
        if (roomData.getPassword() != null && roomData.getPassword().length() > 0) {
            String tmp;
            if (roomData.isHost()) {
                tmp = GameDatabase.getHostPasswordPattern(roomData.getChannel(), roomData.getModName());
            } else {
                tmp = GameDatabase.getJoinPasswordPattern(roomData.getChannel(), roomData.getModName());
            }
            tmp = tmp.replace("{PASSWORD}", roomData.getPassword());
            ret = ret.replace("{PASSWORD}", tmp);
        } else {
            ret = ret.replace("{PASSWORD}", "");
        }
        if (TempGameSettings.getMap() != null) {
            ret = ret.replace("{MAP}", TempGameSettings.getMap());
        }
        for (GameSetting gs : TempGameSettings.getGameSettings()) {
            ret = ret.replace("{" + gs.getKeyWord() + "}", gs.getRealValue());
        }
        String params = GameDatabase.getAdditionalParameters(GameDatabase.getIDofGame(roomData.getChannel()));
        if (params != null && params.length() > 0) {
            ret += " " + params;
        }
        return ret;
    }
}
