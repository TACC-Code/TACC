class BackupThread extends Thread {
    void saveAs(Tree t) {
        if (t == null) {
            return;
        }
        boolean ow1 = false;
        boolean ow2 = false;
        boolean exception = false;
        int result = saveas_jfilechooser.showSaveDialog(contentpane);
        File file = saveas_jfilechooser.getSelectedFile();
        if (file != null && result == JFileChooser.APPROVE_OPTION) {
            if (file.exists()) {
                int i = JOptionPane.showConfirmDialog(this, file + " already exists. Overwrite?", "File|SaveAs", JOptionPane.OK_CANCEL_OPTION, JOptionPane.WARNING_MESSAGE);
                if (i == JOptionPane.OK_OPTION) {
                    ow1 = ow2 = true;
                } else {
                    return;
                }
            }
            try {
                BufferedWriter bf = new BufferedWriter(new FileWriter(file));
                treeComments = comments.getText();
                treeComments = this.concatenateComments(treeComments);
                bf.write("[" + treeComments + " ]\n");
                bf.write(FormatFileTo80Cols.formatString(toNHX.toNewick(t.toNewHampshireX())));
                bf.close();
            } catch (Exception e) {
                exception = true;
                exceptionOccuredDuringSaveAs(e);
            }
            if (!exception) {
                treefile = file;
                setTitle("" + treefile);
            }
        }
    }
}
