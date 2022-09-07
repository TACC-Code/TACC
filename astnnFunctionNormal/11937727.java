class BackupThread extends Thread {
            @Override
            public void actionPerformed(ActionEvent e) {
                JFileChooser chooser = new JFileChooser();
                FileNameExtensionFilter filter = new FileNameExtensionFilter("*.xml", "xml");
                chooser.setFileFilter(filter);
                int returnVal = chooser.showSaveDialog(getParent());
                if (returnVal == JFileChooser.APPROVE_OPTION) {
                    try {
                        int choice = 0;
                        if (!chooser.getSelectedFile().toString().toLowerCase().endsWith(".xml".toLowerCase())) chooser.setSelectedFile(new File(chooser.getSelectedFile().toString() + ".xml"));
                        if (chooser.getSelectedFile().exists()) {
                            choice = JOptionPane.showConfirmDialog(chooser, "Sovrascrivere il file?");
                        }
                        ;
                        if (choice == 0) {
                            FileUtils.copyFile(DomXml.getTagFile(), chooser.getSelectedFile());
                            JOptionPane.showMessageDialog(chooser, "File dei tag esportato con successo!");
                        }
                    } catch (IOException e1) {
                        e1.printStackTrace();
                    }
                }
            }
}
