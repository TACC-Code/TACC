class BackupThread extends Thread {
    public void loeschen(int index) {
        if (index >= 0 && index < anzahl) {
            for (int i = index; i < anzahl - 1; i++) werte[i] = werte[i + 1];
            anzahl--;
        } else System.out.println("ung�ltiger Index");
    }
}
