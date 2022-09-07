class BackupThread extends Thread {
    public String promptFile(String defaultFilename, String[][] suffices, boolean saveMode, boolean directoryMode) {
        JFileChooser fc = new JFileChooser();
        if (!directoryMode) {
            final boolean save = saveMode;
            if (suffices != null) {
                for (int i = 0; i < suffices.length; i++) {
                    final String suffix = suffices[i][0];
                    final String description = suffices[i][1];
                    FileFilter ff = new FileFilter() {

                        public boolean accept(File file) {
                            if (file.isDirectory()) return true;
                            String name = file.getName();
                            return suffix.equals("*") || name.endsWith("." + suffix) || (save && fileDoesntExistAndDoesntEndWithAnySuffix(file));
                        }

                        public String getDescription() {
                            return "." + suffix + " - " + description;
                        }
                    };
                    fc.addChoosableFileFilter(ff);
                }
            }
        } else {
            fc.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        }
        int r;
        if (defaultFilename != null) {
            File file = new File(defaultFilename);
            fc.setSelectedFile(file);
        }
        if (saveMode) {
            r = fc.showSaveDialog(this);
        } else {
            r = fc.showOpenDialog(this);
        }
        if (r == JFileChooser.APPROVE_OPTION) {
            File file = fc.getSelectedFile();
            String name = file.getName();
            String extraSuffix = "";
            if (name.indexOf('.') == -1) {
                if (suffices != null && suffices.length > 0) {
                    extraSuffix = "." + suffices[0][0];
                }
            }
            String filename = file.getAbsolutePath() + extraSuffix;
            if (saveMode) {
                File fl = new File(filename);
                if (fl.exists()) {
                    if (!confirm("File " + filename + " already exists. Overwrite?")) {
                        return null;
                    }
                }
            }
            return filename;
        } else {
            return null;
        }
    }
}
