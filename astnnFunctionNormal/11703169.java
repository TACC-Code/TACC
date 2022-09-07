class BackupThread extends Thread {
    public IBond removeBond(int position) {
        IBond bond = bonds[position];
        bond.removeListener(this);
        for (int i = position; i < bondCount - 1; i++) {
            bonds[i] = bonds[i + 1];
        }
        bonds[bondCount - 1] = null;
        bondCount--;
        notifyChanged();
        return bond;
    }
}
