class BackupThread extends Thread {
    public int showOverwriteDialog(String file, boolean yesToAllPresent) {
        int n = -1;
        if (yesToAllPresent) {
            final Object[] buttonRowObjects = new Object[] { Messages.getMessage("PdfViewerConfirmButton.Yes"), Messages.getMessage("PdfViewerConfirmButton.YesToAll"), Messages.getMessage("PdfViewerConfirmButton.No"), Messages.getMessage("PdfViewerConfirmButton.Cancel") };
            n = JOptionPane.showOptionDialog(frame, file + "\n" + Messages.getMessage("PdfViewerMessage.FileAlreadyExists") + "\n" + Messages.getMessage("PdfViewerMessage.ConfirmResave"), Messages.getMessage("PdfViewerMessage.Overwrite"), JOptionPane.DEFAULT_OPTION, JOptionPane.QUESTION_MESSAGE, null, buttonRowObjects, buttonRowObjects[0]);
        } else {
            n = JOptionPane.showOptionDialog(frame, file + "\n" + Messages.getMessage("PdfViewerMessage.FileAlreadyExists") + "\n" + Messages.getMessage("PdfViewerMessage.ConfirmResave"), Messages.getMessage("PdfViewerMessage.Overwrite"), JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null, null, null);
        }
        return n;
    }
}
