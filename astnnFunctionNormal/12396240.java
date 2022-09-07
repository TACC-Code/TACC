class BackupThread extends Thread {
    public final void testSaveZuordnungenAuswahlkriteriumLagermittel() {
        try {
            ZuordnungAuswahlkriteriumLagermittelDAO dao = new ZuordnungAuswahlkriteriumLagermittelDAO();
            Hashtable<ZuordnungXYTO, Boolean> datensaetze = dao.getZuordnungenAuswahlkriteriumLagermittel();
            assertTrue("[testSaveZuordnungenAuswahlkriteriumLagermittel]: Collection darf nicht NULL sein!", null != datensaetze);
            assertTrue("[testSaveZuordnungenAuswahlkriteriumLagermittel]: Collection muss Elemente enthalten!", datensaetze.size() > 0);
            int readCount = datensaetze.size();
            dao.saveZuordnungenAuswahlkriteriumLagermittel(datensaetze);
            Hashtable<ZuordnungXYTO, Boolean> datensaetzeNachSchreiben = dao.getZuordnungenAuswahlkriteriumLagermittel();
            assertTrue("[testSaveZuordnungenAuswahlkriteriumLagermittel]: Collection darf nicht NULL sein!", null != datensaetzeNachSchreiben);
            assertTrue("[testSaveZuordnungenAuswahlkriteriumLagermittel]: Collection muss Elemente enthalten!", datensaetzeNachSchreiben.size() > 0);
            int writeCount = datensaetzeNachSchreiben.size();
            assertTrue("[testSaveZuordnungenAuswahlkriteriumLagermittel]: Die Anzahl der Datens�tze muss �bereinstimmen!", readCount == writeCount);
        } finally {
        }
    }
}
