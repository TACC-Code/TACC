class BackupThread extends Thread {
            @Override
            public void actionPerformed(ActionEvent e) {
                final String extension = QueleaProperties.get().getSongPackExtension();
                JFileChooser chooser = getChooser();
                int chooserResult = chooser.showSaveDialog(owner);
                if (chooserResult == JFileChooser.APPROVE_OPTION) {
                    final File file;
                    if (chooser.getSelectedFile().getName().endsWith("." + extension)) {
                        file = chooser.getSelectedFile();
                    } else {
                        file = new File(chooser.getSelectedFile().getAbsoluteFile() + "." + extension);
                    }
                    boolean writeFile = true;
                    if (file.exists()) {
                        int result = JOptionPane.showConfirmDialog(Application.get().getMainWindow(), file.getName() + " already exists. Overwrite?", "Overwrite", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE, null);
                        if (result != JOptionPane.YES_OPTION) {
                            writeFile = false;
                        }
                    }
                    if (writeFile) {
                        writeSongPack(file);
                    }
                }
            }
}
