class BackupThread extends Thread {
    private void clearDisconnectedClients(ButtonGroup gr) {
        int clientsNumber = pnlClients.getComponentCount();
        for (int i = 0; i < clientsNumber; i++) {
            try {
                if (pnlClients.getComponent(i) instanceof javax.swing.JToggleButton) {
                    JToggleButton el = (JToggleButton) pnlClients.getComponent(i);
                    Set set = Server.getChannelsArray().entrySet();
                    Iterator it = set.iterator();
                    boolean flag = false;
                    while (it.hasNext()) {
                        Map.Entry entry = (Map.Entry) it.next();
                        if (el.getName().equalsIgnoreCase(entry.getKey().toString())) {
                            flag = true;
                        }
                    }
                    if (!flag) {
                        pnlClients.remove(el);
                    }
                }
            } catch (ArrayIndexOutOfBoundsException ex) {
                log.error(ex.getMessage());
            }
        }
        if (pnlClients.getComponentCount() == 1 && ((JToggleButton) pnlClients.getComponent(0)).isSelected() == false) {
            ((JToggleButton) pnlClients.getComponent(0)).setSelected(true);
            fireClientSelected(((JToggleButton) pnlClients.getComponent(0)).getName());
        }
    }
}
