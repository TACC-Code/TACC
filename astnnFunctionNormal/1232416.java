class BackupThread extends Thread {
    public void writeb(int address, int val) {
        int addr = (address & 4095);
        if (Memory.host_readb(hostmem + addr) == (val & 0xFF)) return;
        Memory.host_writeb(hostmem + addr, (short) val);
        if (write_map.readb(addr) == 0) {
            if (active_blocks != 0) return;
            active_count--;
            if (active_count == 0) Release();
            return;
        } else if (invalidation_map == null) {
            invalidation_map = new Ptr(4096);
        }
        invalidation_map.p[addr]++;
        InvalidateRange(addr, addr);
    }
}
