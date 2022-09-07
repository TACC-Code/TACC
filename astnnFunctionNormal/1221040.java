class BackupThread extends Thread {
    public void run(Emulator em) throws EmulatorException {
        em.writeRegister(this.rC, ~(em.readRegister(this.rA) | em.readRegister(this.rB)));
    }
}
