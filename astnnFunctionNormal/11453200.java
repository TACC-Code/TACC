class BackupThread extends Thread {
            public void actionPerformed(final ActionEvent e) {
                if (GameRunner.isMac()) {
                    final FileDialog fileDialog = new FileDialog(m_frame);
                    fileDialog.setMode(FileDialog.SAVE);
                    SaveGameFileChooser.ensureDefaultDirExists();
                    fileDialog.setDirectory(SaveGameFileChooser.DEFAULT_DIRECTORY.getPath());
                    fileDialog.setFilenameFilter(new FilenameFilter() {

                        public boolean accept(final File dir, final String name) {
                            return name.endsWith(".tsvg") || name.endsWith(".svg");
                        }
                    });
                    fileDialog.setVisible(true);
                    String fileName = fileDialog.getFile();
                    final String dirName = fileDialog.getDirectory();
                    if (fileName == null) return; else {
                        if (!fileName.endsWith(".tsvg")) fileName += ".tsvg";
                        final File f = new File(dirName, fileName);
                        getGame().saveGame(f);
                        JOptionPane.showMessageDialog(m_frame, "Game Saved", "Game Saved", JOptionPane.INFORMATION_MESSAGE);
                    }
                } else {
                    final JFileChooser fileChooser = SaveGameFileChooser.getInstance();
                    final int rVal = fileChooser.showSaveDialog(m_frame);
                    if (rVal == JFileChooser.APPROVE_OPTION) {
                        File f = fileChooser.getSelectedFile();
                        if (GameRunner.isWindows()) {
                            final int slashIndex = Math.min(f.getPath().lastIndexOf("\\"), f.getPath().length());
                            final String filePath = f.getPath().substring(0, slashIndex);
                            if (!fileChooser.getCurrentDirectory().toString().equals(filePath)) {
                                final int choice = JOptionPane.showConfirmDialog(m_frame, "Sub directories are not allowed in the file name.  Please rename it.", "Cancel?", JOptionPane.DEFAULT_OPTION, JOptionPane.WARNING_MESSAGE);
                                return;
                            }
                        }
                        if (!f.getName().toLowerCase().endsWith(".tsvg")) {
                            f = new File(f.getParent(), f.getName() + ".tsvg");
                        }
                        if (f.exists()) {
                            final int choice = JOptionPane.showConfirmDialog(m_frame, "A file by that name already exists. Do you wish to over write it?", "Over-write?", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
                            if (choice != JOptionPane.OK_OPTION) {
                                return;
                            }
                        }
                        getGame().saveGame(f);
                        JOptionPane.showMessageDialog(m_frame, "Game Saved", "Game Saved", JOptionPane.INFORMATION_MESSAGE);
                    }
                }
            }
}
