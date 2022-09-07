class BackupThread extends Thread {
    private Result setBytesIS(long lobID, InputStream inputStream, long length, boolean adjustLength) {
        long writeLength = 0;
        int blockLimit = (int) (length / lobBlockSize);
        int byteLimitOffset = (int) (length % lobBlockSize);
        if (byteLimitOffset == 0) {
            byteLimitOffset = lobBlockSize;
        } else {
            blockLimit++;
        }
        createBlockAddresses(lobID, 0, blockLimit);
        int[][] blockAddresses = getBlockAddresses(lobID, 0, blockLimit);
        byte[] dataBytes = new byte[lobBlockSize];
        for (int i = 0; i < blockAddresses.length; i++) {
            for (int j = 0; j < blockAddresses[i][LOBS.BLOCK_COUNT]; j++) {
                int localLength = lobBlockSize;
                if (i == blockAddresses.length - 1 && j == blockAddresses[i][LOBS.BLOCK_COUNT] - 1) {
                    localLength = byteLimitOffset;
                    java.util.Arrays.fill(dataBytes, (byte) 0);
                }
                try {
                    int count = 0;
                    while (localLength > 0) {
                        int read = inputStream.read(dataBytes, count, localLength);
                        if (read == -1) {
                            if (adjustLength) {
                                read = localLength;
                            } else {
                                return Result.newErrorResult(new EOFException());
                            }
                        } else {
                            writeLength += read;
                        }
                        localLength -= read;
                        count += read;
                    }
                } catch (IOException e) {
                    return Result.newErrorResult(e);
                }
                try {
                    getLobStore().setBlockBytes(dataBytes, blockAddresses[i][LOBS.BLOCK_ADDR] + j, 1);
                } catch (HsqlException e) {
                    return Result.newErrorResult(e);
                }
            }
        }
        return ResultLob.newLobSetResponse(lobID, writeLength);
    }
}
