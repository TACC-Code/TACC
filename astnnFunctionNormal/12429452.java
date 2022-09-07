class BackupThread extends Thread {
    private boolean getOverwrite() {
        boolean exists = false;
        filenamesloop: for (int i = 0; i < filename.length; i++) {
            if (filename[i].exists()) {
                exists = true;
                break filenamesloop;
            }
        }
        if (exists) {
            return Util.yesNoMsg(this, "A file (" + filename[0].getName() + ") that this\ngraph writes to already exists:\ndo you want to overwrite it?\n(Answering \"no\" will continue with the new data\nat the end of the old file.");
        } else {
            return true;
        }
    }
}
