class BackupThread extends Thread {
    public void takeSnapshot() {
        Dimension size = screen.getSize();
        BufferedImage image = new BufferedImage(size.width, size.height, BufferedImage.TYPE_INT_RGB);
        Graphics2D g = image.createGraphics();
        screen.paintAll(g);
        String filename = "";
        try {
            boolean continues = true;
            while (continues) {
                continues = false;
                fileChooserGrab.setCurrentDirectory(new File(lastScreenShot));
                if (fileChooserGrab.showSaveDialog(screen) != JFileChooser.APPROVE_OPTION) return;
                File file = fileChooserGrab.getSelectedFile();
                if (file != null) {
                    if (file.exists()) {
                        int selection = JOptionPane.showConfirmDialog(screen, "File already exist. Do you want to overwrite?");
                        if (selection == JOptionPane.YES_OPTION) {
                        } else if (selection == JOptionPane.NO_OPTION) {
                            continues = true;
                        } else {
                            return;
                        }
                    }
                    filename = file.getAbsolutePath();
                    if (!continues) {
                        OutputStream out = new FileOutputStream(filename);
                        String ext = (filename.lastIndexOf(".") == -1) ? "" : filename.substring(filename.lastIndexOf(".") + 1, filename.length());
                        ImageIO.write(image, ext, out);
                        out.close();
                        lastScreenShot = filename;
                        ssp.setStatus("Scene is saved to image : " + filename);
                    }
                }
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(screen, "Could not save the screenshot to file : \n" + filename + "\nException : " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}
