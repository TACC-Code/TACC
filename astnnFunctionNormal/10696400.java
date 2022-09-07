class BackupThread extends Thread {
        public static void doString() {
            int add_index = CPU.cpu.direction << 2;
            Memory.mem_writed(CPU.Segs_ESphys + reg_edi.dword, Memory.mem_readd(base_ds + reg_esi.dword));
            reg_edi.dword += add_index;
            reg_esi.dword += add_index;
        }
}
