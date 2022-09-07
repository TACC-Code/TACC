class BackupThread extends Thread {
    private void showCapturePanel() {
        JFileChooser fileChooser = new JFileChooser(LMPreferences.getLastDirectory());
        fileChooser.setApproveButtonText(GUIMessages.getString("GUI.save"));
        fileChooser.setDialogTitle(GUIMessages.getString("GUI.saveCurrentLatticeAsImage"));
        fileChooser.setAcceptAllFileFilterUsed(false);
        fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
        fileChooser.setSelectedFile(new File(glCurrent.getName() + GUIMessages.getString("GUI.latticeDefaultImageName")));
        ExampleFileFilter filterBMP = new ExampleFileFilter("bmp", GUIMessages.getString("GUI.bmpFormat"));
        fileChooser.addChoosableFileFilter(filterBMP);
        ExampleFileFilter filterJPG = new ExampleFileFilter("jpg", GUIMessages.getString("GUI.jpgFormat"));
        fileChooser.addChoosableFileFilter(filterJPG);
        ExampleFileFilter filterPNG = new ExampleFileFilter("png", GUIMessages.getString("GUI.pngFormat"));
        fileChooser.addChoosableFileFilter(filterPNG);
        int returnVal = fileChooser.showSaveDialog(this);
        if (returnVal == JFileChooser.APPROVE_OPTION) {
            File fileName = fileChooser.getSelectedFile();
            ExampleFileFilter currentFilter = (ExampleFileFilter) fileChooser.getFileFilter();
            ArrayList<String> extensions = currentFilter.getExtensionsList();
            String oldFileType = ExampleFileFilter.getExtension(fileName);
            if (extensions != null && !extensions.contains(oldFileType)) {
                String newFileType = extensions.get(0);
                String oldFileName = fileName.getAbsolutePath();
                int posOldExt = oldFileName.lastIndexOf(".");
                String newFileName = oldFileName + "." + newFileType;
                if (posOldExt != -1) newFileName = newFileName.substring(0, posOldExt) + "." + newFileType;
                fileName = new File(newFileName);
            }
            if (fileName.exists()) {
                int overwrite = DialogBox.showDialogWarning(this, GUIMessages.getString("GUI.doYouWantToOverwriteFile"), GUIMessages.getString("GUI.selectedFileAlreadyExist"));
                if (overwrite == DialogBox.NO) {
                    DialogBox.showMessageInformation(this, GUIMessages.getString("GUI.imageNotSaved"), GUIMessages.getString("GUI.notSaved"));
                    return;
                }
            }
            try {
                ScreenImage.createImage(viewer, fileName.getAbsolutePath());
                DialogBox.showMessageInformation(this, GUIMessages.getString("GUI.imageSuccessfullySaved"), GUIMessages.getString("GUI.saveSuccess"));
            } catch (IOException ioe) {
                DialogBox.showMessageError(this, GUIMessages.getString("GUI.imageCouldnotBeSaved"), GUIMessages.getString("GUI.errorWithFile"));
            }
            LMPreferences.setLastDirectory(fileChooser.getCurrentDirectory().getAbsolutePath());
        }
    }
}
