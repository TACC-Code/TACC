class BackupThread extends Thread {
        public void noteOff(int nNoteNumber, int nVelocity) {
            FluidSynthesizer.this.noteOff(getChannel(), nNoteNumber, nVelocity);
        }
}
