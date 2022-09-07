class BackupThread extends Thread {
    public JIMenuBar() {
        properties = JIConfigurator.instance().getProperties();
        menuFile = new JMenu(properties.getProperty(JIConfigurator.MENU_FILE, "File"));
        menuCategories = new JMenu(properties.getProperty(JIConfigurator.MENU_CATEGORIES, "Categories"));
        add(menuFile);
        add(menuCategories);
        export = new JMenuItem(properties.getProperty(JIConfigurator.MENU_FILE_EXPORT, "Export"));
        menuFile.add(export);
        export.addActionListener(new ActionListener() {

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
        });
        import_item = new JMenuItem(properties.getProperty(JIConfigurator.MENU_FILE_IMPORT, "Import"));
        menuFile.add(import_item);
        import_item.addActionListener(new ActionListener() {

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
        });
        exit = new JMenuItem(properties.getProperty(JIConfigurator.MENU_FILE_EXIT, "Exit"));
        menuFile.add(exit);
        exit.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                System.exit(0);
            }
        });
        JMenuItem manageCat_item = new JMenuItem(properties.getProperty(JIConfigurator.MENU_CATEGORIES_MANAGER, "Manage Categories"));
        menuCategories.add(manageCat_item);
        manageCat_item.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                (new CategoryManagerFrame()).addWindowListener(new CategoryManagerFrameWindowListener());
            }
        });
    }
}
