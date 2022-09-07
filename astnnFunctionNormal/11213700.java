class BackupThread extends Thread {
        public void actionPerformed(ActionEvent e) {
            if (null == nip.getImage()) return;
            String name = getTitle();
            int i = name.lastIndexOf('/');
            int j = name.lastIndexOf('.');
            if (i >= 0) {
                name = name.substring(i, j >= 0 ? j : name.length());
                name = name.trim();
            }
            name += ".png";
            jfc.setSelectedFile(new File(name));
            jfc.showSaveDialog(SpInfoViewer.this);
            final File selected = jfc.getSelectedFile();
            if (!selected.getName().toLowerCase().endsWith(".png")) {
                JOptionPane.showMessageDialog(SpInfoViewer.this, "The filename must end with .png", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            if (selected.exists()) {
                if (JOptionPane.YES_OPTION != JOptionPane.showConfirmDialog(SpInfoViewer.this, "Overwrite existing file?", "File already exists", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE)) return;
            }
            try {
                ImageIO.write(nip.getImage(), "png", selected);
            } catch (IOException x) {
                ErrorInfo ei = new ErrorInfo("Error", "Failed to save image", null, null, x, null, null);
                JXErrorPane.showDialog(null, ei);
            }
        }
}
