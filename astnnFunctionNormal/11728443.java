class BackupThread extends Thread {
    public void getRemoteFiles(SftpClient sftp, java.util.List files, long totalBytes) {
        this.files = files;
        this.totalBytes = totalBytes;
        this.sftp = sftp;
        lblTargetAction.setText("Downloading to:");
        progressbar.setMaximum(100);
        if ((totalBytes / 1073741824) > 0) {
            formattedTotal = formatMb.format((double) totalBytes / 1073741824) + " GB";
        } else if ((totalBytes / 1048576) > 0) {
            formattedTotal = formatMb.format((double) totalBytes / 1048576) + " MB";
        } else {
            formattedTotal = formatKb.format((double) totalBytes / 1024) + " KB";
        }
        Thread thread = new Thread(new Runnable() {

            int i;

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
        });
        thread.start();
        try {
            setVisible(true);
        } catch (NullPointerException e) {
            setVisible(true);
        }
    }
}
