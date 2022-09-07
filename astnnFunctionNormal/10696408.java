class BackupThread extends Thread {
        public static void doString() {
            int add_index = CPU.cpu.direction;
            int si_base = base_ds;
            int di_base = CPU.Segs_ESphys;
            Memory.mem_writeb(di_base + reg_edi.dword, Memory.mem_readb(si_base + reg_esi.dword));
            reg_edi.dword += add_index;
            reg_esi.dword += add_index;
        }
}
