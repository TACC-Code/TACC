class BackupThread extends Thread {
    private void dialogChanged() {
        if (_backupTarget == null || _backupTarget.length() == 0) {
            setMessage("");
            setErrorState("No target defined");
            _ableToFinish = false;
        } else {
            setErrorState(null);
            _ableToFinish = true;
            if (targetExists()) {
                setMessage("Attention: The target exists already on the filesystem.\n" + "By creating the backup might overwrite existing data!!!");
            } else {
                setMessage("");
            }
        }
    }
}
