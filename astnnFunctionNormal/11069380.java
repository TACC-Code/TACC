class BackupThread extends Thread {
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
}
