class BackupThread extends Thread {
    public synchronized void write(LogRecord rec) throws LogException {
        if (operationsLogger.isLoggable(Level.FINER)) {
            operationsLogger.entering(SimpleLogFile.class.getName(), "write", rec);
        }
        try {
            if (!valid) throw new InvalidatedLogException("Cannot write to to " + "invalidated log");
            if (readonly) throw new LogException("Unable to write to read only log");
            if (out == null) {
                boolean append = true;
                File log = new File(name);
                outfile = new FileOutputStream(name, append);
                out = new HeaderlessObjectOutputStream(new BufferedOutputStream(outfile));
                if (log.length() == 0) {
                    out.writeLong(cookie);
                }
                out.reset();
            }
            out.writeObject(rec);
            out.flush();
            outfile.getFD().sync();
            if (persistenceLogger.isLoggable(Level.FINEST)) {
                persistenceLogger.log(Level.FINEST, "Wrote: {0}", rec);
            }
        } catch (InvalidClassException ice) {
            if (persistenceLogger.isLoggable(Level.WARNING)) {
                persistenceLogger.log(Level.WARNING, "Problem persisting LogRecord", ice);
            }
        } catch (NotSerializableException nse) {
            if (persistenceLogger.isLoggable(Level.WARNING)) {
                persistenceLogger.log(Level.WARNING, "Problem persisting LogRecord", nse);
            }
        } catch (IOException ioe) {
            if (persistenceLogger.isLoggable(Level.WARNING)) {
                persistenceLogger.log(Level.WARNING, "Problem persisting LogRecord", ioe);
            }
        } catch (SecurityException se) {
            if (persistenceLogger.isLoggable(Level.WARNING)) {
                persistenceLogger.log(Level.WARNING, "Problem persisting LogRecord", se);
            }
        }
        if (operationsLogger.isLoggable(Level.FINER)) {
            operationsLogger.exiting(SimpleLogFile.class.getName(), "write", rec);
        }
    }
}
