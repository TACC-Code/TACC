class BackupThread extends Thread {
        OutputSelectionAction(cDVSTest30.cDVSTestBiasgen.OutputMux m, int i) {
            super(m.getChannelName(i));
            mux = m;
            channel = i;
            m.addObserver(this);
        }
}
