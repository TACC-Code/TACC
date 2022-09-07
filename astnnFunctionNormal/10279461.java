class BackupThread extends Thread {
    protected LogEntry getNext() throws LogEntryException {
        long offset = -1;
        ReusableBuffer item = null;
        try {
            if (channel.position() == channel.size()) return null;
            int numRead = channel.read(myInt);
            if (numRead < Integer.SIZE / 8) return null;
            myInt.flip();
            int entrySize = myInt.getInt();
            myInt.flip();
            offset = channel.position() - Integer.SIZE / 8;
            channel.position(offset);
            if (entrySize < 0) throw new LogEntryException("log entry with negative size detected: " + entrySize);
            item = BufferPool.allocate(entrySize);
            channel.read(item.getBuffer());
            item.flip();
            LogEntry e = LogEntry.deserialize(item, csumAlgo);
            csumAlgo.reset();
            return e;
        } catch (LogEntryException ex) {
            Logging.logMessage(Logging.LEVEL_ERROR, this, "***** INVALID LOG ENTRY *****");
            Logging.logMessage(Logging.LEVEL_ERROR, this, "the log contains an invalid log entry at offset %d, file will be truncated at offset %d", offset, offset);
            Logging.logMessage(Logging.LEVEL_ERROR, this, ex.getMessage());
            try {
                channel.close();
                FileOutputStream fout = new FileOutputStream(file, true);
                fout.getChannel().truncate(offset);
                fout.close();
                fis = new FileInputStream(file);
                channel = fis.getChannel();
                channel.position(offset);
            } catch (IOException exc) {
                throw new LogEntryException("Cannot truncate log file: " + ex);
            }
            return null;
        } catch (IOException ex) {
            Logging.logMessage(Logging.LEVEL_DEBUG, this, ex.getMessage());
            throw new LogEntryException("Cannot read log entry: " + ex);
        } finally {
            if (item != null) BufferPool.free(item);
        }
    }
}
