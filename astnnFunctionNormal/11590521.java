class BackupThread extends Thread {
    protected IDataSet loadDataSet(URL url) throws DataSetException, IOException {
        InputStream in = url.openStream();
        IDataSet ds = new XmlDataSet(in);
        return ds;
    }
}
