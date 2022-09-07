class BackupThread extends Thread {
    private void showCaptureHelpDialog() {
        JOptionPane.showInternalMessageDialog(mainFrame.getContentPane(), getChannelListInstructions(), "Capture Help", JOptionPane.INFORMATION_MESSAGE);
    }
}
