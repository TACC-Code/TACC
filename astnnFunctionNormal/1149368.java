class BackupThread extends Thread {
    public void actionPerformed(ActionEvent e) {
        if (e.getSource().equals(btn_exportClustalMapped)) {
            File f = finp_clustalMapped.getFile();
            if (exportSequenceSet(list_initial, f)) {
                showDialog(true);
                file_initial = f;
            }
        } else if (e.getSource().equals(btn_exportFinal)) {
            File f_final = finp_finalOutput.getFile();
            importSequenceSet(list_initial, file_initial);
            File file_being_processed = null;
            try {
                String desc_final = list_final.size() + " final sequences to '" + f_final + "'";
                file_being_processed = f_final;
                new FastaFile().writeFile(f_final, list_final, new ProgressDialog(explorer.getFrame(), "Please wait, exporting final Fasta file ...", "The final Fasta file is being exported. Sorry for the wait!"));
                String desc_missing = "";
                if (list_missing != null && list_missing.size() > 0) {
                    File f_missing = new File(f_final.getParent(), "missing.txt");
                    if (f_missing.exists()) {
                        MessageBox mb = new MessageBox(explorer.getFrame(), "Overwrite file?", "The file '" + f_missing + "' already exists! Would you like to overwrite it?\n\nIf you say 'Yes' here, the file '" + f_missing + "' will be deleted.\nIf you say 'No' here, the file '" + f_missing + "' will not be altered. No missing file will be saved.", MessageBox.MB_YESNO);
                        if (mb.showMessageBox() == MessageBox.MB_NO) f_missing = null;
                    }
                    if (f_missing != null) {
                        desc_missing = " and " + list_missing.size() + " missing sequences to '" + f_missing + "'";
                        file_being_processed = f_missing;
                        new FastaFile().writeFile(f_missing, list_missing, new ProgressDialog(explorer.getFrame(), "Please wait, exporting missing sequences to a separate Fasta file ...", "The Fasta file containing missing seuqences is being written out now. Please wait a moment!"));
                    }
                }
                new MessageBox(explorer.getFrame(), "All done!", "Successfully exported " + desc_final + desc_missing + ".").go();
            } catch (IOException exp) {
                new MessageBox(explorer.getFrame(), "Could not write file " + file_being_processed + "!", "There was an error writing to '" + file_being_processed + "'. Try again, and ensure you have adequate permissions to the files you are trying to write to. The error which occured is: " + exp.getMessage());
            } catch (DelayAbortedException exp) {
                return;
            }
        } else if (e.getSource().equals(btn_close)) {
            closeDialog();
        }
    }
}
