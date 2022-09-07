class BackupThread extends Thread {
    private void writeObject(java.io.ObjectOutputStream out) throws IOException {
        synchronized (_global_read_write_lock) {
            out.writeInt(_coder.getDirection().swigValue());
            out.writeInt(_coder.getCodecID().swigValue());
            out.writeInt(_coder.getFlags());
            out.writeInt(_coder.getPixelType().swigValue());
            out.writeInt(_coder.getWidth());
            out.writeInt(_coder.getHeight());
            out.writeInt(_coder.getTimeBase().getNumerator());
            out.writeInt(_coder.getTimeBase().getDenominator());
            out.writeInt(_framerate.getNumerator());
            out.writeInt(_framerate.getDenominator());
            out.writeInt(_coder.getNumPicturesInGroupOfPictures());
            out.writeInt(_coder.getBitRate());
            out.writeInt(_coder.getBitRateTolerance());
            out.writeInt(_coder.getChannels());
            out.writeInt(_coder.getSampleRate());
            out.writeInt(_coder.getSampleFormat().swigValue());
            if (_keys == null) {
                _keys = _coder.getPropertyNames();
            }
            _props = new Properties();
            ;
            for (String key : _keys) {
                if (_coder.getPropertyAsString(key) != null) _props.setProperty(key, _coder.getPropertyAsString(key));
            }
            out.writeObject(_props);
            out.writeInt(_coder.getExtraDataSize());
        }
    }
}
