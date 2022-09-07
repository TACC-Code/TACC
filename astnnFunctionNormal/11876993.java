class BackupThread extends Thread {
    public void startupInterface() {
        setSessionID(JUnique.getUniqueID());
        Common.sessionID = getSessionID();
        Common.sd.setData(getSessionID(), "silentExceptions", "Yes", true);
        Common.applicationMode = "SwingClient";
        JUtility.initLogging("");
        JPrint.init();
        if (JUtility.isValidJavaVersion(Common.requiredJavaVersion) == true) {
            Common.hostList.loadHosts();
            Common.selectedHostID = getHostID();
            houseKeeping = false;
            while (shutdown == false) {
                secondsRemaining = secondsBeforeHousekeeping;
                logger.debug("Connecting to database.");
                while ((Common.hostList.getHost(getHostID()).isConnected(getSessionID()) == false) && (shutdown == false)) {
                    Common.hostList.getHost(getHostID()).connect(getSessionID(), getHostID());
                }
                if ((Common.hostList.getHost(getHostID()).isConnected(getSessionID()) == true) && (shutdown == false)) {
                    JDBSchema schema = new JDBSchema(getSessionID(), Common.hostList.getHost(getHostID()));
                    schema.validate(false);
                    JUtility.initEANBarcode();
                    JLaunchReport.init();
                    Common.init();
                    JDBUser user = new JDBUser(getHostID(), getSessionID());
                    JDBControl ctrl = new JDBControl(getHostID(), getSessionID());
                    JeMail mail = new JeMail(getHostID(), getSessionID());
                    user.setUserId("INTERFACE");
                    user.setPassword("INTERFACE");
                    Common.userList.addUser(getSessionID(), user);
                    enableEnterfaceStatusEmails = Boolean.parseBoolean(ctrl.getKeyValueWithDefault("INTERFACE EMAIL NOTIFY", "false", "Email startup and shutdown events :- true or false"));
                    interfaceEmailAddresses = ctrl.getKeyValueWithDefault("INTERFACE ADMIN EMAIL", "someone@somewhere.com", "Email address for startup and shutdown events.");
                    StringConverter stringConverter = new StringConverter();
                    ArrayConverter arrayConverter = new ArrayConverter(String[].class, stringConverter);
                    arrayConverter.setDelimiter(';');
                    arrayConverter.setAllowedChars(new char[] { '@', '_' });
                    String[] emailList = (String[]) arrayConverter.convert(String[].class, interfaceEmailAddresses);
                    siteName = Common.hostList.getHost(getHostID()).getSiteDescription();
                    if (user.login()) {
                        if (enableEnterfaceStatusEmails == true) {
                            try {
                                String subject = "";
                                if (houseKeeping == true) {
                                    subject = "Commander4j " + JVersion.getProgramVersion() + " Interface maintenance restart for [" + siteName + "] on " + JUtility.getClientName();
                                } else {
                                    subject = "Commander4j " + JVersion.getProgramVersion() + " Interface startup for [" + siteName + "] on " + JUtility.getClientName();
                                }
                                mail.postMail(emailList, subject, "Interface service has started.", "", "");
                            } catch (Exception ex) {
                                logger.error("InterfaceThread Unable to send emails");
                            }
                        }
                        houseKeeping = false;
                        logger.debug("Interface Logged on successfully");
                        logger.debug("Starting Threads....");
                        secondsBeforeHousekeeping = Integer.valueOf(ctrl.getKeyValueWithDefault("INTERFACE HOUSEKEEPING INTERVAL", "86400", "Frequency in seconds."));
                        secondsRemaining = secondsBeforeHousekeeping;
                        startupThreads();
                        while ((shutdown == false) & (houseKeeping == false)) {
                            com.commander4j.util.JWait.milliSec(1000);
                            secondsRemaining--;
                            if (secondsRemaining == 0) {
                                houseKeeping = true;
                            }
                        }
                        logger.debug("Stopping Threads....");
                        shutdownThreads();
                        user.logout();
                        logger.debug("Interface Logged out successfully");
                        if (enableEnterfaceStatusEmails == true) {
                            try {
                                String subject = "";
                                if (houseKeeping == true) {
                                    subject = "Commander4j " + JVersion.getProgramVersion() + " Interface maintenance shutdown for [" + siteName + "] on " + JUtility.getClientName();
                                } else {
                                    subject = "Commander4j " + JVersion.getProgramVersion() + " Interface shutdown for [" + siteName + "] on " + JUtility.getClientName();
                                }
                                mail.postMail(emailList, subject, "Interface service has stopped.", "", "");
                            } catch (Exception ex) {
                                logger.error("InterfaceThread Unable to send emails");
                            }
                        }
                    } else {
                        logger.debug("Interface routine failed to logon to application using account INTERFACE");
                    }
                    logger.debug("Disconnecting from database.");
                    Common.hostList.getHost(getHostID()).disconnectAll();
                    if (houseKeeping == true) {
                        logger.debug("HOUSEKEEPING START");
                        String memoryBefore = "Memory used before garbage collection = " + String.valueOf((Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory()) / 1024) + "k";
                        System.gc();
                        String memoryAfter = "Memory used after garbage collection  = " + String.valueOf((Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory()) / 1024) + "k";
                        String stats = GenericMessageHeader.getStats();
                        GenericMessageHeader.clearStats();
                        if (enableEnterfaceStatusEmails == true) {
                            try {
                                mail.postMail(emailList, "Commander4j " + JVersion.getProgramVersion() + " Interface maintenance for [" + siteName + "] on " + JUtility.getClientName(), memoryBefore + "\n\n" + memoryAfter + "\n\n" + "Maintenance is scheduled to occur every " + String.valueOf(secondsBeforeHousekeeping) + " seconds.\n\n\n\n" + stats, "", "");
                            } catch (Exception ex) {
                                logger.error("InterfaceThread Unable to send emails");
                            }
                        }
                        logger.debug("Interface Garbage Collection.");
                        logger.debug("HOUSEKEEPING END");
                    }
                }
            }
        }
    }
}
