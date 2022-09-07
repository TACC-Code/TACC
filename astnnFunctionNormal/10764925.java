class BackupThread extends Thread {
    private void bnOutputBrowseActionPerformed(java.awt.event.ActionEvent evt) {
        JFileChooser chooser = new JFileChooser(new File(System.getProperty("user.home")));
        chooser.setFileFilter(new FileFilter() {

            @Override
            public String getDescription() {
                return ".csv";
            }

            @Override
            public boolean accept(File file) {
                boolean status = false;
                try {
                    String fileName = file.getName().toLowerCase();
                    status = fileName.endsWith(".csv");
                } catch (Exception e) {
                    e.printStackTrace();
                }
                return status;
            }
        });
        int i = chooser.showSaveDialog(this);
        chooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
        if (i == JFileChooser.APPROVE_OPTION) {
            String file = chooser.getSelectedFile().toString();
            StringTokenizer str = new StringTokenizer(file, ".");
            if (str.countTokens() <= 2) {
                if (str.countTokens() == 1) {
                    createFile = new File(chooser.getSelectedFile().toString() + ".csv");
                    if (createFile.exists()) {
                        int cnt = JOptionPane.showConfirmDialog(this, "This file already exists ! Are you sure \n you want to over write it.", "check", JOptionPane.OK_CANCEL_OPTION);
                        if (cnt == 0) {
                            jTextField2.setText(createFile.toString());
                            System.out.println("override");
                        } else {
                            createFile = null;
                            jTextField2.setText("");
                        }
                    } else {
                        jTextField2.setText(createFile.toString());
                    }
                } else {
                    str.nextToken();
                    String s1 = str.nextToken(".");
                    if (s1.equalsIgnoreCase("csv")) {
                        createFile = new File(chooser.getSelectedFile().toString());
                        if (createFile.exists()) {
                            int cnt = JOptionPane.showConfirmDialog(this, "This file already exists ! Are you sure \n you want to over write it.", "check", JOptionPane.OK_CANCEL_OPTION);
                            if (cnt == 0) {
                                jTextField2.setText(createFile.toString());
                                System.out.println("override");
                            } else {
                                createFile = null;
                                jTextField2.setText("");
                            }
                        } else {
                            jTextField2.setText(createFile.toString());
                        }
                    } else {
                        JOptionPane.showMessageDialog(this, "The given file is not in .csv format \n Please create .csv extension.", "check", JOptionPane.CANCEL_OPTION);
                    }
                }
            } else {
                JOptionPane.showMessageDialog(this, "The given file name is not correct \n Please create a new file.", "check", JOptionPane.YES_OPTION);
            }
        } else {
            jTextField2.setText("");
        }
    }
}
