class BackupThread extends Thread {
    public void run() {
        ComReader reader;
        Onion portType = null;
        try {
            portType = new Onion("{'type':'serial'}");
        } catch (JSONException ex) {
            Logger.getLogger(BusCom.class.getName()).log(Level.SEVERE, null, ex);
        }
        reader = new ComReader();
        while (keepRunning == true) {
            Message msg = getMsg(true);
            Onion on = msg.getContent();
            System.out.println("Buscom Message abgeholt:" + on.toString());
            String command = on.getOnionString("command");
            if ("serWrite".equalsIgnoreCase(command)) {
                String data = Base64Coder.decodeString(on.getOnionString("data"));
                Logger.getLogger(BusCom.class.getName()).log(Level.INFO, "busCom serWrite: >" + data + "<");
                reader.write(data);
            } else if ("serFlush".equalsIgnoreCase(command)) {
                Logger.getLogger(BusCom.class.getName()).log(Level.INFO, "busCom serFlush ");
                reader.flush();
            } else if ("serWait".equalsIgnoreCase(command)) {
                try {
                    Integer result = reader.wait(Base64Coder.decodeString(on.getOnionString("data")), on.getInt("timeout"));
                    Logger.getLogger(BusCom.class.getName()).log(Level.INFO, "busCom serWait: " + result);
                    replyMsg(msg, new Onion("" + "{'type':'" + CM_RES_BUS + "'," + "'owner':" + "{'name':'" + getPluginName() + "'}," + "'result':" + result + "," + "'replyID':" + on.getInt("msgID") + "}"));
                } catch (JSONException ex) {
                    Logger.getLogger(BusCom.class.getName()).log(Level.SEVERE, null, ex);
                }
            } else if ("connect".equalsIgnoreCase(command)) {
                reader.close();
                Boolean result = reader.connect((OOBDPort) getCore().supplyHardwareHandle(portType));
                try {
                    replyMsg(msg, new Onion("" + "{'type':'" + CM_RES_BUS + "'," + "'owner':" + "{'name':'" + getPluginName() + "'}," + "'result':" + result.toString() + "," + "'replyID':" + on.getInt("msgID") + "}"));
                } catch (JSONException ex) {
                    Logger.getLogger(BusCom.class.getName()).log(Level.SEVERE, null, ex);
                }
            } else if ("close".equalsIgnoreCase(command)) {
                reader.close();
            } else if ("serReadLn".equalsIgnoreCase(command)) {
                try {
                    String result = reader.readln(on.getInt("timeout"), on.optBoolean("ignore"));
                    Logger.getLogger(BusCom.class.getName()).log(Level.INFO, "busCom readline: " + result);
                    replyMsg(msg, new Onion("" + "{'type':'" + CM_RES_BUS + "'," + "'owner':" + "{'name':'" + getPluginName() + "'}," + "'replyID':" + on.getInt("msgID") + "," + "'result':'" + Base64Coder.encodeString(result) + "'}"));
                } catch (JSONException ex) {
                    Logger.getLogger(BusCom.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
    }
}
