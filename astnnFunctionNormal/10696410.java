class BackupThread extends Thread {
        public static void doString() {
            int add_index = CPU.cpu.direction;
            int count = CPU_Regs.reg_ecx.word();
            int si_base = base_ds;
            int di_base = CPU.Segs_ESphys;
            if (Config.FAST_STRINGS) {
                while (count > 1) {
                    int dst = di_base + reg_edi.word();
                    int src = si_base + reg_esi.word();
                    int dst_index = Paging.getDirectIndex(dst);
                    int src_index = Paging.getDirectIndexRO(src);
                    int src_len;
                    int dst_len;
                    if (dst_index < 0 || src_index < 0) {
                        break;
                    }
                    int len = count;
                    if (add_index < 0) {
                        src_len = (src & 0xFFF) + 1;
                        dst_len = (dst & 0xFFF) + 1;
                    } else {
                        src_len = 0x1000 - (src & 0xFFF);
                        dst_len = 0x1000 - (dst & 0xFFF);
                    }
                    if (len > src_len) len = src_len;
                    if (len > dst_len) len = dst_len;
                    if (add_index < 0) {
                        int ediCount = reg_edi.word() + 1;
                        int esiCount = reg_esi.word() + 1;
                        if (len > ediCount) len = ediCount;
                        if (len > esiCount) len = esiCount;
                    } else {
                        int ediCount = 0x10000 - reg_edi.word();
                        int esiCount = 0x10000 - reg_esi.word();
                        if (len > ediCount) len = ediCount;
                        if (len > esiCount) len = esiCount;
                    }
                    if (Math.abs(src_index - dst_index) < len) {
                        for (int i = 0; i < len; i++) {
                            Memory.host_writeb(dst_index, Memory.host_readb(src_index));
                            dst_index += add_index;
                            src_index += add_index;
                        }
                    } else {
                        if (add_index < 0) {
                            src_index -= len - 1;
                            dst_index -= len - 1;
                        }
                        Memory.host_memcpy(dst_index, src_index, len);
                    }
                    if (add_index < 0) {
                        reg_edi.word(reg_edi.word() - len);
                        reg_esi.word(reg_esi.word() - len);
                    } else {
                        reg_edi.word(reg_edi.word() + len);
                        reg_esi.word(reg_esi.word() + len);
                    }
                    reg_ecx.word(reg_ecx.word() - len);
                    count -= len;
                }
            }
            for (; count > 0; count--) {
                Memory.mem_writeb(di_base + reg_edi.word(), Memory.mem_readb(si_base + reg_esi.word()));
                reg_edi.word(reg_edi.word() + add_index);
                reg_esi.word(reg_esi.word() + add_index);
                reg_ecx.word_dec();
            }
        }
}
