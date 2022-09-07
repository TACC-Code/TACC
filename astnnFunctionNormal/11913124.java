class BackupThread extends Thread {
    @Override
    public void setNNJDataSourceImpl() {
        hilbPhBuff = new double[this.getDataLayout().getChannelCount()][];
        super.setNNJDataSourceImpl();
    }
}
