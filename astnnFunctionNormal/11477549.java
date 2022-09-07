class BackupThread extends Thread {
    private void prepareFile() {
        try {
            File fTmp = new File(EzimConf.getConfDir());
            if (!fTmp.exists() || !fTmp.isDirectory()) fTmp.mkdirs();
            this.flock = new FileOutputStream(EzimConf.getLockFilename()).getChannel().tryLock();
            if (this.flock == null) {
                throw new Exception("Only one program instance can be run at a time.");
            }
        } catch (Exception e) {
            EzimLogger.getInstance().severe(e.getMessage(), e);
            System.exit(1);
        }
    }
}
