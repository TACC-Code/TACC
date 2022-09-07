class BackupThread extends Thread {
        public void programChange(int nProgram) {
            FluidSynthesizer.this.programChange(getChannel(), nProgram);
        }
}
