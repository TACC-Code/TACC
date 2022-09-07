class BackupThread extends Thread {
    public final void testSaveZuordnungenLagermittelTransportmittelEinAuslagern() {
        try {
            ZuordnungLagermittelTransportmittelEinAuslagernDAO dao = new ZuordnungLagermittelTransportmittelEinAuslagernDAO();
            Hashtable<ZuordnungXYTO, Boolean> datensaetze = dao.getZuordnungenLagermittelTransportmittelEinAuslagern();
            assertTrue("[testGetZuordnungenLagermittelTransportmittelEinAuslagern]: Collection darf nicht NULL sein!", null != datensaetze);
            assertTrue("[testGetZuordnungenLagermittelTransportmittelEinAuslagern]: Collection muss Elemente enthalten!", datensaetze.size() > 0);
            int readCount = datensaetze.size();
            dao.saveZuordnungenLagermittelTransportmittelEinAuslagern(datensaetze);
            Hashtable<ZuordnungXYTO, Boolean> datensaetzeNachSchreiben = dao.getZuordnungenLagermittelTransportmittelEinAuslagern();
            assertTrue("[testGetZuordnungenLagermittelTransportmittelEinAuslagern]: Collection darf nicht NULL sein!", null != datensaetzeNachSchreiben);
            assertTrue("[testGetZuordnungenLagermittelTransportmittelEinAuslagern]: Collection muss Elemente enthalten!", datensaetzeNachSchreiben.size() > 0);
            int writeCount = datensaetzeNachSchreiben.size();
            assertTrue("[testGetZuordnungenLagermittelTransportmittelEinAuslagern]: Die Anzahl der Datens�tze muss �bereinstimmen!", readCount == writeCount);
        } finally {
        }
    }
}
