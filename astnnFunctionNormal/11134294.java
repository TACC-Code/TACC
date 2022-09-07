class BackupThread extends Thread {
    void init() {
        try {
            _reader = new ReaderThread(this, _client, _inputStream);
            ThreadManager.getInstance().runInThreadFromPool(_reader);
            _writer = new WriterThread(_socket, _out, _client.getHost());
            ThreadManager.getInstance().runInThreadFromPool(_writer);
        } catch (OutOfMemoryError OOM) {
            CampaignData.mwlog.errLog(OOM.getMessage());
            try {
                _out.close();
                _out = null;
                _socket.close();
                _socket = null;
                _inputStream.close();
                _inputStream = null;
                _client = null;
                _reader = null;
                _writer = null;
                System.gc();
                shutdown(true);
            } catch (Exception e) {
                CampaignData.mwlog.errLog(e);
            }
        } catch (Exception ex) {
            CampaignData.mwlog.errLog(ex);
        }
    }
}
