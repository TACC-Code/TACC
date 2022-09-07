class BackupThread extends Thread {
        public boolean onData(INonBlockingPipeline pipeline) throws IOException, BufferUnderflowException, MaxReadSizeExceededException {
            pipeline.write(pipeline.readByteBufferByLength(pipeline.available()));
            return true;
        }
}
