class BackupThread extends Thread {
    protected void execute() {
        try {
            outer_loop: while (something_to_do) {
                fte.finishedAWrite(this);
                if (chunk != null) {
                    if (chunk.hasFailed()) {
                        logger.logComment("Writing error marker to UPL output");
                        ZipEntry marker = new ZipEntry("ERROR");
                        connection.getDataOutputStream().putNextEntry(marker);
                        connection.getDataOutputStream().closeEntry();
                        connection.doneWithDataOutputStream();
                        continue outer_loop;
                    }
                    if (chunk.getFileName().equals("NO_OVERWRITE")) {
                        logger.logComment("Adding no overwrite marker to stream for <" + chunk.getPortfolio().getUPLDirectoryName() + ">");
                        ZipEntry marker = new ZipEntry(chunk.getPortfolio().getUPLDirectoryName() + "/");
                        marker.setExtra(new byte[1]);
                        connection.getDataOutputStream().putNextEntry(marker);
                        connection.getDataOutputStream().closeEntry();
                        connection.doneWithDataOutputStream();
                        continue outer_loop;
                    }
                    synchronized (open_writers) {
                        Iterator iterator = open_writers.iterator();
                        while (iterator.hasNext()) {
                            WriterChunk wc = (WriterChunk) iterator.next();
                            if (wc.name.equals(chunk.getFileName())) {
                                if (wc.end_of_known_writes == chunk.getStartByte()) {
                                    logger.logComment(Thread.currentThread().getName() + " is handing off <" + chunk.getFileName() + "> <" + chunk.getStartByte() + ">");
                                    wc.appendChunk(chunk);
                                    chunk = null;
                                    continue outer_loop;
                                }
                            }
                        }
                        if (chunk.getFileName().equals(me_as_writer_chunk.name) && me_as_writer_chunk.end_of_known_writes == chunk.getStartByte()) {
                            logger.logComment(Thread.currentThread().getName() + " has contiguous chunk <" + chunk.getFileName() + "> <" + chunk.getStartByte() + ">");
                            me_as_writer_chunk.end_of_known_writes += chunk.getChunkLength();
                        } else {
                            if (me_as_writer_chunk.name != null) {
                                logger.logComment(Thread.currentThread().getName() + " has new chunk <" + chunk.getFileName() + "> <" + chunk.getStartByte() + ">");
                                connection.getDataOutputStream().flush();
                                connection.getDataOutputStream().closeEntry();
                            }
                            me_as_writer_chunk.initChunk(chunk);
                            String file_name;
                            if (chunk.getPortfolio() != null) {
                                file_name = chunk.getPortfolio().getUPLDirectoryName() + "/" + chunk.getFileName();
                            } else {
                                file_name = chunk.getFileName();
                            }
                            if (chunk.getStartByte() != 0) {
                                file_name = "CHUNK_" + chunk.getStartByte() + "_" + file_name;
                            }
                            ZipEntry zip_entry = new ZipEntry(file_name);
                            zip_entry.setExtra(new byte[] { chunk.getMode() });
                            connection.getDataOutputStream().putNextEntry(zip_entry);
                        }
                        open_writers.add(me_as_writer_chunk);
                    }
                    Chunk to_write = chunk;
                    while (to_write != null) {
                        logger.logComment(Thread.currentThread().getName() + " is writing <" + to_write.getFileName() + "> <" + to_write.getStartByte() + "> <" + to_write.getChunkLength() + "> <" + (char) to_write.getBuffer()[0] + ">");
                        connection.getDataOutputStream().write(to_write.getBuffer(), 0, (int) to_write.getChunkLength());
                        fte.disposeWrittenChunk(to_write);
                        synchronized (open_writers) {
                            to_write = me_as_writer_chunk.getChunk();
                            if (to_write == null) open_writers.remove(me_as_writer_chunk);
                        }
                    }
                    connection.getDataOutputStream().flush();
                }
            }
            connection.getDataOutputStream().closeEntry();
            connection.doneWithDataOutputStream();
        } catch (IOException e) {
            terminate(true);
            fte.notifyError(chunk, e);
        } catch (Exception e) {
            terminate(true);
            fte.notifyError(chunk, e);
        } finally {
        }
    }
}
