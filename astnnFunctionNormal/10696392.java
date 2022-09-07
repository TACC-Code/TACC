class BackupThread extends Thread {
        public static void doString() {
            int add_index = CPU.cpu.direction << 1;
            Memory.mem_writew(CPU.Segs_ESphys + reg_edi.dword, Memory.mem_readw(base_ds + reg_esi.dword));
            reg_edi.dword += add_index;
            reg_esi.dword += add_index;
        }
}
