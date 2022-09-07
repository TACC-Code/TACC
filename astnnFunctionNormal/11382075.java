class BackupThread extends Thread {
    private void atActionExport() {
        File xmlFile = new File(atFileField.getText());
        try {
            boolean created = xmlFile.createNewFile();
            if (!created) {
                changer.setStatusText(I18N.translate("Export file already exists."));
                HMessageDialog dlg = new HMessageDialog(changer.topFrame, HMessageDialog.MESSAGE_QUESTION, I18N.translate("Export file already exists. Do you want rewrite this file?"));
                if (dlg.getResult() == HMessageDialog.RESULT_NO) {
                    return;
                }
            }
        } catch (IOException e) {
            changer.setStatusText(I18N.translate("Can not create new export file. ") + e.getLocalizedMessage());
            new HMessageDialog(changer.topFrame, HMessageDialog.MESSAGE_ERROR, I18N.translate("Can not create new export file. " + e.getLocalizedMessage()));
            return;
        }
        if (!xmlFile.canWrite()) {
            changer.setStatusText(I18N.translate("Can not write to export file."));
            new HMessageDialog(changer.topFrame, HMessageDialog.MESSAGE_ERROR, I18N.translate("Can not write to export file."));
            return;
        }
        try {
            DataManagement.getDBManager().saveDataToXML(xmlFile);
            changer.setStatusText(I18N.translate("Data successfully exported to file '" + xmlFile.getName() + "'."));
        } catch (SQLException e) {
            changer.setStatusText(I18N.translate("Can not get data from DB. " + e.getLocalizedMessage()));
            new HMessageDialog(changer.topFrame, HMessageDialog.MESSAGE_ERROR, I18N.translate("Can not get data from DB. " + e.getLocalizedMessage()));
        } catch (IOException e) {
            changer.setStatusText(I18N.translate("Can not write to export file. " + e.getLocalizedMessage()));
            new HMessageDialog(changer.topFrame, HMessageDialog.MESSAGE_ERROR, I18N.translate("Can not write to export file. " + e.getLocalizedMessage()));
        }
    }
}
