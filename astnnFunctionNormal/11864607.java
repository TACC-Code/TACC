class BackupThread extends Thread {
    public void onManagerEvent(ManagerEvent event) {
        String evento = event.getClass().getSimpleName();
        if (evento.equals("DialEvent")) {
            org.asteriskjava.manager.event.DialEvent call = (org.asteriskjava.manager.event.DialEvent) event;
            if (call.getDestination() != null) {
                if (call.getDestination().startsWith(jcVariabili.ASTERISK_SOURCE)) {
                    try {
                        Statement s = jcPostgreSQL.myDb.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
                        ResultSet wr = s.executeQuery("SELECT clienteid, nome, cognome FROM clienti WHERE tel='" + call.getCallerIdNum() + "' OR cell='" + call.getCallerIdNum() + "' LIMIT 1");
                        int agg = 0;
                        while (wr.next()) {
                            jcVariabili.FINESTRA_PRINCIPALE.jlClienteInRicezione.setText(wr.getString("nome").trim() + (wr.getObject("cognome") == null ? "" : " " + wr.getString("cognome").trim()));
                            jcVariabili.FINESTRA_PRINCIPALE.ASTERISK_CLIENTEID = wr.getInt("clienteid");
                            agg = 0;
                        }
                        if (agg == 0) {
                            jcVariabili.FINESTRA_PRINCIPALE.jlClienteInRicezione.setText("-");
                            jcVariabili.FINESTRA_PRINCIPALE.ASTERISK_CLIENTEID = -1;
                        }
                    } catch (SQLException ex) {
                        Logger.getLogger(jcAsterisk.class.getName()).log(Level.SEVERE, null, ex);
                        if (jcVariabili.DEBUG) System.out.println("Errore riconoscimento cliente: " + ex.toString());
                    }
                    jcVariabili.FINESTRA_PRINCIPALE.jlClienteNumeroInRicezione.setText(call.getCallerIdNum());
                    if (jcVariabili.DEBUG) System.out.println("ASTERISK\nEvento: " + evento + "\nStato: " + call.getDialStatus() + "\nNumero: " + call.getCallerIdNum() + "\nNome: " + call.getCallerIdName() + "\nCanale: " + call.getChannel() + "\nlinea: " + call.getLine() + "\nID: " + call.getUniqueId() + "\nCanale: " + call.getChannel() + "\nSorce: " + call.getSource() + "\nDest" + call.getDestination() + "\n\n");
                }
            }
        }
    }
}
