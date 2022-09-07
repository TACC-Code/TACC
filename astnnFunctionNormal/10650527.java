class BackupThread extends Thread {
    public synchronized void run() {
        try {
            Process _process = Runtime.getRuntime().exec(_shellPath, null, new File(_shellDirectory));
            BufferedWriter _writer = new BufferedWriter(new OutputStreamWriter(_process.getOutputStream()));
            AcideConsoleInputProcess inputThread = new AcideConsoleInputProcess(_writer, System.in);
            AcideOutputProcess errorGobbler = new AcideOutputProcess(_process.getErrorStream(), _textComponent);
            AcideOutputProcess outputGobbler = new AcideOutputProcess(_process.getInputStream(), _textComponent);
            errorGobbler.start();
            outputGobbler.start();
            inputThread.start();
            try {
                _process.waitFor();
            } catch (InterruptedException exception) {
                AcideLog.getLog().error(exception.getMessage());
                exception.printStackTrace();
            }
        } catch (Exception exception) {
            AcideLog.getLog().error(exception.getMessage());
            JOptionPane.showMessageDialog(AcideMainWindow.getInstance(), AcideLanguageManager.getInstance().getLabels().getString("s1017"), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}
