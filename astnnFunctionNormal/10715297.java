class BackupThread extends Thread {
    private void saveAsXls() {
        File xlsFile = promptForFilename();
        if (xlsFile == null) {
            return;
        }
        if (xlsFile.getName().toLowerCase().endsWith(".xls") == false) {
            File renameF = new File(xlsFile.getAbsolutePath() + ".xls");
            xlsFile = renameF;
        }
        XlsReader xlsreader = new XlsReader();
        xlsreader.write(this, xlsFile);
    }
}
