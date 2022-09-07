class BackupThread extends Thread {
        public void noteOn(int nNoteNumber, int nVelocity) {
            FluidSynthesizer.this.noteOn(getChannel(), nNoteNumber, nVelocity);
        }
}
