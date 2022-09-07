class BackupThread extends Thread {
    public boolean getfileIsTextual() {
        File f = getSFile();
        if (f == null) {
            return false;
        }
        if (!f.exists()) {
            return true;
        }
        FormatDescription desc = FormatIdentification.identify(f);
        if (desc == null) {
            try {
                FileInputStream fis = new FileInputStream(f);
                if (fis.getChannel().size() == 0) {
                    return true;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return false;
        } else {
            return desc.isTextual();
        }
    }
}
