class BackupThread extends Thread {
            public void run() {
                String pwd = FileTransferDialog.this.sftp.pwd();
                String lpwd = FileTransferDialog.this.sftp.lpwd();
                try {
                    for (i = 0; i < FileTransferDialog.this.files.size(); i++) {
                        File file = new File(lpwd + File.separator + FileTransferDialog.this.files.get(i));
                        if (file.exists()) {
                            int result = JOptionPane.showConfirmDialog(FileTransferDialog.this, "This file already exists are you sure you wish to overwrite?\n\n" + file.getName(), "Confirm File Overwrite", JOptionPane.YES_NO_CANCEL_OPTION);
                            if (result == JOptionPane.NO_OPTION) {
                                if (i == FileTransferDialog.this.files.size() - 1) {
                                    cancelOperation();
                                    return;
                                } else {
                                    String ff = pwd + "/" + FileTransferDialog.this.files.get(i);
                                    FileAttributes attrs = FileTransferDialog.this.sftp.stat(ff);
                                    started(attrs.getSize().longValue(), ff);
                                    progressed(attrs.getSize().longValue());
                                    completed();
                                    continue;
                                }
                            }
                            if (result == JOptionPane.CANCEL_OPTION) {
                                cancelOperation();
                                return;
                            }
                        }
                        setSource(pwd + "/" + FileTransferDialog.this.files.get(i));
                        setTarget(lpwd + File.separator + FileTransferDialog.this.files.get(i));
                        if (!cancelled) {
                            FileTransferDialog.this.sftp.get((String) FileTransferDialog.this.files.get(i), FileTransferDialog.this);
                        }
                    }
                    notifyWaiting();
                } catch (IOException ex) {
                    if (!cancelled) {
                        SshToolsApplicationPanel.showErrorMessage(FileTransferDialog.this, "The file operation failed!\n" + ex.getMessage(), "File Transfer", ex);
                        File file = new File(lpwd + File.separator + FileTransferDialog.this.files.get(i));
                        if (file.exists()) {
                            file.delete();
                        }
                        setVisible(false);
                    }
                }
            }
}
