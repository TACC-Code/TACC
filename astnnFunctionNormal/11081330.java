class BackupThread extends Thread {
    private void doSomethingEntered() {
        if (props.getProperty("emul.accessDigest") != null) {
            try {
                DESSHA1 sha = new DESSHA1();
                if (props.getProperty("emul.accessDigest").equals(sha.digest(new String(password.getPassword()), "tn5205j"))) {
                    accessOptions.setEnabled(true);
                    setPassButton.setEnabled(true);
                }
            } catch (Exception ex) {
                this.log.warn(ex.getMessage(), ex);
            }
        }
    }
}
