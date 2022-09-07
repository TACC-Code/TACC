class BackupThread extends Thread {
            public void windowClosing(WindowEvent ev) {
                int res = JOptionPane.showConfirmDialog(MainFrame.this, "Do you want to save changes?", "Warning", JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE);
                switch(res) {
                    case JOptionPane.YES_OPTION:
                        ZFile zf = (ZFile) tree.getModel().getRoot();
                        File f = zf.getFile();
                        String n = f.getName();
                        n = n.substring(0, n.lastIndexOf('.')) + ".java";
                        f = new File(f.getParent(), n);
                        if (f.exists()) {
                            int ret = JOptionPane.showConfirmDialog(MainFrame.getInstance(), "File " + f + " already exists.\n" + "Would you like to overwrite it?", "Overwrite?", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
                            if (ret == JOptionPane.NO_OPTION) return;
                        }
                        try {
                            IndentedWriter w = new IndentedWriter(new BufferedWriter(new FileWriter(f)));
                            zf.printJava(w);
                            w.close();
                        } catch (UnsupportedOperationException e) {
                            e.printStackTrace();
                            JOptionPane.showMessageDialog(MainFrame.getInstance(), "Syntax not yet supported " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                        } catch (IOException e) {
                            JOptionPane.showMessageDialog(MainFrame.getInstance(), "Error writing file " + f + " " + e.getMessage());
                        }
                        System.exit(0);
                    case JOptionPane.NO_OPTION:
                        System.exit(0);
                    case JOptionPane.CANCEL_OPTION:
                }
            }
}
