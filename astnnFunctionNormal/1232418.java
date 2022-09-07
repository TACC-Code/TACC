class BackupThread extends Thread {
    public void writed(int address, int val) {
        int addr = (address & 4095);
        if (Memory.host_readd(hostmem + addr) == (val & 0xFFFFFFFF)) return;
        Memory.host_writed(hostmem + addr, val);
        if (write_map.readd(addr) == 0) {
            if (active_blocks != 0) return;
            active_count--;
            if (active_count == 0) Release();
            return;
        } else if (invalidation_map == null) {
            invalidation_map = new Ptr(4096);
        }
        invalidation_map.writed(addr, invalidation_map.readd(addr) + 0x1010101);
        InvalidateRange(addr, addr + 3);
    }
}
