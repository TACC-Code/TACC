class BackupThread extends Thread {
            @Override
            public void actionPerformed(ActionEvent e) {
                JFileChooser chooser = new JFileChooser();
                FileNameExtensionFilter filter = new FileNameExtensionFilter("*.xml", "xml");
                chooser.setFileFilter(filter);
                int returnVal = chooser.showOpenDialog(getParent());
                if (returnVal == JFileChooser.APPROVE_OPTION) {
                    try {
                        int choice = -22;
                        if (chooser.getSelectedFile().toString().toLowerCase().endsWith(".xml") && DomXml.isJimagickSource(chooser.getSelectedFile())) {
                            choice = JOptionPane.showConfirmDialog(chooser, "Sovrascrivere il file?");
                            if (choice == 0) {
                                FileUtils.copyFile(chooser.getSelectedFile(), DomXml.getTagFile());
                                DomXml.reset();
                                JOptionPane.showMessageDialog(chooser, "File dei tag importato con successo!");
                                ArrayList<String> categories = DomXml.loadAllCategories();
                                Collections.sort(categories);
                                GUI.instance(false).getCatModel().clear();
                                GUI.instance(false).getSearchPanel().getModel().clear();
                                for (String string : categories) {
                                    GUI.instance(false).getCatModel().addElement(string, false);
                                    GUI.instance(false).getSearchPanel().getModel().addElement(string, false);
                                }
                                GUI.instance(false).getCatModel().reload();
                                GUI.instance(false).getSearchPanel().getModel().reload();
                                GUI.instance(false).getCatPanel().updateModelBeforeSaving();
                            }
                        } else JOptionPane.showMessageDialog(chooser, "Il file selezionato non è compatibile con JIMagick!");
                    } catch (IOException e1) {
                        e1.printStackTrace();
                    }
                }
            }
}
