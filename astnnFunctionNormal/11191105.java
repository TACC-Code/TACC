class BackupThread extends Thread {
    public void actionPerformed(ActionEvent event) {
        JFileChooser chooser = new JFileChooser();
        chooser.setCurrentDirectory(this.jcpPanel.getCurrentWorkDirectory());
        JCPExportFileFilter.addChoosableFileFilters(chooser);
        if (currentFilter != null) {
            {
                for (int i = 0; i < chooser.getChoosableFileFilters().length; i++) {
                    if (chooser.getChoosableFileFilters()[i].getDescription().equals(currentFilter.getDescription())) chooser.setFileFilter(chooser.getChoosableFileFilters()[i]);
                }
            }
        }
        chooser.setFileView(new JCPFileView());
        int returnVal = chooser.showSaveDialog(jcpPanel);
        currentFilter = chooser.getFileFilter();
        if (returnVal == JFileChooser.APPROVE_OPTION) {
            if (!(currentFilter instanceof IJCPFileFilter)) {
                JOptionPane.showMessageDialog(jcpPanel, GT._("Please chose a file type!"), GT._("No file type chosen"), JOptionPane.INFORMATION_MESSAGE);
                return;
            } else {
                type = ((JCPExportFileFilter) currentFilter).getType();
                File outFile = new File(chooser.getSelectedFile().getAbsolutePath());
                String fileName = outFile.toString();
                if (!fileName.endsWith("." + type)) {
                    fileName += "." + type;
                    outFile = new File(fileName);
                }
                if (outFile.exists()) {
                    String message = GT._("File already exists. Do you want to overwrite it?");
                    String title = GT._("File already exists");
                    int value = JOptionPane.showConfirmDialog(jcpPanel, message, title, JOptionPane.YES_NO_OPTION);
                    if (value == JOptionPane.NO_OPTION) {
                        return;
                    }
                }
                if (type.equals(JCPExportFileFilter.svg)) {
                    try {
                        String svg = this.jcpPanel.getSVGString();
                        FileWriter writer = new FileWriter(outFile);
                        writer.append(svg);
                        writer.flush();
                        JOptionPane.showMessageDialog(jcpPanel, GT._("Exported image to") + " " + outFile);
                        return;
                    } catch (IOException e) {
                        String error = GT._("Problem exporting to svg");
                        JOptionPane.showMessageDialog(jcpPanel, error);
                        return;
                    }
                } else {
                    RenderedImage image = (RenderedImage) this.jcpPanel.takeSnapshot();
                    try {
                        String imageIOType;
                        if (type.equals(JCPExportFileFilter.bmp)) {
                            imageIOType = "BMP";
                        } else if (type.equals(JCPExportFileFilter.jpg)) {
                            imageIOType = "JPEG";
                        } else {
                            imageIOType = "PNG";
                        }
                        boolean succeeded = ImageIO.write(image, imageIOType, outFile);
                        if (succeeded) {
                            JOptionPane.showMessageDialog(jcpPanel, GT._("Exported image to") + " " + outFile);
                            return;
                        } else {
                            ImageIO.write(image, "PNG", outFile);
                            JOptionPane.showMessageDialog(jcpPanel, GT._("Exported image to") + " " + outFile + " " + GT._("as PNG, since") + " " + type + " " + GT._("could not be written"));
                            return;
                        }
                    } catch (IOException ioe) {
                        ioe.printStackTrace();
                        JOptionPane.showMessageDialog(jcpPanel, GT._("Problem exporting image"));
                    }
                }
            }
        } else if (returnVal == JFileChooser.CANCEL_OPTION) {
            return;
        }
    }
}
