class BackupThread extends Thread {
    private void okButton_ActionPerformed(java.awt.event.ActionEvent evt) {
        System.out.println("Ok button clicked...");
        String fileName = fileTextField.getText();
        if (fileName.length() > 0) {
            if (!fileName.endsWith(".html") || !fileName.endsWith(".htm")) {
                fileName = fileName + ".html";
            }
            if (JabutiGUI.getProject() == null) {
                JOptionPane.showMessageDialog(null, "No project is active!!!", "Warning", JOptionPane.WARNING_MESSAGE);
                return;
            }
            File repFile = new File(fileName);
            if (repFile.exists()) {
                int option = JOptionPane.showConfirmDialog(null, "File \"" + fileName + "\" already exists. Overwrite it?", "Warning", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
                if (option == JOptionPane.NO_OPTION) return;
            }
            Document htmlDoc = HTMLGen.customReport(JabutiGUI.getProject(), projectChk.isSelected(), classChk.isSelected(), methodChk.isSelected(), testSetChk.isSelected(), testCaseChk.isSelected(), testCasePathsChk.isSelected());
            System.out.println("********* Saving Custom Report *********");
            try {
                XMLPrettyPrinter.writeDocument(htmlDoc, fileName);
            } catch (Exception pce) {
                ToolConstants.reportException(pce, ToolConstants.STDERR);
                JOptionPane.showMessageDialog(null, "Error saving the file " + fileName + "!!!", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            } finally {
                htmlDoc = null;
                HTMLGen.restart();
            }
            System.out.println("****************************************");
            JOptionPane.showMessageDialog(null, "Custom Report " + fileName + " generated successfully.", "Information", JOptionPane.INFORMATION_MESSAGE);
        } else {
            JOptionPane.showMessageDialog(null, "Enter the name of the output file!!!", "Warning", JOptionPane.WARNING_MESSAGE);
            return;
        }
    }
}
