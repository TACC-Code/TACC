class BackupThread extends Thread {
    private void initDialog() {
        dialog = LDialog.createDialog(new String[] { LButton.HELP, "Close", "Export" }, mainWindow.getFrame(), true);
        dialog.setTitle(I18n.translate("Export"));
        addHelpAction(dialog, HelpContextKeys.IMPORT_EXPORT);
        exportBtn = dialog.getButton("Export");
        exportBtn.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                File file = new File(filePath.getText());
                if (file.exists()) {
                    int answer = lOptions.showOptionPane(dialog, "Selected file already exists. Do you want to overwrite it?", "Rewrite file", "Rewrite", "Cancel");
                    if (answer == 1) {
                        return;
                    }
                }
                if (isXMLFormat) {
                    exportController.export(file, lessonPicker.getPickedLessons());
                } else {
                    exportController.export(file, (Lesson) lessonCombo.getSelectedItem());
                }
            }
        });
        LButton cancel = dialog.getButton("Close");
        cancel.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                closeWindow();
            }
        });
        filePath = new JTextField();
        filePath.addKeyListener(new KeyAdapter() {

            @Override
            public void keyPressed(KeyEvent e) {
                handleButtons();
            }
        });
        cardLayout = new CardLayout();
        cardPanel = new JPanel(cardLayout);
        cardPanel.add(getLTFContent(), LTF_TAB);
        cardPanel.add(getXMLContent(), XML_TAB);
        cardLayout.show(cardPanel, XML_TAB);
        final LRadioGroupPanel radioGroupPanel = LRadioGroupPanel.createRadioButtonGroup(PanelOrientation.VERTICAL, I18n.translate("Export to XML Format"), I18n.translate("Export to LangTeacher Format (ltf)"));
        radioGroupPanel.setSelectRadioButton(0);
        radioGroupPanel.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                if (radioGroupPanel.getIndexOfSelectedRadioButton() == 0) {
                    cardLayout.show(cardPanel, XML_TAB);
                    isXMLFormat = true;
                } else {
                    cardLayout.show(cardPanel, LTF_TAB);
                    isXMLFormat = false;
                }
            }
        });
        JLabel selectFileLabel = new JLabel(translate("Export to file:"));
        LButton selectFileBtn = new LButton(I18n.translate("Choose file"));
        selectFileBtn.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                int returnVal = fileChooser.showOpenDialog(dialog);
                if (returnVal == JFileChooser.APPROVE_OPTION) {
                    final File file = fileChooser.getSelectedFile();
                    filePath.setText(file.getAbsolutePath());
                    filePath.repaint();
                    handleButtons();
                }
            }
        });
        FormLayout layout = guiUtils.getFormLayoutExport();
        DefaultFormBuilder builder = new DefaultFormBuilder(layout);
        builder.append(LGroupPanel.createSimpleGroup(radioGroupPanel, I18n.translate("Select Format of Export")), 5);
        builder.nextLine(2);
        builder.append(cardPanel, 5);
        builder.nextLine(2);
        builder.append(selectFileLabel);
        builder.append(filePath);
        builder.append(selectFileBtn);
        JPanel mainPanel = builder.getPanel();
        dialog.setMainPane(mainPanel);
    }
}
