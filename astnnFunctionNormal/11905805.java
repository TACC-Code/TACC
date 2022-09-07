class BackupThread extends Thread {
    public Product read() throws Exception {
        Product product = delegate.read();
        ThreadUtils.writeThreadExecutionMessage("read", product);
        return product;
    }
}
