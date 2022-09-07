class BackupThread extends Thread {
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
}
