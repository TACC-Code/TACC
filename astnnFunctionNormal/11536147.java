class BackupThread extends Thread {
    public void saveGraphFile() {
        if (this.currentDirectory.equals("")) {
            this.fc = new JFileChooser();
        } else {
            this.fc = new JFileChooser(this.currentDirectory);
        }
        int returnVal = fc.showSaveDialog(this);
        if (returnVal == JFileChooser.APPROVE_OPTION) {
            File file = fc.getSelectedFile();
            final String fileName = file.getAbsolutePath();
            if (file.exists()) {
                final JFrame overwriteMessage = new JFrame();
                overwriteMessage.setTitle("Overwrite file?");
                overwriteMessage.setLocationRelativeTo(fc);
                JLabel title = new JLabel(" " + fileName);
                title.setFont(new Font("Tahoma", 0, 12));
                JLabel title2 = new JLabel(" File already exists, overwrite it?");
                title.setFont(new Font("Tahoma", 0, 12));
                JButton saveButton = new JButton();
                saveButton.setText("Yes");
                saveButton.setToolTipText("Overwrite file?");
                saveButton.addMouseListener(new java.awt.event.MouseAdapter() {

                    public void mouseClicked(java.awt.event.MouseEvent evt) {
                        ResultXML xml = new ResultXML();
                        xml.writeFile(graph.getDataGraph(), fileName);
                        overwriteMessage.setVisible(false);
                    }
                });
                JButton noButton = new JButton();
                noButton.setText("No");
                noButton.setToolTipText("Overwrite file");
                noButton.addMouseListener(new java.awt.event.MouseAdapter() {

                    public void mouseClicked(java.awt.event.MouseEvent evt) {
                        overwriteMessage.setVisible(false);
                    }
                });
                JPanel buttons = new JPanel();
                GridLayout buttonLayout = new GridLayout(1, 2);
                buttonLayout.addLayoutComponent("savebutton", saveButton);
                buttonLayout.addLayoutComponent("nobutton", noButton);
                buttons.add(saveButton);
                buttons.add(noButton);
                buttons.setLayout(buttonLayout);
                JPanel complete = new JPanel();
                GridLayout layout = new GridLayout(3, 1);
                layout.addLayoutComponent("title", title);
                layout.addLayoutComponent("title2", title2);
                layout.addLayoutComponent("buttons", buttons);
                complete.add(title);
                complete.add(title2);
                complete.add(buttons);
                complete.setLayout(layout);
                Dimension d = new Dimension(450, 100);
                overwriteMessage.setSize(d);
                overwriteMessage.setContentPane(complete);
                overwriteMessage.setVisible(true);
            } else {
                String xmlExtension = ".xml";
                String fileName2 = fileName;
                if (!fileName.contains(xmlExtension)) fileName2 = file.getAbsolutePath() + xmlExtension;
                ResultXML xml = new ResultXML();
                xml.writeFile(this.graph.getDataGraph(), fileName2);
            }
            this.currentDirectory = file.getPath();
        }
    }
}
