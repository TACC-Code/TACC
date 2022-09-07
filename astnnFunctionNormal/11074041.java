class BackupThread extends Thread {
    private JButton createCloseTabButton(final TitledTab tab) {
        JButton closeButton = createXButton();
        closeButton.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                Object windowType = tab.getClientProperty(IRCWindowConstants.IRCWINDOW_DISCUSSION_TYPE_PROPERTY);
                if (windowType != null) {
                    if (windowType.equals(IRCWindowConstants.DISCUSSION_TYPE.PUBLIC_DISCUSSION)) {
                        System.out.println("Parting closetab");
                        IRCWindow window = (IRCWindow) tab.getContentComponent();
                        Channel s = window.getDocument().getChannel();
                        String message = "SandIRC client, you know you want, some come and get it";
                        s.part(message);
                        removeTab(tab);
                    } else {
                        removeTab(tab);
                    }
                }
            }
        });
        return closeButton;
    }
}
