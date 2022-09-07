class BackupThread extends Thread {
    private void saveCurrentLatticeAs() {
        if (!glCurrent.isNested()) {
            ConceptLattice lattice = glCurrent.getNestedLattice().getConceptLattice();
            JFileChooser fileChooser = new JFileChooser(LMPreferences.getLastDirectory());
            fileChooser.setApproveButtonText(GUIMessages.getString("GUI.saveAs"));
            fileChooser.setDialogTitle(GUIMessages.getString("GUI.saveCurrentLattice"));
            fileChooser.setAcceptAllFileFilterUsed(false);
            fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
            ExampleFileFilter filterGaliciaXml = new ExampleFileFilter("lat.xml", GUIMessages.getString("GUI.galiciaXMLLatticeFormat"));
            fileChooser.addChoosableFileFilter(filterGaliciaXml);
            int returnVal = fileChooser.showSaveDialog(this);
            if (returnVal == JFileChooser.APPROVE_OPTION) {
                File fileName = fileChooser.getSelectedFile();
                ExampleFileFilter currentFilter = (ExampleFileFilter) fileChooser.getFileFilter();
                ArrayList<String> extensions = currentFilter.getExtensionsList();
                String oldFileType = ExampleFileFilter.getExtension(fileName);
                String newFileType = oldFileType;
                if (extensions != null && !extensions.contains(oldFileType)) {
                    newFileType = extensions.get(0);
                    String oldFileName = fileName.getAbsolutePath();
                    int posOldExt = oldFileName.lastIndexOf(".");
                    String newFileName = oldFileName + "." + newFileType;
                    if (posOldExt != -1) newFileName = newFileName.substring(0, posOldExt) + "." + newFileType;
                    fileName = new File(newFileName);
                }
                if (fileName.exists()) {
                    int overwrite = DialogBox.showDialogWarning(this, GUIMessages.getString("GUI.doYouWantToOverwriteFile"), GUIMessages.getString("GUI.selectedFileAlreadyExist"));
                    if (overwrite == DialogBox.NO) {
                        DialogBox.showMessageInformation(this, GUIMessages.getString("GUI.latticeNotSaved"), GUIMessages.getString("GUI.notSaved"));
                        return;
                    }
                }
                try {
                    String fileType = ExampleFileFilter.getExtension(fileName);
                    if (fileType.equals("lat.xml")) {
                        new GaliciaXMLLatticeWriter(fileName, lattice);
                    } else {
                        DialogBox.showMessageError(this, GUIMessages.getString("GUI.latticeExtensionNotKnown"), GUIMessages.getString("GUI.wrongLatticeFormat"));
                        return;
                    }
                    DialogBox.showMessageInformation(this, GUIMessages.getString("GUI.latticeSuccessfullySaved"), GUIMessages.getString("GUI.saveSuccess"));
                } catch (IOException ioe) {
                    DialogBox.showMessageError(this, GUIMessages.getString("GUI.latticeCouldnotBeSaved"), GUIMessages.getString("GUI.errorWithFile"));
                } catch (WriterException e) {
                    DialogBox.showMessageError(this, e);
                }
                LMPreferences.setLastDirectory(fileChooser.getCurrentDirectory().getAbsolutePath());
            }
        }
    }
}
