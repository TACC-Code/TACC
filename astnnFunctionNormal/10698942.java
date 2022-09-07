class BackupThread extends Thread {
    public void actionPerformed(ActionEvent e) {
        DefaultMutableTreeNode rootNode = ChannelEditor.application.getChannelListingPanel().getRootNode();
        if (rootNode != null && rootNode.getChildCount() > 0) {
            File saveFile = null;
            try {
                final JFileChooser fc = new JFileChooser();
                FileFilter fFilter = new FileFilter() {

                    public boolean accept(File f) {
                        if (f.isDirectory()) {
                            return true;
                        }
                        if (f.getName().endsWith(".conf")) {
                            return true;
                        }
                        return false;
                    }

                    public String getDescription() {
                        return Messages.getString("SaveAsAction.3");
                    }
                };
                fc.setFileFilter(fFilter);
                int ret = fc.showSaveDialog(ChannelEditor.application);
                if (ret == JFileChooser.APPROVE_OPTION) {
                    saveFile = fc.getSelectedFile();
                    if (saveFile.exists()) {
                        int result = JOptionPane.showConfirmDialog(ChannelEditor.application, Messages.getString("SaveAsAction.4") + saveFile.getPath() + Messages.getString("SaveAsAction.5"), Messages.getString("SaveAsAction.6"), JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
                        if (result == JOptionPane.NO_OPTION) {
                            return;
                        }
                    }
                    FileWriter outFile = new FileWriter(saveFile);
                    Utils.outputChannelTree(outFile, rootNode);
                    ChannelEditor.application.setChannelFile(saveFile);
                    ChannelEditor.application.setModified(false);
                    ChannelElement channelElement = (ChannelElement) rootNode.getUserObject();
                    channelElement.setName(saveFile.getName());
                    ChannelEditor.application.getChannelListingPanel().treeNodeChanged(rootNode);
                }
            } catch (Exception ioe) {
                JOptionPane.showConfirmDialog(ChannelEditor.application, Messages.getString("SaveAsAction.7") + saveFile != null ? saveFile.getPath() : Messages.getString("SaveAsAction.8") + Messages.getString("SaveAsAction.9") + ioe.getMessage(), Messages.getString("SaveAsAction.10"), JOptionPane.CLOSED_OPTION, JOptionPane.ERROR_MESSAGE);
                ioe.printStackTrace();
            }
        }
    }
}
