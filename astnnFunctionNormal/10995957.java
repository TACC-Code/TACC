class BackupThread extends Thread {
    public void run(Emulator em) throws EmulatorException {
        if (this.signedToUnsigned(em.readRegister(this.rA)) < this.signedToUnsigned(em.readRegister(this.rB))) em.writePC(em.readPC() + this.imm);
    }
}
