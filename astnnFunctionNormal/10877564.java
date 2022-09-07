class BackupThread extends Thread {
    private void prepareBoardCommunication() {
        List<ConnectionChannelConfig> commChannels = config.getConnParameter().getCommChannels();
        for (ConnectionChannelConfig currChannel : commChannels) {
            try {
                Class<BoardCommunication> connectionClass = (Class<BoardCommunication>) Class.forName(currChannel.getClassname());
                BoardCommunication commChannel = connectionClass.newInstance();
                commChannel.initByParameters(currChannel.getParametersAsHashMap());
                commChannel.setChannelName(currChannel.getChannelname());
                this.commChannelById.put(currChannel.getChannelname(), commChannel);
            } catch (Throwable e) {
                stdlog.log(Level.SEVERE, "Error in preparing board communication with JLab ID " + currChannel.getChannelname() + " Class " + currChannel.getClassname(), e);
                int option = JOptionPane.showConfirmDialog(GlobalsLocator.getMainFrame(), GlobalsLocator.translate("connection-error-warning"), GlobalsLocator.translate("connection-error-header"), JOptionPane.YES_OPTION);
                if (option == JOptionPane.YES_OPTION) {
                    resetConnectionConfig();
                    System.exit(0);
                }
            }
        }
    }
}
