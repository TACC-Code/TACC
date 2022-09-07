class BackupThread extends Thread {
    @Override
    public void onManagerEvent(ManagerEvent managerEvent) {
        try {
            JobUtil.prepareThread();
            Connection.initNonJ2EE();
            if (NewStateEvent.class.isAssignableFrom(managerEvent.getClass())) {
                NewStateEvent newStateEvent = (NewStateEvent) managerEvent;
                addActiveCall(newStateEvent);
                Connection.initNonJ2EE();
                updateState(newStateEvent.getUniqueId(), newStateEvent.getChannelStateDesc());
                Connection.closeActive();
            } else if (QueueMemberAddedEvent.class.isAssignableFrom(managerEvent.getClass())) {
                ConfigWriter.writeFOP(PBXID);
                Runtime.getRuntime().exec("service tegsoft_panel reload");
            } else if (QueueMemberRemovedEvent.class.isAssignableFrom(managerEvent.getClass())) {
                Thread.sleep(2000);
                ConfigWriter.writeFOP(PBXID);
                Runtime.getRuntime().exec("service tegsoft_panel reload");
            } else if (AgentCalledEvent.class.isAssignableFrom(managerEvent.getClass())) {
                AgentCalledEvent agentCalledEvent = (AgentCalledEvent) managerEvent;
                Connection.initNonJ2EE();
                Command command = new Command("SELECT UID FROM TBLCCAGENTLOG WHERE UNITUID={UNITUID} AND LOGTYPE='LOGIN' AND INTERFACE=");
                command.bind(agentCalledEvent.getAgentCalled());
                command.append("AND ENDDATE IS NULL");
                String agentUID = command.executeScalarAsString();
                if (NullStatus.isNull(agentUID)) {
                    Connection.closeActive();
                    return;
                }
                String uniqueId = agentCalledEvent.getUniqueId();
                if (NullStatus.isNull(uniqueId)) {
                    if (NullStatus.isNotNull(agentCalledEvent.getChannelCalling())) {
                        NewStateEvent callingCahnnel = getActiveCall(agentCalledEvent.getChannelCalling());
                        if (callingCahnnel != null) {
                            uniqueId = callingCahnnel.getUniqueId();
                        }
                    }
                }
                notifyAgent(uniqueId, agentUID, agentCalledEvent.getCallerIdNum(), agentCalledEvent.getAgentCalled());
                Connection.closeActive();
            } else if (NewCallerIdEvent.class.isAssignableFrom(managerEvent.getClass())) {
                JobUtil.prepareThread();
                Connection.initNonJ2EE();
                NewCallerIdEvent newCallerIdEvent = (NewCallerIdEvent) managerEvent;
                Command command = new Command("UPDATE TBLCCAGENTLOG SET REASON='DEFAULT',PHONE=");
                command.bind(newCallerIdEvent.getCallerIdNum());
                command.append("WHERE UNITUID={UNITUID} AND CALLID=");
                command.bind(newCallerIdEvent.getUniqueId());
                command.executeNonQuery(true);
                Connection.getActive().commit();
                Connection.closeActive();
            } else if (AgentConnectEvent.class.isAssignableFrom(managerEvent.getClass())) {
                AgentConnectEvent agentConnectEvent = (AgentConnectEvent) managerEvent;
                new Command("UPDATE TBLCCAGENTLOG SET REASON='DEFAULT' WHERE CALLID='" + agentConnectEvent.getUniqueId() + "'").executeNonQuery(true);
            } else if (VarSetEvent.class.isAssignableFrom(managerEvent.getClass())) {
                JobUtil.prepareThread();
                Connection.initNonJ2EE();
                VarSetEvent varSetEvent = (VarSetEvent) managerEvent;
                if ("QEHOLDTIME".equals(varSetEvent.getVariable())) {
                    new Command("UPDATE TBLCCAGENTLOG SET REASON='DEFAULT' WHERE CALLID='" + varSetEvent.getUniqueId() + "'").executeNonQuery(true);
                }
            } else if (HangupEvent.class.isAssignableFrom(managerEvent.getClass())) {
                JobUtil.prepareThread();
                Connection.initNonJ2EE();
                HangupEvent hangupEvent = (HangupEvent) managerEvent;
                new Command("UPDATE TBLCCCAMPCALLD A SET A.HANGUP={NOW}  WHERE A.CALLID='" + hangupEvent.getUniqueId() + "' ").executeNonQuery(true);
                new Command("UPDATE TBLCCAGENTLOG SET ENDDATE={NOW} WHERE CALLID='" + hangupEvent.getUniqueId() + "'").executeNonQuery(true);
                removeActiveCall(hangupEvent.getUniqueId());
                Connection.closeActive();
            } else if (DtmfEvent.class.isAssignableFrom(managerEvent.getClass())) {
                JobUtil.prepareThread();
                Connection.initNonJ2EE();
                DtmfEvent dtmfEvent = (DtmfEvent) managerEvent;
                String CAMPAIGNID = new Command("SELECT CAMPAIGNID FROM TBLCCCAMPCALLD WHERE CALLID='" + dtmfEvent.getUniqueId() + "' ").executeScalarAsString();
                String CONTID = new Command("SELECT CONTID FROM TBLCCCAMPCALLD WHERE CALLID='" + dtmfEvent.getUniqueId() + "' ").executeScalarAsString();
                Command command = new Command("UPDATE TBLCCCAMPCONT SET DTMFARRAY=' ' WHERE CAMPAIGNID=");
                command.bind(CAMPAIGNID);
                command.append("AND CONTID=");
                command.bind(CONTID);
                command.append("AND DTMFARRAY IS NULL");
                command.executeNonQuery(true);
                command = new Command("UPDATE TBLCCCAMPCONT SET DTMFARRAY=DTMFARRAY||'" + dtmfEvent.getDigit() + "' WHERE CAMPAIGNID=");
                command.bind(CAMPAIGNID);
                command.append("AND CONTID=");
                command.bind(CONTID);
                command.executeNonQuery(true);
                Connection.closeActive();
            }
        } catch (Exception ex) {
            MessageUtil.logMessage(ManagerEventListener.class, Level.FATAL, ex);
        }
        Connection.closeActive();
    }
}
