class BackupThread extends Thread {
    protected void handleEditButton() {
        final int selectedIndex = table.getSelectionIndex();
        if (selectedIndex == -1) {
            ControlFactory.showMessageDialog(translator.getString("Please verify a BizDriver Protocol Type is selected to perform Edit operation."), translator.getString("Information"));
        } else {
            final JDBCDriverInfoDlg jdbcDlg = new JDBCDriverInfoDlg(parent, "Edit BizDriver Protocol Type", contentProvider.getRow(selectedIndex));
            if (jdbcDlg.showDialog()) {
                if (contentProvider.contains(jdbcDlg.getObject())) {
                    final int oldIndex = contentProvider.findRow(jdbcDlg.getObject());
                    if (oldIndex != selectedIndex) {
                        final int result = ControlFactory.showConfirmDialog("BizDriver Protocol Type already exists, Do you want to Overwrite?");
                        if (result == Window.OK) {
                            contentProvider.updateRow(selectedIndex, jdbcDlg.getObject(), false);
                            contentProvider.removeRow(oldIndex);
                        }
                    } else {
                        contentProvider.updateSelectedRow(jdbcDlg.getObject(), false);
                    }
                } else {
                    contentProvider.updateRow(selectedIndex, jdbcDlg.getObject(), false);
                }
            }
        }
    }
}
