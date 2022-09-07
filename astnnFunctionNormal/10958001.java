class BackupThread extends Thread {
    private void confirmOverwrite(final NoteInfo existingNote, SugarItem item) {
        Object[] possibleValues = { "Yes", "Yes To All", "No", "No To All" };
        String message = "<html><b>Warning</b><hr>Sugar Note <b><i>'" + existingNote.getTitle() + "'</i></b><br> Already attached to following Sugar Item:<br>" + "<b><i>'" + item.getName() + "'</i></b><hr>" + " <br>Are you sure you want to overwrite ?<br></html>";
        int val = JOptionPane.showOptionDialog(MainWindow.getInstance(), message, "Confirm Overwrite", JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE, null, possibleValues, possibleValues[0]);
        confirmOverwriteValue = val;
    }
}
