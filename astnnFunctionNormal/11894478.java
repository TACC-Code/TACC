class BackupThread extends Thread {
    protected void connectAll() {
        for (int i = 0; i < qPSs.size(); i++) {
            ConnectPV connectPV1 = new ConnectPV(setPVChs[i], this);
            Thread thread1 = new Thread(connectPV1);
            thread1.start();
            ConnectPV connectPV2 = new ConnectPV(rbPVChs[i], this);
            Thread thread2 = new Thread(connectPV2);
            thread2.start();
            getChannelVec(setPVChs[i]).add(setPVCell[i]);
            getChannelVec(rbPVChs[i]).add(rbPVCell[i]);
            Channel.flushIO();
        }
        final TableProdder prodder = new TableProdder(quadTableModel);
        prodder.start();
    }
}
