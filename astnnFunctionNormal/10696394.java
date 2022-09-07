class BackupThread extends Thread {
        public static void doString() {
            int add_index = CPU.cpu.direction << 1;
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
                    int len = count << 1;
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
                        len = len & ~1;
                    } else {
                        int ediCount = 0x10000 - reg_edi.word();
                        int esiCount = 0x10000 - reg_esi.word();
                        if (len > ediCount) len = ediCount;
                        if (len > esiCount) len = esiCount;
                        len = len & ~1;
                    }
                    if (len <= 0) {
                        Memory.mem_writew(di_base + reg_edi.word(), Memory.mem_readw(si_base + reg_esi.word()));
                        reg_edi.word(reg_edi.word() + add_index);
                        reg_esi.word(reg_esi.word() + add_index);
                        reg_ecx.word_dec();
                        count--;
                    } else {
                        int thisCount = (len >> 1);
                        if (Math.abs(src_index - dst_index) < len) {
                            for (int i = 0; i < thisCount; i++) {
                                Memory.host_writew(dst_index, Memory.host_readw(src_index));
                                dst_index += add_index;
                                src_index += add_index;
                            }
                        } else {
                            if (add_index < 0) {
                                src_index -= len - 2;
                                dst_index -= len - 2;
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
                        reg_ecx.word(reg_ecx.word() - thisCount);
                        count -= thisCount;
                    }
                }
            }
            for (; count > 0; count--) {
                Memory.mem_writew(di_base + reg_edi.word(), Memory.mem_readw(si_base + reg_esi.word()));
                reg_edi.word(reg_edi.word() + add_index);
                reg_esi.word(reg_esi.word() + add_index);
                reg_ecx.word_dec();
            }
        }
}
