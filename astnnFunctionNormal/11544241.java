class BackupThread extends Thread {
    public void run() {
        fte.finishedARead(this);
        try {
            while (something_to_do) {
                if (chunk.getFileName().equals("NO_OVERWRITE")) {
                    if (logger.writeComments()) logger.logComment("Writing no-overwrite marker to buffer.");
                    fte.finishedARead(this);
                }
                if (logger.writeComments()) logger.logComment("Reading a chunk from file <" + chunk.getFileName() + "> from <" + chunk.getStartByte() + ">");
                int read = TargetSystem.getTargetSystem().readFile(creator.getRootDir(chunk.getPortfolio()) + chunk.getFileName(), chunk.getBuffer(), chunk.getStartByte(), chunk.getChunkLength(), 0, creator.getIncarnatedUser());
                if (logger.writeComments()) logger.logComment("Finished reading a chunk from file <" + chunk.getFileName() + "> from <" + chunk.getStartByte() + "> now at <" + (chunk.getStartByte() + read) + "> <" + (char) chunk.getBuffer()[0] + ">");
                fte.finishedARead(this);
            }
        } catch (Exception e) {
            terminate(true);
            fte.notifyError(chunk, e);
            chunk.setFailed(true);
            fte.finishedARead(this);
        }
        creator.notifyTerminated(this);
        creator = null;
        fte = null;
        chunk = null;
    }
}
