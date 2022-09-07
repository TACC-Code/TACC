class BackupThread extends Thread {
            public void run() {
                NickChangeEvent nce = (NickChangeEvent) e;
                ServerPanel panel = SandIRCFrame.getInstance().getServersPanel();
                ServerTreeNode node = panel.getOrCreateServerNode(e.getSession());
                IRCWindowContainer sessionContainer = node.getContainer();
                List<IRCWindow> windows = sessionContainer.getIRCWindows();
                windows = WindowUtilites.getWindowsForNick(nce.getNewNick(), e.getSession(), windows);
                for (final IRCWindow win : windows) {
                    if (nce.getNewNick().equals(e.getSession().getNick())) {
                        win.insertDefault("** You are now known as " + nce.getNewNick());
                    } else {
                        win.insertDefault("** " + nce.getOldNick() + " is now known as " + nce.getNewNick());
                    }
                    if (win.getDocument().getChannel() != null) win.updateUsersList();
                }
            }
}
