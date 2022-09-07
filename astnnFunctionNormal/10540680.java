class BackupThread extends Thread {
    public static void applyUpgrade(Event event) throws Exception {
        if (((Component) UiUtil.findComponent("restartRequiredAlert")).isVisible()) {
            if (!((Checkbox) UiUtil.findComponent("restartRequired")).isChecked()) {
                UiUtil.showMessage(MessageType.ERROR, MessageUtil.getMessage(Upgrade.class, Messages.upgrade_9));
                return;
            }
        }
        Dataset TBLINSTALLATION = new Dataset("TBLINSTALLATION", "TBLINSTALLATION");
        TBLINSTALLATION.fill(new Command("SELECT * FROM TBLINSTALLATION WHERE STATUS='DOWNLOADED' ORDER BY VERSION DESC,ORDERID"));
        String maxVersion = TBLINSTALLATION.getRow(0).getString("VERSION");
        ArrayList<String> operationList = new ArrayList<String>();
        for (int i = 0; i < TBLINSTALLATION.getRowCount(); i++) {
            String UPGRADETYPE = TBLINSTALLATION.getRow(i).getString("UPGRADETYPE");
            if (operationList.indexOf(UPGRADETYPE) < 0) {
                operationList.add(UPGRADETYPE);
            }
        }
        String tobeHome = UiUtil.getParameter("RealPath.Context");
        new File(tobeHome + "/backup/").mkdirs();
        SimpleDateFormat yyyyMMdd = new SimpleDateFormat("yyyyMMdd");
        yyyyMMdd.setTimeZone(DateUtil.getTimezone());
        long currentDate = Long.parseLong(yyyyMMdd.format(DateUtil.today()));
        boolean databaseExecutionRequired = false;
        boolean restartRequired = false;
        for (int i = 0; i < operationList.size(); i++) {
            String downloadFileName = "";
            if (Compare.equal("FORMS", operationList.get(i))) {
                downloadFileName = "Tegsoft" + "_FORMS_" + maxVersion + ".zip";
                FileUtil.createZipPackage(tobeHome + "/forms", tobeHome + "/backup/FORMS" + currentDate + Counter.getUUIDString() + ".zip");
                FileUtil.deleteDir(tobeHome + "/forms/");
                FileUtil.extractZipPackage(tobeHome + "/setup/" + downloadFileName, tobeHome);
            } else if (Compare.equal("DB", operationList.get(i))) {
                downloadFileName = "Tegsoft" + "_DB_" + maxVersion + ".zip";
                FileUtil.createZipPackage(tobeHome + "/sql", tobeHome + "/backup/DB" + currentDate + Counter.getUUIDString() + ".zip");
                FileUtil.deleteDir(tobeHome + "/sql/");
                databaseExecutionRequired = true;
                FileUtil.extractZipPackage(tobeHome + "/setup/" + downloadFileName, tobeHome);
            } else if (Compare.equal("IMAGES", operationList.get(i))) {
                downloadFileName = "Tegsoft" + "_IMAGES_" + maxVersion + ".zip";
                FileUtil.createZipPackage(tobeHome + "/image", tobeHome + "/backup/IMAGES" + currentDate + Counter.getUUIDString() + ".zip");
                FileUtil.deleteDir(tobeHome + "/image/");
                FileUtil.extractZipPackage(tobeHome + "/setup/" + downloadFileName, tobeHome);
            } else if (Compare.equal("VIDEOS", operationList.get(i))) {
                downloadFileName = "Tegsoft" + "_VIDEOS_" + maxVersion + ".zip";
                FileUtil.createZipPackage(tobeHome + "/videos", tobeHome + "/backup/VIDEOS" + currentDate + Counter.getUUIDString() + ".zip");
                FileUtil.deleteDir(tobeHome + "/videos/");
                FileUtil.extractZipPackage(tobeHome + "/setup/" + downloadFileName, tobeHome);
            } else if (Compare.equal("TEGSOFTJARS", operationList.get(i))) {
                downloadFileName = "Tegsoft" + "_TEGSOFTJARS_" + maxVersion + ".zip";
                FileUtil.createZipPackage(tobeHome + "/WEB-INF/lib/", tobeHome + "/backup/TEGSOFTJARS" + currentDate + Counter.getUUIDString() + ".zip", "Tegsoft", "jar");
                FileUtil.deleteMatchingFilesInDir(new File(tobeHome + "/WEB-INF/lib/"), "Tegsoft", "jar");
                FileUtil.extractZipPackage(tobeHome + "/setup/" + downloadFileName, tobeHome + "/WEB-INF/");
                restartRequired = true;
            } else if (Compare.equal("TOBEJARS", operationList.get(i))) {
                downloadFileName = "Tegsoft" + "_TOBEJARS_" + maxVersion + ".zip";
                FileUtil.createZipPackage(tobeHome + "/WEB-INF/lib/", tobeHome + "/backup/TOBEJARS" + currentDate + Counter.getUUIDString() + ".zip", "Tobe", "jar");
                FileUtil.deleteMatchingFilesInDir(new File(tobeHome + "/WEB-INF/lib/"), "Tobe", "jar");
                FileUtil.extractZipPackage(tobeHome + "/setup/" + downloadFileName, tobeHome + "/WEB-INF/");
                restartRequired = true;
            } else if (Compare.equal("ALLJARS", operationList.get(i))) {
                downloadFileName = "Tegsoft" + "_ALLJARS_" + maxVersion + ".zip";
                FileUtil.createZipPackage(tobeHome + "/WEB-INF/lib/", tobeHome + "/backup/AllJARS" + currentDate + Counter.getUUIDString() + ".zip");
                FileUtil.deleteAllFilesInDir(new File(tobeHome + "/WEB-INF/lib/"));
                FileUtil.extractZipPackage(tobeHome + "/setup/" + downloadFileName, tobeHome + "/WEB-INF/");
                restartRequired = true;
            } else if (Compare.equal("CONFIGSERVICE", operationList.get(i))) {
                downloadFileName = "Tegsoft" + "_CONFIGSERVICE_" + maxVersion + ".zip";
                FileUtil.createZipPackage("/etc/init.d/", tobeHome + "/backup/CONFIGSERVICE" + currentDate + Counter.getUUIDString() + ".zip", "tegsoft", null);
                FileUtil.deleteMatchingFilesInDir(new File("/etc/init.d/"), "tegsoft", null);
                FileUtil.extractZipPackage(tobeHome + "/setup/" + downloadFileName, "/etc/");
            } else if (Compare.equal("CONFIGSCRIPTS", operationList.get(i))) {
                downloadFileName = "Tegsoft" + "_CONFIGSCRIPTS_" + maxVersion + ".zip";
                FileUtil.createZipPackage("/root/", tobeHome + "/backup/CONFIGSCRIPTS" + currentDate + Counter.getUUIDString() + ".zip", "tegsoft", null);
                FileUtil.deleteMatchingFilesInDir(new File("/root/"), "tegsoft", null);
                FileUtil.extractZipPackage(tobeHome + "/setup/" + downloadFileName, "/");
            } else if (Compare.equal("CONFIGFOP", operationList.get(i))) {
                downloadFileName = "Tegsoft" + "_CONFIGFOP_" + maxVersion + ".zip";
                FileUtil.createZipPackage("/root/fop/", tobeHome + "/backup/CONFIGFOP" + currentDate + Counter.getUUIDString() + ".zip");
                FileUtil.deleteAllFilesInDir(new File("/root/fop/"));
                FileUtil.extractZipPackage(tobeHome + "/setup/" + downloadFileName, "/root/");
            } else if (Compare.equal("CONFIGASTERISK", operationList.get(i))) {
                downloadFileName = "Tegsoft" + "_CONFIGASTERISK_" + maxVersion + ".zip";
                FileUtil.createZipPackage("/etc/asterisk/", tobeHome + "/backup/CONFIGASTERISK" + currentDate + Counter.getUUIDString() + ".zip");
                FileUtil.deleteAllFilesInDir(new File("/etc/asterisk/"));
                FileUtil.extractZipPackage(tobeHome + "/setup/" + downloadFileName, "/etc/");
                restartRequired = true;
            }
            for (int j = 0; j < TBLINSTALLATION.getRowCount(); j++) {
                if (Compare.equal("DB", operationList.get(i))) {
                    continue;
                }
                if (Compare.equal(TBLINSTALLATION.getRow(j).getString("UPGRADETYPE"), operationList.get(i))) {
                    TBLINSTALLATION.getRow(j).setString("STATUS", "APPLIED");
                }
            }
            TBLINSTALLATION.save();
        }
        if (databaseExecutionRequired) {
            File sqlFolder = new File(tobeHome + "/sql");
            File sqlFiles[] = sqlFolder.listFiles();
            for (int i = 0; i < sqlFiles.length; i++) {
                BufferedReader in = new BufferedReader(new FileReader(sqlFiles[i]));
                String sql = "";
                String str = "";
                while ((str = in.readLine()) != null) {
                    sql += str + "\n ";
                }
                in.close();
                String sqlCommands[] = sql.split(";");
                for (String sqlCommand : sqlCommands) {
                    if (NullStatus.isNull(sqlCommand)) {
                        continue;
                    }
                    if (sqlCommand.startsWith("//")) {
                        continue;
                    }
                    if (sqlCommand.startsWith("--")) {
                        continue;
                    }
                    Command command = new Command(sqlCommand);
                    try {
                        command.executeNonQuery();
                        Connection.getActive().commit();
                    } catch (Exception ex) {
                    }
                    Connection.closeActive();
                }
            }
            for (int j = 0; j < TBLINSTALLATION.getRowCount(); j++) {
                if (Compare.equal(TBLINSTALLATION.getRow(j).getString("UPGRADETYPE"), "DB")) {
                    TBLINSTALLATION.getRow(j).setString("STATUS", "APPLIED");
                }
            }
            TBLINSTALLATION.save();
        }
        String clientAppliedVersion = new Command("SELECT MAX(VERSION) FROM TBLINSTALLATION WHERE STATUS='APPLIED'").executeScalarAsString();
        if (NullStatus.isNull(clientAppliedVersion)) {
            clientAppliedVersion = "20050101";
        }
        String clientMAC = "";
        String clientUNITID = UiUtil.getUNITUID();
        ArrayList<String> macList = getMacList();
        for (int i = 0; i < macList.size(); i++) {
            clientMAC += macList.get(i) + ",";
        }
        URL urlUPGRADECOMPLETE = new URL("http://www.tegsoft.com/Tobe/forms/TobeOS/upgrade/upgrade_current.jsp?tegsoftCLIENTVERSION=" + clientAppliedVersion + "&tegsoftCLIENTUNITID=" + clientUNITID + "&tegsoftCLIENTMAC=" + clientMAC + "&tegsoftCOMMAND=UPGRADECOMPLETE");
        URLConnection connectionUPGRADECOMPLETE = urlUPGRADECOMPLETE.openConnection();
        BufferedReader in = new BufferedReader(new InputStreamReader(connectionUPGRADECOMPLETE.getInputStream()));
        while (in.readLine() != null) {
        }
        in.close();
        UiUtil.getDataset("TBLINSTALLATION").reFill();
        ((Component) UiUtil.findComponent("downloadUpgrade")).setVisible(false);
        ((Component) UiUtil.findComponent("applyUpgrade")).setVisible(false);
        ((Component) UiUtil.findComponent("restartRequiredHbox")).setVisible(false);
        ((Component) UiUtil.findComponent("restartRequiredAlert")).setVisible(false);
        if (restartRequired) {
            Runtime.getRuntime().exec("/root/tegsoft_restartSystem.sh");
            UiUtil.showMessage(MessageType.INFO, MessageUtil.getMessage(Upgrade.class, Messages.upgrade_7));
        } else {
            UiUtil.showMessage(MessageType.INFO, MessageUtil.getMessage(Upgrade.class, Messages.upgrade_8));
        }
    }
}
