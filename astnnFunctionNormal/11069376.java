class BackupThread extends Thread {
    protected void _init() {
        propertyData = new PropertyData();
        JPanel panel = new JPanel(new LambdaLayout());
        panel.setBorder(new EmptyBorder(6, 6, 6, 6));
        JLabel prop_name_label = new JLabel("Property name:");
        final JComboBox prop_chooser = new JComboBox(isDirectory ? default_dir_prop_names : default_file_prop_names);
        prop_chooser.setEditable(true);
        prop_chooser.setSelectedItem(name == null ? "" : name);
        JPanel content_panel = new JPanel(new LambdaLayout());
        content_panel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(), "Property value"));
        final JRadioButton text_btn = new JRadioButton("Enter a text value:");
        text_btn.setSelected(true);
        final JRadioButton file_btn = new JRadioButton("Or load value from file:");
        ButtonGroup bg = new ButtonGroup();
        bg.add(text_btn);
        bg.add(file_btn);
        final JTextArea text_value = new JTextArea(8, 30);
        if (value != null) {
            text_value.setText(value);
        }
        final JTextField file_value = new JTextField(30);
        file_value.setEnabled(false);
        final JButton browse_btn = new JButton("Browse...");
        browse_btn.setEnabled(false);
        browse_btn.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent ae) {
                String[] filename = GUIUtilities.showVFSFileDialog(view, PVHelper.getProjectRoot(view), VFSBrowser.OPEN_DIALOG, false);
                if (filename != null && filename.length > 0) {
                    file_value.setText(filename[0]);
                }
            }
        });
        ActionListener al = new ActionListener() {

            public void actionPerformed(ActionEvent ae) {
                file_value.setEnabled(file_btn.isSelected());
                browse_btn.setEnabled(file_btn.isSelected());
                text_value.setEnabled(text_btn.isSelected());
            }
        };
        text_btn.addActionListener(al);
        file_btn.addActionListener(al);
        content_panel.add("0, 0, 7, 1, W, w, 3", text_btn);
        content_panel.add("0, 1, 1, 1", KappaLayout.createHorizontalStrut(11, true));
        content_panel.add("1, 1, 6, 1, 0, wh, 3", new JScrollPane(text_value));
        content_panel.add("0, 2, 7, 1, W, w, 3", file_btn);
        content_panel.add("0, 3, 1, 1", KappaLayout.createHorizontalStrut(11, true));
        content_panel.add("1, 3, 5, 1, W, w, 3", file_value);
        content_panel.add("6, 3, 1, 1, E,  , 3", browse_btn);
        final JCheckBox recursive_cb = new JCheckBox("Apply recursively?");
        recursive_cb.setSelected(false);
        recursive_cb.setEnabled(isDirectory);
        recursive_cb.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent ae) {
                propertyData.setRecursive(recursive_cb.isSelected());
            }
        });
        KappaLayout kl = new KappaLayout();
        JPanel btn_panel = new JPanel(kl);
        JButton ok_btn = new JButton("Ok");
        JButton cancel_btn = new JButton("Cancel");
        btn_panel.add("0, 0, 1, 1, 0, w, 3", ok_btn);
        btn_panel.add("1, 0, 1, 1, 0, w, 3", cancel_btn);
        kl.makeColumnsSameWidth(0, 1);
        ok_btn.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent ae) {
                Object item = prop_chooser.getSelectedItem();
                if (item != null && !item.toString().isEmpty()) {
                    propertyData.setName(item.toString());
                    if (text_btn.isSelected()) {
                        propertyData.setValue(text_value.getText() == null ? "" : text_value.getText());
                    } else {
                        String filename = file_value.getText();
                        if (filename == null || filename.isEmpty()) {
                            JOptionPane.showMessageDialog(view, "No filename entered for property value.", "Error", JOptionPane.ERROR_MESSAGE);
                            file_value.requestFocusInWindow();
                            return;
                        }
                        try {
                            Reader reader = new BufferedReader(new FileReader(filename));
                            StringWriter writer = new StringWriter();
                            FileUtilities.copy(reader, writer);
                            propertyData.setValue(writer.toString());
                        } catch (Exception e) {
                            JOptionPane.showMessageDialog(view, "Unable to read property value from file:\n" + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                            return;
                        }
                    }
                }
                PropertyEditor.this.setVisible(false);
                PropertyEditor.this.dispose();
            }
        });
        cancel_btn.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent ae) {
                propertyData = null;
                PropertyEditor.this.setVisible(false);
                PropertyEditor.this.dispose();
            }
        });
        panel.add("0, 0, 1, 1, W,  , 6", prop_name_label);
        panel.add("1, 0, 5, 1, W, w, 4", prop_chooser);
        panel.add("0, 1, 6, 1, W, wh, 3", content_panel);
        panel.add("0, 2, 1, 1, 0,  , 0", KappaLayout.createVerticalStrut(6, true));
        panel.add("0, 3, 6, 1, W,  , 6", recursive_cb);
        panel.add("0, 4, 1, 1, 0,  , 0", KappaLayout.createVerticalStrut(10, true));
        panel.add("0, 5, 6, 1, E,  , 6", btn_panel);
        setContentPane(panel);
        pack();
    }
}
