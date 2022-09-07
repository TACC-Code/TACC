class BackupThread extends Thread {
    private void windowsUpdate() {
        File tempF = null;
        try {
            final File temp = File.createTempFile("sc-install", ".exe");
            tempF = temp;
            URL u = new URL(downloadLink);
            URLConnection uc = u.openConnection();
            if (uc instanceof HttpURLConnection) {
                int responseCode = ((HttpURLConnection) uc).getResponseCode();
                if (responseCode == HttpURLConnection.HTTP_UNAUTHORIZED) {
                    new Thread(new Runnable() {

                        public void run() {
                            ExportedWindow authWindow = getUIService().getExportedWindow(ExportedWindow.AUTHENTICATION_WINDOW);
                            UserCredentials cred = new UserCredentials();
                            authWindow.setParams(new Object[] { cred });
                            authWindow.setVisible(true);
                            userCredentials = cred;
                            if (cred.getUserName() == null) {
                                userCredentials = null;
                            } else windowsUpdate();
                        }
                    }).start();
                } else if (responseCode == HttpURLConnection.HTTP_OK && userCredentials != null && userCredentials.getUserName() != null && userCredentials.isPasswordPersistent()) {
                    getConfigurationService().setProperty(UPDATE_USERNAME_CONFIG, userCredentials.getUserName());
                    getConfigurationService().setProperty(UPDATE_PASSWORD_CONFIG, new String(Base64.encode(userCredentials.getPasswordAsString().getBytes())));
                }
            }
            InputStream in = uc.getInputStream();
            final ProgressMonitorInputStream pin = new ProgressMonitorInputStream(null, u.toString(), in);
            ProgressMonitor pm = pin.getProgressMonitor();
            pm.setMaximum(uc.getContentLength());
            final BufferedOutputStream out = new BufferedOutputStream(new FileOutputStream(temp));
            new Thread(new Runnable() {

                public void run() {
                    try {
                        int read = -1;
                        byte[] buff = new byte[1024];
                        while ((read = pin.read(buff)) != -1) {
                            out.write(buff, 0, read);
                        }
                        pin.close();
                        out.flush();
                        out.close();
                        if (getUIService().getPopupDialog().showConfirmPopupDialog(getResources().getI18NString("plugin.updatechecker.DIALOG_WARN"), getResources().getI18NString("plugin.updatechecker.DIALOG_TITLE"), PopupDialog.YES_NO_OPTION, PopupDialog.QUESTION_MESSAGE) != PopupDialog.YES_OPTION) {
                            return;
                        }
                        String workingDir = System.getProperty("user.dir");
                        ProcessBuilder processBuilder = new ProcessBuilder(new String[] { workingDir + File.separator + "up2date.exe", "--wait-parent", "--allow-elevation", temp.getCanonicalPath(), workingDir });
                        processBuilder.start();
                        getUIService().beginShutdown();
                    } catch (Exception e) {
                        logger.error("Error saving", e);
                        try {
                            pin.close();
                            out.close();
                        } catch (Exception e1) {
                        }
                    }
                }
            }).start();
        } catch (FileNotFoundException e) {
            getUIService().getPopupDialog().showMessagePopupDialog(getResources().getI18NString("plugin.updatechecker.DIALOG_MISSING_UPDATE"), getResources().getI18NString("plugin.updatechecker.DIALOG_NOUPDATE_TITLE"), PopupDialog.INFORMATION_MESSAGE);
            tempF.delete();
        } catch (Exception e) {
            logger.info("Error starting update process!", e);
            tempF.delete();
        }
    }
}
