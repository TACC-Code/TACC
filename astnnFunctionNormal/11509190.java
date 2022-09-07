class BackupThread extends Thread {
        public int getPitchBend() {
            return FluidSynthesizer.this.getPitchBend(getChannel());
        }
}
