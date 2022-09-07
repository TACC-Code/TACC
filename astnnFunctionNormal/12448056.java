class BackupThread extends Thread {
    public void saveas_action(IDEUpdater updater) {
        try {
            JFileChooser jfc = null;
            if (ids.getCurrentFileFolder() == null) {
                jfc = new JFileChooser();
            } else {
                jfc = new JFileChooser(ids.getCurrentFileFolder());
                jfc.addChoosableFileFilter(new javax.swing.filechooser.FileFilter() {

                    public boolean accept(File f) {
                        boolean acceptedFormat = f.getName().toLowerCase().endsWith(".xml");
                        return acceptedFormat || f.isDirectory();
                    }

                    public String getDescription() {
                        return "xml";
                    }
                });
            }
            boolean invalidFolder = true;
            File sel = null;
            while (invalidFolder) {
                jfc.setLocation(DialogWindows.getCenter(resources.getMainFrame().getSize(), resources.getMainFrame()));
                jfc.showSaveDialog(resources.getMainFrame());
                sel = jfc.getSelectedFile();
                invalidFolder = sel != null && !sel.getParentFile().exists();
                if (invalidFolder) {
                    JOptionPane.showMessageDialog(resources.getMainFrame(), "You cannot save your file to " + sel.getParentFile().getPath() + ". That folder does not exist. Please, try again", "Error", JOptionPane.WARNING_MESSAGE);
                }
            }
            if (sel != null && !sel.isDirectory()) {
                if (sel.exists()) {
                    int result = JOptionPane.showConfirmDialog(resources.getMainFrame(), "The file already exists. Do you want to overwrite (y/n)?", "Warning", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
                    if (result == JOptionPane.OK_OPTION) {
                        PersistenceManager p = new PersistenceManager();
                        p.save(sel, ids);
                        ids.setCurrentFile(sel);
                        ids.setCurrentFileFolder(sel.getParentFile());
                        HistoryManager.updateHistory(ids.getCurrentFile(), resources, ids, updater);
                        resources.getMainFrame().setTitle("Project:" + sel.getAbsolutePath());
                        ids.setChanged(false);
                    }
                } else {
                    PersistenceManager p = new PersistenceManager();
                    if (!sel.getPath().toLowerCase().endsWith(".xml")) {
                        sel = new File(sel.getPath() + ".xml");
                    }
                    p.save(sel, ids);
                    ids.setCurrentFile(sel);
                    ids.setCurrentFileFolder(sel.getParentFile());
                    resources.getMainFrame().setTitle("Project:" + sel.getAbsolutePath());
                    HistoryManager.updateHistory(ids.getCurrentFile(), resources, ids, updater);
                    ids.setChanged(false);
                }
            }
        } catch (Exception e1) {
            e1.printStackTrace();
        }
    }
}
