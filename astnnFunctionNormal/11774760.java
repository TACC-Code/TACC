class BackupThread extends Thread {
        public void run() {
            try {
                io = IOProvider.getDefault().getIO("Db4o Output", false);
                serverInstances.add(Db4oServerImpl.this);
                io.select();
                OutputWriter writer = io.getOut();
                String executable = buildExecutableName();
                String arguments = buildArguments();
                NbProcessDescriptor nbe = new NbProcessDescriptor(executable, arguments);
                proc = nbe.exec();
                if (proc != null) {
                    writer.println("Server started hosting " + Db4oServerImpl.this.getDatabase().getFile());
                    Db4oServerImpl.this.setState(STARTED);
                    pcs.firePropertyChange("started", false, true);
                    InputStream is = proc.getInputStream();
                    final BufferedReader outRdr = new BufferedReader(new InputStreamReader(is));
                    readOutput(writer, is);
                    try {
                        proc.waitFor();
                    } catch (InterruptedException ex) {
                        ex.printStackTrace();
                    }
                    writer.println("Server terminated (" + proc.exitValue() + ")");
                    writer.flush();
                    synchronized (this) {
                        proc = null;
                    }
                    writer.close();
                    Db4oServerImpl.this.setState(STOPPED);
                    pcs.firePropertyChange("started", true, false);
                    serverInstances.remove(Db4oServerImpl.this);
                } else {
                    writer.println("Could not start server process ");
                }
            } catch (IOException ex) {
                ex.printStackTrace();
                throw new Db4oServerException(ex);
            }
        }
}
