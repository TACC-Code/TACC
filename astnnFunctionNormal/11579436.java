class BackupThread extends Thread {
        protected void receive(final StreamSession session, FileOutputStream fileOutputStream, SubMonitor monitor) throws IOException, SarosCancellationException, RemoteCancellationException {
            monitor.subTask(Messages.SendFileAction_monitor_start_receive_file);
            long fileSize = ((FileDescription) session.getInitiationDescription()).size;
            monitor.setWorkRemaining(fileSize > Integer.MAX_VALUE ? Integer.MAX_VALUE : (int) fileSize);
            long received = 0;
            InputStream in = session.getInputStream(0);
            byte[] buffer = new byte[session.getService().getChunkSize()[0]];
            StopWatch watch = new StopWatch();
            watch.start();
            try {
                int readBytes;
                boolean canceled = false;
                while ((readBytes = in.read(buffer)) > 0) {
                    received += readBytes;
                    fileOutputStream.write(buffer, 0, readBytes);
                    monitor.worked(readBytes);
                    monitor.subTask(watch.throughput(received));
                    if (monitor.isCanceled() && !canceled) {
                        getStreamSession().stopSession();
                        canceled = true;
                    }
                    if (Thread.interrupted() && !monitor.isCanceled()) throw new RemoteCancellationException();
                }
                fileOutputStream.flush();
                if (monitor.isCanceled()) throw new SarosCancellationException();
                receivedSuccessfully = received == fileSize;
            } finally {
                IOUtils.closeQuietly(fileOutputStream);
                IOUtils.closeQuietly(in);
                monitor.done();
            }
        }
}
