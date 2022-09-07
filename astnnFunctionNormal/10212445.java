class BackupThread extends Thread {
    static String getNewOutputFile(String title, String defaultDir, myFileFilter[] fileTypes) {
        if (dirOut == null) {
            dirOut = defaultDir;
        }
        if (dirOut == null) {
            dirOut = System.getProperty("user.dir");
        }
        File file = null;
        while (true) {
            JFileChooser fileChooser = new JFileChooser(dirOut);
            fileChooser.setDialogTitle(title);
            for (int i = 0; i < fileTypes.length; i++) {
                fileChooser.addChoosableFileFilter(fileTypes[i]);
            }
            fileChooser.setFileFilter(fileTypes[0]);
            int option = fileChooser.showSaveDialog(null);
            if (option == JFileChooser.APPROVE_OPTION) {
                file = fileChooser.getSelectedFile();
                String path = file.getAbsolutePath();
                if (!checkFileType(file, fileTypes)) {
                    try {
                        myFileFilter f = (myFileFilter) fileChooser.getFileFilter();
                        path += ("." + f.mask);
                        file = new File(path);
                    } catch (Exception e) {
                        JOptionPane.showMessageDialog(null, "Invalid input type", "Invalid file type", JOptionPane.INFORMATION_MESSAGE);
                        continue;
                    }
                }
                dirOut = fileChooser.getCurrentDirectory().getAbsolutePath();
                if (file.exists()) {
                    int i = JOptionPane.showConfirmDialog(null, "File already exists. Overwrite?", "Warning", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
                    if (i != JOptionPane.YES_OPTION) continue;
                }
                return path;
            } else {
                return null;
            }
        }
    }
}
