class BackupThread extends Thread {
    public PerfDataBuffer(VmIdentifier vmid) throws MonitorException {
        try {
            ByteBuffer bb = perf.attach(vmid.getLocalVmId(), vmid.getMode());
            createPerfDataBuffer(bb, vmid.getLocalVmId());
        } catch (IllegalArgumentException e) {
            try {
                String filename = PerfDataFile.getTempDirectory() + PerfDataFile.dirNamePrefix + Integer.toString(vmid.getLocalVmId());
                File f = new File(filename);
                FileChannel fc = new RandomAccessFile(f, "r").getChannel();
                ByteBuffer bb = fc.map(FileChannel.MapMode.READ_ONLY, 0L, (int) fc.size());
                fc.close();
                createPerfDataBuffer(bb, vmid.getLocalVmId());
            } catch (FileNotFoundException e2) {
                throw new MonitorException(vmid.getLocalVmId() + " not found", e);
            } catch (IOException e2) {
                throw new MonitorException("Could not map 1.4.1 file for " + vmid.getLocalVmId(), e2);
            }
        } catch (IOException e) {
            throw new MonitorException("Could not attach to " + vmid.getLocalVmId(), e);
        }
    }
}
