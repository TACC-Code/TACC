class BackupThread extends Thread {
    public pnlClientsBar() {
        initComponents();
        if (!java.beans.Beans.isDesignTime()) {
            pnlClients = new BackgroundPanel();
            pnlClients.setLayout(new javax.swing.BoxLayout(pnlClients, javax.swing.BoxLayout.Y_AXIS));
            jScrollPane3.setViewportView(pnlClients);
            oneSecondTimer = new javax.swing.Timer(1000, new ActionListener() {

                public void actionPerformed(ActionEvent e) {
                    clearDisconnectedClients(clientsButtonGroup);
                    Integer selectedID = getSelectedClient();
                    Set set = Server.getChannelsArray().entrySet();
                    Iterator it = set.iterator();
                    while (it.hasNext()) {
                        Map.Entry entry = (Map.Entry) it.next();
                        if (!isClientAlreadyConnected(clientsButtonGroup, entry.getKey().toString())) {
                            JToggleButton cmdClient01 = createClientButton(entry.getKey().toString(), entry.getValue().toString());
                            clientsButtonGroup.add(cmdClient01);
                            pnlClients.add(cmdClient01);
                        }
                    }
                    if (selectedID == -1 && pnlClients.getComponentCount() > 0) {
                        ((JToggleButton) pnlClients.getComponent(0)).setSelected(true);
                        fireClientSelected(((JToggleButton) pnlClients.getComponent(0)).getName());
                    }
                    pnlClients.revalidate();
                    pnlClients.repaint();
                    fireOneSecondTimerTicked();
                }
            });
            popup = new JPopupMenu();
            mi = new JMenuItem("Disconnect");
            mi.addActionListener(new ActionListener() {

                public void actionPerformed(ActionEvent e) {
                    if (e.getActionCommand().equals("disconnect")) {
                        fireDisconnectionRequested(popUpInvoker);
                    }
                }
            });
            mi.setActionCommand("disconnect");
            popup.add(mi);
            popup.setOpaque(true);
            popup.setLightWeightPopupEnabled(true);
        }
    }
}
