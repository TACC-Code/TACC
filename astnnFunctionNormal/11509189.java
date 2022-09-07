class BackupThread extends Thread {
        public void setPitchBend(int nBend) {
            FluidSynthesizer.this.setPitchBend(getChannel(), nBend);
        }
}
