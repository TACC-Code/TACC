class BackupThread extends Thread {
    public void update(Observable obs, Object ss_msg) {
        String msg = (String) ss_msg;
        Spieler aktSpieler;
        VerwaltungClient verClient;
        Spieler[] a_spieler;
        int[] a_waren;
        if (obs instanceof VerwaltungClient && (msg.equals(Konstanten.NOMSG) || msg.equals(Konstanten.SPIELSTART))) {
            verClient = (VerwaltungClient) obs;
            if (msg.equals(Konstanten.SPIELSTART)) {
                zeilen = new String[verClient.getSpieleranzahl()][9];
                for (int i = 0; i < zeilen.length; i++) {
                    for (int j = 0; j < zeilen[i].length; j++) {
                        zeilen[i][j] = "X";
                    }
                }
                table = new JTable(zeilen, spalten);
                table.setEnabled(false);
                scrollPane = new JScrollPane(table);
                scrollPane.setPreferredSize(new Dimension(250, 120));
                table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
                if (infoTable != null) {
                    this.remove(infoTable);
                }
                infoTable = new JPanel();
                infoTable.add(scrollPane);
                this.add(infoTable);
            } else if (msg.equals(Konstanten.NOMSG)) {
                a_spieler = verClient.getA_spieler();
                zeilen = new String[a_spieler.length][9];
                for (int i = 0; i < a_spieler.length; i++) {
                    for (int j = 0; j < a_spieler.length - 1; j++) {
                        if (verClient.getPunkte(a_spieler[j].get_Spielernummer()) < verClient.getPunkte(a_spieler[j + 1].get_Spielernummer())) {
                            aktSpieler = a_spieler[j];
                            a_spieler[j] = a_spieler[j + 1];
                            a_spieler[j + 1] = aktSpieler;
                        }
                    }
                }
                CellRenderer aktRenderer = new CellRenderer();
                int[] reihenfolge = new int[a_spieler.length];
                for (int i = 0; i < a_spieler.length; i++) {
                    reihenfolge[i] = a_spieler[i].get_Spielernummer();
                }
                aktRenderer = new CellRenderer();
                aktRenderer.setIdReihenfolge(reihenfolge);
                table.getColumn(spalten[0]).setPreferredWidth(80);
                table.getColumn(spalten[0]).setCellRenderer(aktRenderer);
                for (int i = 1; i < 9; i++) {
                    aktRenderer = new CellRenderer();
                    aktRenderer.setIdReihenfolge(reihenfolge);
                    table.getColumn(spalten[i]).setPreferredWidth(55);
                    table.getColumn(spalten[i]).setCellRenderer(aktRenderer);
                }
                for (int i = 0; i < a_spieler.length; i++) {
                    zeilen[i][0] = "" + a_spieler[i].getName();
                    zeilen[i][1] = "" + verClient.getPunkte(a_spieler[i].get_Spielernummer());
                    zeilen[i][2] = "" + a_spieler[i].get_zenturios();
                    zeilen[i][3] = "" + a_spieler[i].getBotschafter();
                    a_waren = verClient.getA_Waren(a_spieler[i].get_Spielernummer());
                    for (int j = 0; j < a_waren.length; j++) {
                        zeilen[i][4 + j] = "" + a_waren[j];
                    }
                }
                for (int i = 0; i < zeilen.length; i++) {
                    for (int j = 0; j < zeilen[i].length; j++) {
                        table.getModel().setValueAt(zeilen[i][j], i, j);
                    }
                }
                this.setAktiv(verClient.isSpieleraktiv());
                this.setAktAktion(verClient.getAktAktion());
                for (int i = 0; i < a_spieler.length; i++) {
                    if (a_spieler[i].isStartspieler()) {
                        this.setStartSpieler(a_spieler[i].getName(), a_spieler[i].get_Spielernummer());
                    }
                    this.setMeinGeld(verClient.getMeinSpielerByID().getGeld());
                    lblTraining.setText("" + verClient.getMeinSpielerByID().getTrainingsFaktor());
                }
                this.setAktProvinzenzahl(verClient.getZFreieProvinzen());
            }
        }
    }
}
