class BackupThread extends Thread {
    private void readObject(java.io.ObjectInputStream in) throws IOException, ClassNotFoundException {
        synchronized (_global_read_write_lock) {
            int direction = in.readInt();
            _coder = IStreamCoder.make(IStreamCoder.Direction.swigToEnum(direction));
            _coder.setCodec(ICodec.ID.swigToEnum(in.readInt()));
            _coder.setFlags(in.readInt());
            _coder.setPixelType(IPixelFormat.Type.swigToEnum(in.readInt()));
            _coder.setWidth(in.readInt());
            _coder.setHeight(in.readInt());
            IRational tb = IRational.make(in.readInt(), in.readInt());
            _coder.setTimeBase(tb);
            _framerate = IRational.make(in.readInt(), in.readInt());
            _coder.setNumPicturesInGroupOfPictures(in.readInt());
            _coder.setBitRate(in.readInt());
            _coder.setBitRateTolerance(in.readInt());
            _coder.setChannels(in.readInt());
            _coder.setSampleRate(in.readInt());
            _coder.setSampleFormat(IAudioSamples.Format.swigToEnum(in.readInt()));
            Properties props = (Properties) in.readObject();
            Iterator keys = props.keySet().iterator();
            while (keys.hasNext()) {
                String key = (String) keys.next();
                _coder.setProperty(key, props.getProperty(key));
            }
            int extradatasize = in.readInt();
        }
    }
}
