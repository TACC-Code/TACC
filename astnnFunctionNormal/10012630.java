class BackupThread extends Thread {
	public IoConnector(int readwriteThreadNum) throws Exception {
		super();
		this.initIoReadWriteMachines(readwriteThreadNum);
	}
}
