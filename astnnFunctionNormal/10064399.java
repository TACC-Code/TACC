class BackupThread extends Thread {
    void recordTape() {
        OutputStream out = null;
        if (!tapeFile.exists()) {
            out = Stream.getOutputStream(tapeFile, outFormat);
        } else {
            String[] msg = { "File " + tapeFile.getPath() + " already exists.", "Are you sure you want to overwrite it?" };
            String[] opts = { "No", "Yes" };
            if (JOptionPane.showInternalOptionDialog(this, msg, getTitle(), JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE, null, opts, opts[0]) == JOptionPane.NO_OPTION) {
                out = Stream.getOutputStream(tapeFile, outFormat);
            } else {
                stop.button.doClick();
            }
        }
        if (out != null) {
            format.setValue((out instanceof UK101OutputStream) ? TapeFormat.MODE_ASCII : TapeFormat.MODE_BINARY);
            recorder.setOutputTape(out);
        }
    }
}
