class BackupThread extends Thread {
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
}
