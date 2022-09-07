class BackupThread extends Thread {
    public void run(IRCEvent e) {
        final KickEvent ke = (KickEvent) e;
        ServerPanel panel = SandIRCFrame.getInstance().getServersPanel();
        ServerTreeNode node = panel.getOrCreateServerNode(e.getSession());
        IRCWindowContainer sessionContainer = node.getContainer();
        final IRCWindow win = sessionContainer.findWindowByChannel(ke.getChannel());
        SwingUtilities.invokeLater(new Runnable() {

            public void run() {
                win.insertDefault("*** " + ke.getWho() + " was kicked by " + ke.byWho() + " (" + ke.getMessage() + ")");
                win.updateUsersList();
            }
        });
    }
}
