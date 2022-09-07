class BackupThread extends Thread {
    public void widgetSelected(final SelectionEvent e) {
        if (e.getSource() == addBtn) {
            final JDBCDriverInfoDlg jdbcDlg = new JDBCDriverInfoDlg(parent, "Add BizDriver Protocol Type", null);
            if (jdbcDlg.showDialog()) {
                if (contentProvider.contains(jdbcDlg.getObject())) {
                    final int result = ControlFactory.showConfirmDialog("BizDriver Protocol Type already exists, Do you want to Overwrite?");
                    if (result == Window.OK) {
                        final int oldIndex = contentProvider.findRow(jdbcDlg.getObject());
                        contentProvider.updateRow(oldIndex, jdbcDlg.getObject(), false);
                    }
                } else {
                    contentProvider.addRow(jdbcDlg.getObject());
                }
            }
        }
        if (e.getSource() == editBtn) {
            handleEditButton();
        }
        if (e.getSource() == deleteBtn) {
            final int selectedIndex = table.getSelectionIndex();
            if (selectedIndex == -1) {
                ControlFactory.showMessageDialog("Please verify a BizDriver Protocol Type is selected to perform Delete operation.", "Information");
            } else {
                contentProvider.removeRow(selectedIndex);
            }
        }
        if (e.getSource() == selectAllBtn) {
            final Control compositeChildArray[] = checkComposite.getChildren();
            for (int i = 0; i < compositeChildArray.length; i++) {
                final Object compositeChilsArrayObj = compositeChildArray[i];
                if (compositeChilsArrayObj instanceof Button) {
                    final Button select = (Button) compositeChildArray[i];
                    select.setSelection(true);
                }
            }
        }
        if (e.getSource() == deSelectAllBtn) {
            final Control compositeChildArray[] = checkComposite.getChildren();
            for (int i = 0; i < compositeChildArray.length; i++) {
                final Object compositeChilsArrayObj = compositeChildArray[i];
                if (compositeChilsArrayObj instanceof Button) {
                    final Button select = (Button) compositeChildArray[i];
                    select.setSelection(false);
                }
            }
        }
    }
}
