class BackupThread extends Thread {
    public static boolean verifyOverwrite(Component parent, File f) {
        Object[] options = { "Yes", "No" };
        int n = JOptionPane.showOptionDialog(parent, "<html>This file already exists.  Do you wish to overwrite the file?<br>" + f.getPath() + "<html>", "Confirm Overwrite", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null, options, options[1]);
        return (n == JOptionPane.YES_OPTION);
    }
}
