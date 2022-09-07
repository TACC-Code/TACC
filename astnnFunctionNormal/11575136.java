class BackupThread extends Thread {
    @SuppressWarnings("unchecked")
    public void receivedMessage(byte[] bytes) {
        try {
            Convertible conv = Converter.getInstance().revert(bytes);
            if (conv instanceof QueryChannel) {
                SGSBusinessProcess.doQueryChannel(server.get(SGSServer.class), session, (QueryChannel) conv);
            } else if (conv instanceof EnterChannel) {
                SGSBusinessProcess.doEnterChannel(server.get(SGSServer.class), session, (EnterChannel) conv);
                SGSChannelData chnlData = getChannelData(session);
                HashSet<AbstractDrawing> set = (HashSet<AbstractDrawing>) chnlData.get(SGSChannelData.WHITEBOARD_DATA);
                for (Iterator itor = set.iterator(); itor.hasNext(); ) {
                    AbstractDrawing cur = (AbstractDrawing) itor.next();
                    session.send(Converter.getInstance().convert(cur));
                }
            } else if (conv instanceof LeaveChannel) {
                SGSBusinessProcess.doLeaveChannel(server.get(SGSServer.class), session, (LeaveChannel) conv);
            } else if (conv instanceof Logout) {
                SGSBusinessProcess.doLogout(server.get(SGSServer.class), session, (Logout) conv);
            } else if (conv instanceof QueryPeer) {
                SGSBusinessProcess.doQueryPeer(server.get(SGSServer.class), session, (QueryPeer) conv);
            } else if (conv instanceof AbstractDrawing) {
                SGSChannelData chnlData = getChannelData(session);
                chnlData.get(SGSChannelData.WHITEBOARD_DATA, HashSet.class).add((AbstractDrawing) conv);
                Channel chnl = chnlData.getChannel();
                SGSBusinessProcess.channelwideDispatch(conv, session, chnl);
            } else if (conv instanceof Clear) {
                SGSChannelData chnlData = getChannelData(session);
                chnlData.get(SGSChannelData.WHITEBOARD_DATA, HashSet.class).clear();
                Channel chnl = chnlData.getChannel();
                SGSBusinessProcess.channelwideDispatch(conv, session, chnl);
            } else if (conv instanceof OpenMic) {
                OpenMic om = (OpenMic) conv;
                String account = SGSBusinessProcess.accFromSesName(session.getName());
                SGSClientData data = getServer().getClientData(account);
                String channelName = data.get(SGSClientData.CURRENT_CHANNEL_NAME, String.class);
                SGSChannelData chnlData = getServer().getChannelData(channelName);
                data.set(SGSClientData.TALKING, Boolean.TRUE);
                NOpenMic tom = new NOpenMic();
                tom.nid = om.nid;
                getServer().sendToNServer(Converter.getInstance().convert(tom));
                Channel chnl = chnlData.getChannel();
                SGSBusinessProcess.channelwideDispatch(conv, session, chnl);
            } else if (conv instanceof CloseMic) {
                CloseMic cm = (CloseMic) conv;
                String account = SGSBusinessProcess.accFromSesName(session.getName());
                SGSClientData data = getServer().getClientData(account);
                String channelName = data.get(SGSClientData.CURRENT_CHANNEL_NAME, String.class);
                SGSChannelData chnlData = getServer().getChannelData(channelName);
                data.set(SGSClientData.TALKING, Boolean.FALSE);
                NCloseMic tcm = new NCloseMic();
                tcm.nid = cm.nid;
                getServer().sendToNServer(Converter.getInstance().convert(tcm));
                Channel chnl = chnlData.getChannel();
                SGSBusinessProcess.channelwideDispatch(conv, session, chnl);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}
