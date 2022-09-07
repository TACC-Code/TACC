class BackupThread extends Thread {
        public int getProgram() {
            return FluidSynthesizer.this.getProgram(getChannel());
        }
}
