class BackupThread extends Thread {
        public void controlChange(int nController, int nValue) {
            FluidSynthesizer.this.controlChange(getChannel(), nController, nValue);
        }
}
