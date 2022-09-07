class BackupThread extends Thread {
    public void writew(int address, int val) {
        int addr = (address & 4095);
        if (Memory.host_readw(hostmem + addr) == (val & 0xFFFF)) return;
        Memory.host_writew(hostmem + addr, val);
        if (write_map.readw(addr) == 0) {
            if (active_blocks != 0) return;
            active_count--;
            if (active_count == 0) Release();
            return;
        } else if (invalidation_map == null) {
            invalidation_map = new Ptr(4096);
        }
        invalidation_map.writew(addr, invalidation_map.readw(addr) + 0x101);
        InvalidateRange(addr, addr + 1);
    }
}
