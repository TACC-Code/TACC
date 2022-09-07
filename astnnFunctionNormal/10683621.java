class BackupThread extends Thread {
        @Override
        public void actionPerformed(ActionEvent e) {
            File contigs = null;
            Properties projectProperties = p.getLookup().lookup(Properties.class);
            if (projectProperties != null) {
                contigs = new File(projectProperties.getProperty("contigs"));
            }
            if (contigs != null && !contigs.exists()) {
                JOptionPane.showMessageDialog(null, "Contigs are not readable", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            File[] references = new FileChooserBuilder("ReferenceFastaFile").addFileFilter(new CustomFileFilter(".fas,.fna,.fasta", "FASTA File")).setFilesOnly(true).setTitle("Select Reference Genome(s)").showMultiOpenDialog();
            if (references != null) {
                nextreference: for (int i = 0; i < references.length; i++) {
                    File reference = references[i];
                    String referenceString = MiscFileUtils.getFileNameWithoutExtension(reference);
                    FileObject existingMatches = p.getProjectDirectory().getFileObject(referenceString, "r2c");
                    if (existingMatches != null) {
                        NotifyDescriptor.Confirmation confirm = new NotifyDescriptor.Confirmation(existingMatches.getNameExt() + " already exists\nDo you want to overwrite this file?", "Already existing matchfile");
                        Object returnvalue = DialogDisplayer.getDefault().notify(confirm);
                        if (returnvalue == NotifyDescriptor.Confirmation.YES_OPTION) {
                            try {
                                FileObject logfile = FileUtil.findBrother(existingMatches, "log");
                                if (logfile != null) {
                                    logfile.delete();
                                }
                                existingMatches.delete();
                            } catch (IOException ex) {
                                Exceptions.printStackTrace(ex);
                            }
                        } else {
                            continue nextreference;
                        }
                    }
                    MatchingTask matcher = new MatchingTask(contigs, reference);
                    CombinedNetbeansProgressReporter progress = new CombinedNetbeansProgressReporter("Matching Contigs:" + MiscFileUtils.getFileNameWithoutExtension(contigs), "Matching on " + referenceString, referenceString);
                    matcher.setProgressReporter(progress);
                    matcher.addPropertyChangeListener(this);
                    taskQueue.add(matcher);
                }
            }
            startNextTask();
        }
}
