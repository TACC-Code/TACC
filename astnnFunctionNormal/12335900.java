class BackupThread extends Thread {
            @Override
            public void actionPerformed(ActionEvent evt) {
                JFileChooser fileDialog = new JFileChooser(".");
                fileDialog.setFileFilter(ReaderData.mkExtensionFileFilter(".grp", "Group Files"));
                int outcome = fileDialog.showOpenDialog((Frame) null);
                if (outcome == JFileChooser.APPROVE_OPTION) {
                    assert (fileDialog.getCurrentDirectory() != null);
                    assert (fileDialog.getSelectedFile() != null);
                    String fileName = fileDialog.getCurrentDirectory().toString() + File.separator + fileDialog.getSelectedFile().getName();
                    BufferedReader fileReader = null;
                    try {
                        fileReader = new BufferedReader(new FileReader(fileName));
                        ReaderWriterGroup.read(fileReader, writer);
                        fileReader.close();
                    } catch (Exception e) {
                        System.err.println("Exception while reading from file '" + fileName + "'.");
                        System.err.println(e);
                    }
                } else if (outcome == JFileChooser.CANCEL_OPTION) {
                }
            }
}
