class BackupThread extends Thread {
    public byte[] readFileNIO(String file) {
        try {
            System.out.println("Start NIO #2: " + (System.currentTimeMillis()));
            progMonitor.setNote(file);
            setProgress(1);
            System.out.println("Start NIO #3: " + (System.currentTimeMillis()));
            FileChannel fileChannel = new RandomAccessFile(file, "rw").getChannel();
            System.out.println("Start NIO #4: " + (System.currentTimeMillis()));
            int sz = (int) fileChannel.size();
            if (sz == 0) {
                return new byte[0];
            }
            OutOfMemExceptionRaised = false;
            System.out.println("MEMORY INFO: Used c1: " + (ManagementFactory.getMemoryMXBean().getHeapMemoryUsage()));
            byte[] buf = new byte[sz];
            int blockSize = sz / maxProgress;
            int curStep = 0;
            for (; curStep < maxProgress && !progMonitor.isCanceled(); curStep++) {
                int offSet = blockSize * curStep;
                MappedByteBuffer bb = fileChannel.map(FileChannel.MapMode.READ_ONLY, offSet, blockSize);
                byte[] readBytes = new byte[blockSize];
                bb.get(readBytes);
                System.arraycopy(readBytes, 0, buf, offSet, blockSize);
                setProgress(curStep);
            }
            if (progMonitor.isCanceled()) {
                try {
                    return Arrays.copyOf(buf, curStep * blockSize);
                } catch (OutOfMemoryError e) {
                    OutOfMemExceptionRaised = true;
                    return buf;
                }
            } else {
                int offSetRemaining = blockSize * maxProgress;
                if (sz - offSetRemaining > 0) {
                    MappedByteBuffer bb = fileChannel.map(FileChannel.MapMode.READ_ONLY, offSetRemaining, sz - offSetRemaining);
                    byte[] readBytes = new byte[sz - offSetRemaining];
                    bb.get(readBytes);
                    System.arraycopy(readBytes, 0, buf, offSetRemaining, sz - offSetRemaining);
                }
            }
            return buf;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return new byte[0];
    }
}
