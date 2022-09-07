class BackupThread extends Thread {
    protected boolean isBizDriverPanelValid() {
        String fname = fileChooser.getFileString().trim();
        fileChooser.getTextField().selectAll();
        if (fname.equals("")) {
            ControlFactory.showMessageDialog(t.getString("Enter BizDriver file name."), t.getString("Information"));
            return false;
        }
        File bizDriverFile = ResourceUtils.getFile(fname);
        if (bizDriverFile.isDirectory() && !useExistingBtn.isEnabled()) {
            ControlFactory.showMessageDialog(t.getString("Enter BizDriver file name."), t.getString("Information"));
            return false;
        }
        if (bizDriverFile.getName().trim().equals(".xdr")) {
            ControlFactory.showMessageDialog(t.getString("Enter BizDriver file name."), t.getString("Information"));
            return false;
        }
        if ((bizDriverFile.getAbsolutePath().indexOf('#') > -1) || (bizDriverFile.getAbsolutePath().indexOf('%') > -1)) {
            ControlFactory.showMessageDialog(t.getString("Please do not enter # or % in the file path or name."), t.getString("Information"));
            return false;
        }
        if (filePathCmb.getText().trim().equals("")) {
            ControlFactory.showMessageDialog(t.getString("Please enter a file name."), t.getString("Information"));
            return false;
        }
        String dirName = bizDriverFile.getParent();
        if ((dirName == null) || dirName.trim().equals("")) {
            dirName = Path.fromOSString(initPath).addTrailingSeparator().toString();
            fileChooser.setFileString(dirName + fileChooser.getFileString());
            fileChooser.getTextField().selectAll();
        } else if (dirName.trim().equals(File.separator)) {
            dirName = initPath;
            fileChooser.setFileString(dirName.substring(0, dirName.length() - 1) + fileChooser.getFileString());
            fileChooser.getTextField().selectAll();
        } else {
            dirName = dirName.trim();
            if (Path.fromOSString(dirName).hasTrailingSeparator()) {
                fileChooser.setFileString(dirName + bizDriverFile.getName().trim());
                fileChooser.getTextField().selectAll();
            } else {
                fileChooser.setFileString(fname);
                fileChooser.getTextField().selectAll();
            }
        }
        final File tmpFile = ResourceUtils.getDirectory(dirName);
        if (!tmpFile.isDirectory()) {
            ControlFactory.showMessageDialog(t.getString("Invalid directory for BizDriver file."), t.getString("Information"));
            return false;
        }
        fname = fileChooser.getFileString().trim();
        bizDriverFile = ResourceUtils.getFile(fname);
        if (createNewBtn.getSelection()) {
            final String fileNameStr = bizDriverFile.getName();
            if (fileNameStr.indexOf('.') == -1) {
                fname = fname + ".xdr";
                fileChooser.setFileString(fname);
                fileChooser.getTextField().selectAll();
                bizDriverFile = new File(fname);
            } else if (fileNameStr.indexOf('.') == (fileNameStr.length() - 1)) {
                fname = fname + "xdr";
                fileChooser.setFileString(fname);
                fileChooser.getTextField().selectAll();
                bizDriverFile = new File(fname);
            }
            if (!saved && bizDriverFile.exists()) {
                final int choice = ControlFactory.showConfirmDialog("BizDriver file which you have entered already exists. Overwrite?");
                if (choice != MessageDialog.OK) {
                    return false;
                }
            }
        } else if (useExistingBtn.getSelection()) {
            if (bizDriverFile.isDirectory() || !bizDriverFile.exists()) {
                ControlFactory.showMessageDialog(t.getString("BizDriver file which you have entered does not exist. Please enter correct file name."), t.getString("Information"));
                return false;
            }
        }
        if (requestTypeCmb.getText().equals(FileBizDriverInfo.FILE_WRITE) || requestTypeCmb.getText().equals(FileBizDriverInfo.FILE_APPEND)) {
            final File file = ResourceUtils.getFile(filePathCmb.getText().trim());
            if (file.isFile() && !useExistingBtn.getSelection()) {
                final int ret = ControlFactory.showConfirmDialog("File exists. Continue?", "Information", false);
                if (ret != MessageDialog.OK) {
                    return false;
                }
            }
        }
        if (!isInputParam(filePathCmb.getText().trim())) {
            if (requestTypeCmb.getText().equals(FileBizDriverInfo.FILE_READ)) {
                final File file = ResourceUtils.getFile(filePathCmb.getText().trim());
                if (!file.isFile()) {
                    ControlFactory.showMessageDialog(t.getString("Please enter existing file name or " + "valid input parameter references."), t.getString("Information"));
                    return false;
                }
            } else {
                final File file = ResourceUtils.getFile(filePathCmb.getText().trim());
                String parentFile = file.getParent();
                if (parentFile == null) {
                    if (!isInputParam(filePathCmb.getText().trim())) {
                        ControlFactory.showMessageDialog(t.getString("Please enter valid file path or" + " valid input parameter references." + "Instead of: " + file.getPath()), t.getString("Information"));
                        return false;
                    }
                    parentFile = ".";
                }
                logger.finest(parentFile);
                if (!new File(parentFile).isDirectory()) {
                    ControlFactory.showMessageDialog(t.getString("Please enter valid file path. Instead of: " + file.getPath()), t.getString("Information"));
                    return false;
                }
                if (file.isDirectory()) {
                    ControlFactory.showMessageDialog(t.getString("Please enter a file name."), t.getString("Information"));
                    return false;
                }
            }
        }
        return true;
    }
}
