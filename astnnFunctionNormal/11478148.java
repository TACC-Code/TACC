class BackupThread extends Thread {
            @Override
            public void run() {
                Socket socket = null;
                BufferedInputStream bi = null;
                BufferedOutputStream bo = null;
                try {
                    try {
                        running = true;
                        updateStatusLabel("Transfer starting...");
                        serverSocket = new ServerSocket(Settings.getFiletTansferPort());
                        socket = serverSocket.accept();
                        serverSocket.close();
                    } catch (Exception e) {
                        if (e instanceof java.net.BindException) {
                            serverSocket.close();
                            Protocol.turnTransferAround(sender, filename);
                            Thread.sleep(500);
                            startDownloadingRetry();
                            return;
                        }
                    }
                    bi = new BufferedInputStream(socket.getInputStream());
                    destfile = getDestFile();
                    if (!resuming) {
                        int n = 1;
                        while (!destfile.createNewFile()) {
                            destfile = Checkfile(destfile, n);
                            n++;
                        }
                    } else {
                        if (!destfile.exists()) {
                            destfile.createNewFile();
                        }
                    }
                    bo = new BufferedOutputStream(new FileOutputStream(destfile, resuming));
                    updateStatusLabel("Transferring...");
                    starttime = System.currentTimeMillis();
                    int readedbyte;
                    long recievedBytes = 0;
                    long currenttime;
                    long timeelapsed;
                    long tmpCounter = 0;
                    while (running && (readedbyte = bi.read()) != -1) {
                        bo.write(readedbyte);
                        ++recievedBytes;
                        ++tmpCounter;
                        if (tmpCounter >= onePercentInBytes) {
                            tmpCounter = 0;
                            bo.flush();
                            progress = (int) ((((recievedBytes + firstByteToSend) * 1.0) / (totalsize)) * 100);
                            SwingUtilities.invokeLater(new Runnable() {

                                @Override
                                public void run() {
                                    pgb_progress.setValue(progress);
                                }
                            });
                            currenttime = System.currentTimeMillis();
                            timeelapsed = currenttime - starttime;
                            timeelapsed = timeelapsed / 1000;
                            setTimeLeft((long) (((totalsize - firstByteToSend) - recievedBytes) / (recievedBytes * 1.0) * timeelapsed));
                        }
                    }
                    bo.flush();
                    if ((recievedBytes + firstByteToSend - (resuming ? 1 : 0)) == totalsize) {
                        SwingUtilities.invokeLater(new Runnable() {

                            @Override
                            public void run() {
                                lbl_statusValue.setText("Transfer complete!");
                                pgb_progress.setValue(100);
                                btn_accept.setEnabled(true);
                                btn_refuse.setText("Close");
                            }
                        });
                    } else {
                        SwingUtilities.invokeLater(new Runnable() {

                            @Override
                            public void run() {
                                lbl_statusValue.setText("Transfer incomplete!");
                                btn_refuse.setText("Close");
                            }
                        });
                    }
                    running = false;
                    bi.close();
                    bo.close();
                } catch (Exception e) {
                    e.printStackTrace();
                    if (!wasCancelled) {
                        updateStatusLabel("Error: " + e.getLocalizedMessage());
                    }
                } finally {
                    try {
                        if (bi != null) {
                            bi.close();
                        }
                        if (bo != null) {
                            bo.close();
                        }
                        if (socket != null) {
                            socket.close();
                            socket = null;
                        }
                    } catch (Exception e) {
                    }
                }
            }
}
