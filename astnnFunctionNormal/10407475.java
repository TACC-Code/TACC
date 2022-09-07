class BackupThread extends Thread {
    public static int overwriteOption(Component owner, String path, boolean always) {
        String msg = path + "\nalready exists.  Overwrite?";
        String[] options = { "Yes", "No", "Always yes", "Always no", "Cancel" };
        if (!always) {
            String[] altOptions = { "Yes", "No", "Cancel" };
            options = altOptions;
        }
        int choice = JOptionPane.showOptionDialog(owner, msg, "Overwrite?", JOptionPane.DEFAULT_OPTION, JOptionPane.WARNING_MESSAGE, null, options, options[1]);
        return choice;
    }
}
