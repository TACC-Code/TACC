class BackupThread extends Thread {
    public String getCheckSum() {
        try {
            if (scriptDigest == null) {
                readScript();
            } else if (scriptReader.ready()) {
                throw new UnitilsException("Cannot obtain checksum, since a script is currently being read");
            }
            return getHexPresentation(scriptDigest.digest());
        } catch (IOException e) {
            throw new UnitilsException(e);
        }
    }
}
