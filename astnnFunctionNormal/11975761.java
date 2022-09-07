class BackupThread extends Thread {
    public void install_only(String packagePath) throws UserException {
        CapFiles file = null;
        try {
            file = new CapFiles(packagePath, max_size);
        } catch (Exception e) {
            throw new UserException(e.getMessage());
        }
        log.write("File " + packagePath + " read.");
        int nbApplets = file.getNumberOfApplets();
        log.write("Number of applets :" + nbApplets);
        for (int i = 0; i < nbApplets; i++) {
            install_applet(file.getAIDHeader(), (byte[]) file.getAIDApplet().elementAt(i));
        }
        log.write("Installing " + packagePath + " completed.");
    }
}
