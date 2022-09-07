class BackupThread extends Thread {
        public int getController(int nController) {
            return FluidSynthesizer.this.getController(getChannel(), nController);
        }
}
