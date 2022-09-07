class BackupThread extends Thread {
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
}
