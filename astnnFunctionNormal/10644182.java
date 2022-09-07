class BackupThread extends Thread {
    private void setBlockPosition(long offsetInBlock) throws IOException {
        if (finalized) {
            if (!isRecovery) {
                throw new IOException("Write to offset " + offsetInBlock + " of block " + block + " that is already finalized.");
            }
            if (offsetInBlock > datanode.data.getLength(block)) {
                throw new IOException("Write to offset " + offsetInBlock + " of block " + block + " that is already finalized and is of size " + datanode.data.getLength(block));
            }
            return;
        }
        if (datanode.data.getChannelPosition(block, streams) == offsetInBlock) {
            return;
        }
        long offsetInChecksum = BlockMetadataHeader.getHeaderSize() + offsetInBlock / bytesPerChecksum * checksumSize;
        if (out != null) {
            out.flush();
        }
        if (checksumOut != null) {
            checksumOut.flush();
        }
        if (offsetInBlock % bytesPerChecksum != 0) {
            LOG.info("setBlockPosition trying to set position to " + offsetInBlock + " for block " + block + " which is not a multiple of bytesPerChecksum " + bytesPerChecksum);
            computePartialChunkCrc(offsetInBlock, offsetInChecksum, bytesPerChecksum);
        }
        LOG.info("Changing block file offset of block " + block + " from " + datanode.data.getChannelPosition(block, streams) + " to " + offsetInBlock + " meta file offset to " + offsetInChecksum);
        datanode.data.setChannelPosition(block, streams, offsetInBlock, offsetInChecksum);
    }
}
