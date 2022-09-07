class BackupThread extends Thread {
    private static void refreshAgents(ManagerConnection managerConnection, List<String> sipInUseLines) {
        try {
            if (LicenseManager.isValid("TegsoftCC") <= 0) {
                return;
            }
            Dataset TBLUSERS = UiUtil.getDataset("TBLUSERS");
            TBLUSERS.fill(UiUtil.getDbCommand("TBLUSERS"));
            for (int j = 0; j < 7; j++) {
                Vbox target = (Vbox) UiUtil.findComponent("AGENTS" + j);
                target.getChildren().clear();
                for (int i = j * 10; i < TBLUSERS.getRowCount(); i++) {
                    DataRow rowTBLUSERS = TBLUSERS.getRow(i);
                    Window window = new Window();
                    window.setWidth("120px");
                    window.setBorder("normal");
                    Image image1 = new Image("/image/buttons/ok_unsaved.png");
                    image1.setWidth("15px");
                    image1.setHeight("15px");
                    image1.setVisible(false);
                    UiUtil.addComponent(window, image1);
                    Image image2 = new Image("/image/crm/skype/Error_16x16.png");
                    image2.setWidth("15px");
                    image2.setHeight("15px");
                    image2.setVisible(false);
                    UiUtil.addComponent(window, image2);
                    Image image3 = new Image("/image/crm/skype/ContactsOkay_16x16.png");
                    image3.setWidth("15px");
                    image3.setHeight("15px");
                    image3.setVisible(false);
                    UiUtil.addComponent(window, image3);
                    Image image4 = new Image("/image/crm/skype/BlockContact_16x16.png");
                    image4.setWidth("15px");
                    image4.setHeight("15px");
                    image4.setVisible(false);
                    UiUtil.addComponent(window, image4);
                    Image image5 = new Image("/image/crm/skype/CallPhones_20x16.png");
                    image5.setWidth("15px");
                    image5.setHeight("15px");
                    image5.setVisible(false);
                    UiUtil.addComponent(window, image5);
                    Image image6 = new Image("/image/crm/skype/Sound_16x16.png");
                    image6.setWidth("15px");
                    image6.setHeight("15px");
                    image6.setVisible(false);
                    UiUtil.addComponent(window, image6);
                    Label label1 = new Label();
                    label1.setVisible(false);
                    UiUtil.addComponent(window, label1);
                    Command command = new Command("SELECT INTERFACE FROM TBLCCAGENTLOG WHERE UNITUID={UNITUID} AND LOGTYPE='LOGIN' AND ENDDATE IS NULL");
                    command.append(" AND UID=");
                    command.bind(rowTBLUSERS.getString("UID"));
                    String agentINTERFACE = command.executeScalarAsString();
                    boolean isAgentLogin = NullStatus.isNotNull(agentINTERFACE);
                    command = new Command("SELECT A.REASON FROM TBLCCAGENTLOG A WHERE A.UNITUID={UNITUID} AND A.LOGTYPE='READY' AND A.ENDDATE IS NULL");
                    command.append(" AND UID=");
                    command.bind(rowTBLUSERS.getString("UID"));
                    String REASON = command.executeScalarAsString();
                    boolean isAgentReady = NullStatus.isNull(REASON);
                    if (isAgentLogin) {
                        image1.setVisible(true);
                        String count = "";
                        String callerId = "";
                        String INTERFACE = agentINTERFACE;
                        if (INTERFACE.indexOf("/") >= 0) {
                            INTERFACE = INTERFACE.substring(INTERFACE.indexOf("/") + 1);
                            for (String line : sipInUseLines) {
                                if (line.indexOf("/") > 0) {
                                    if (line.indexOf(" ") > 0) {
                                        String peerName = line.substring(0, line.indexOf(" "));
                                        if (INTERFACE.startsWith(peerName)) {
                                            count = Converter.asNotNullString(line.substring(line.indexOf(" ") + 1, line.indexOf("/", line.indexOf(" ")))).trim();
                                            if (Integer.parseInt(count) > 0) {
                                                image6.setVisible(true);
                                            } else {
                                                image5.setVisible(true);
                                            }
                                            break;
                                        }
                                    }
                                }
                            }
                            for (int k = 0; k < managerConnection.getChannels().size(); k++) {
                                StatusEvent statusEvent = managerConnection.getChannels().get(k);
                                String channel = statusEvent.getChannel();
                                if (NullStatus.isNotNull(channel)) {
                                    if (channel.startsWith(agentINTERFACE)) {
                                        callerId = statusEvent.getCallerIdNum();
                                    }
                                }
                            }
                        }
                        if (isAgentReady) {
                            image3.setVisible(true);
                            label1.setVisible(true);
                            label1.setValue(MessageUtil.getMessage(Monitoring.class, Messages.readyText, callerId));
                        } else {
                            image4.setVisible(true);
                            label1.setVisible(true);
                            label1.setValue(MessageUtil.getMessage(Monitoring.class, Messages.notReadyText, callerId, MessageUtil.getMessage(Monitoring.class, REASON)));
                        }
                        String name = StringUtil.left(StringUtil.convertToEnglishOnlyLetters(Converter.asNotNullString(rowTBLUSERS.getString("USERNAME")) + " " + Converter.asNotNullString(rowTBLUSERS.getString("SURNAME"))).toUpperCase(Locale.ENGLISH), 12);
                        String title = name + " " + INTERFACE;
                        window.setTitle(title);
                    } else {
                        image2.setVisible(true);
                        window.setTitle(StringUtil.left(StringUtil.convertToEnglishOnlyLetters(Converter.asNotNullString(rowTBLUSERS.getString("USERNAME")) + " " + Converter.asNotNullString(rowTBLUSERS.getString("SURNAME"))).toUpperCase(Locale.ENGLISH), 17));
                    }
                    UiUtil.addComponent(target, window);
                    if ((i + 1) % 10 == 0) {
                        break;
                    }
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}
