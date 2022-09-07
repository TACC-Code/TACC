class BackupThread extends Thread {
    public static void DoString32(int prefixes, int type) {
        int si_base;
        int di_base;
        long count;
        int add_index;
        si_base = base_ds;
        di_base = CPU.Segs_ESphys;
        count = reg_ecx.dword & 0xFFFFFFFFl;
        if ((prefixes & PREFIX_REP) == 0) {
            count = 1;
        } else {
            CPU.CPU_Cycles++;
        }
        add_index = CPU.cpu.direction;
        if (count != 0) switch(type) {
            case R_OUTSB:
                for (; count > 0; count--) {
                    IO.IO_WriteB(reg_edx.word(), Memory.mem_readb(si_base + reg_esi.dword));
                    reg_esi.dword += add_index;
                    if ((prefixes & PREFIX_REP) != 0) reg_ecx.dword--;
                }
                break;
            case R_OUTSW:
                add_index <<= 1;
                for (; count > 0; count--) {
                    IO.IO_WriteW(reg_edx.word(), Memory.mem_readw(si_base + reg_esi.dword));
                    reg_esi.dword += add_index;
                    if ((prefixes & PREFIX_REP) != 0) reg_ecx.dword--;
                }
                break;
            case R_OUTSD:
                add_index <<= 2;
                for (; count > 0; count--) {
                    IO.IO_WriteD(reg_edx.word(), Memory.mem_readd(si_base + reg_esi.dword));
                    reg_esi.dword += add_index;
                    if ((prefixes & PREFIX_REP) != 0) reg_ecx.dword--;
                }
                break;
            case R_INSB:
                for (; count > 0; count--) {
                    Memory.mem_writeb(di_base + reg_edi.dword, IO.IO_ReadB(reg_edx.word()));
                    reg_edi.dword += add_index;
                    if ((prefixes & PREFIX_REP) != 0) reg_ecx.dword--;
                }
                break;
            case R_INSW:
                add_index <<= 1;
                for (; count > 0; count--) {
                    Memory.mem_writew(di_base + reg_edi.dword, IO.IO_ReadW(reg_edx.word()));
                    reg_edi.dword += add_index;
                    if ((prefixes & PREFIX_REP) != 0) reg_ecx.dword--;
                }
                break;
            case R_INSD:
                add_index <<= 1;
                for (; count > 0; count--) {
                    Memory.mem_writed(di_base + reg_edi.dword, IO.IO_ReadD(reg_edx.word()));
                    reg_edi.dword += add_index;
                    if ((prefixes & PREFIX_REP) != 0) reg_ecx.dword--;
                }
                break;
            case R_STOSB:
                for (; count > 0; count--) {
                    Memory.mem_writeb(di_base + reg_edi.dword, reg_eax.low());
                    reg_edi.dword += add_index;
                    if ((prefixes & PREFIX_REP) != 0) reg_ecx.dword--;
                }
                break;
            case R_STOSW:
                add_index <<= 1;
                for (; count > 0; count--) {
                    Memory.mem_writew(di_base + reg_edi.dword, reg_eax.word());
                    reg_edi.dword += add_index;
                    if ((prefixes & PREFIX_REP) != 0) reg_ecx.dword--;
                }
                break;
            case R_STOSD:
                add_index <<= 2;
                for (; count > 0; count--) {
                    Memory.mem_writed(di_base + reg_edi.dword, reg_eax.dword);
                    reg_edi.dword += add_index;
                    if ((prefixes & PREFIX_REP) != 0) reg_ecx.dword--;
                }
                break;
            case R_MOVSB:
                for (; count > 0; count--) {
                    Memory.mem_writeb(di_base + reg_edi.dword, Memory.mem_readb(si_base + reg_esi.dword));
                    reg_edi.dword += add_index;
                    reg_esi.dword += add_index;
                    if ((prefixes & PREFIX_REP) != 0) reg_ecx.dword--;
                }
                break;
            case R_MOVSW:
                add_index <<= 1;
                for (; count > 0; count--) {
                    Memory.mem_writew(di_base + reg_edi.dword, Memory.mem_readw(si_base + reg_esi.dword));
                    reg_edi.dword += add_index;
                    reg_esi.dword += add_index;
                    if ((prefixes & PREFIX_REP) != 0) reg_ecx.dword--;
                }
                break;
            case R_MOVSD:
                add_index <<= 2;
                for (; count > 0; count--) {
                    Memory.mem_writed(di_base + reg_edi.dword, Memory.mem_readd(si_base + reg_esi.dword));
                    reg_edi.dword += add_index;
                    reg_esi.dword += add_index;
                    if ((prefixes & PREFIX_REP) != 0) reg_ecx.dword--;
                }
                break;
            case R_LODSB:
                for (; count > 0; count--) {
                    reg_eax.low(Memory.mem_readb(si_base + reg_esi.dword));
                    reg_esi.dword += add_index;
                    if ((prefixes & PREFIX_REP) != 0) reg_ecx.dword--;
                }
                break;
            case R_LODSW:
                add_index <<= 1;
                for (; count > 0; count--) {
                    reg_eax.word(Memory.mem_readw(si_base + reg_esi.dword));
                    reg_esi.dword += add_index;
                    if ((prefixes & PREFIX_REP) != 0) reg_ecx.dword--;
                }
                break;
            case R_LODSD:
                add_index <<= 2;
                for (; count > 0; count--) {
                    reg_eax.dword = Memory.mem_readd(si_base + reg_esi.dword);
                    reg_esi.dword += add_index;
                    if ((prefixes & PREFIX_REP) != 0) reg_ecx.dword--;
                }
                break;
            case R_SCASB:
                {
                    short val2 = 0;
                    for (; count > 0; ) {
                        count--;
                        CPU.CPU_Cycles--;
                        val2 = Memory.mem_readb(di_base + reg_edi.dword);
                        reg_edi.dword += add_index;
                        if ((prefixes & PREFIX_REP) != 0) reg_ecx.dword--;
                        if ((reg_eax.low() == val2) != rep_zero) break;
                    }
                    CMPB(val2, reg_eax.low());
                }
                break;
            case R_SCASW:
                {
                    add_index <<= 1;
                    int val2 = 0;
                    for (; count > 0; ) {
                        count--;
                        CPU.CPU_Cycles--;
                        val2 = Memory.mem_readw(di_base + reg_edi.dword);
                        reg_edi.dword += add_index;
                        if ((prefixes & PREFIX_REP) != 0) reg_ecx.dword--;
                        if ((reg_eax.word() == val2) != rep_zero) break;
                    }
                    CMPW(val2, reg_eax.word());
                }
                break;
            case R_SCASD:
                {
                    add_index <<= 2;
                    int val2 = 0;
                    for (; count > 0; ) {
                        count--;
                        CPU.CPU_Cycles--;
                        val2 = Memory.mem_readd(di_base + reg_edi.dword);
                        reg_edi.dword += add_index;
                        if ((prefixes & PREFIX_REP) != 0) reg_ecx.dword--;
                        if ((reg_eax.dword == val2) != rep_zero) break;
                    }
                    CMPD(val2, reg_eax.dword);
                }
                break;
            case R_CMPSB:
                {
                    short val1 = 0, val2 = 0;
                    for (; count > 0; ) {
                        count--;
                        CPU.CPU_Cycles--;
                        val1 = Memory.mem_readb(si_base + reg_esi.dword);
                        val2 = Memory.mem_readb(di_base + reg_edi.dword);
                        reg_esi.dword += add_index;
                        reg_edi.dword += add_index;
                        if ((prefixes & PREFIX_REP) != 0) reg_ecx.dword--;
                        if ((val1 == val2) != rep_zero) break;
                    }
                    CMPB(val2, val1);
                }
                break;
            case R_CMPSW:
                {
                    add_index <<= 1;
                    int val1 = 0, val2 = 0;
                    for (; count > 0; ) {
                        count--;
                        CPU.CPU_Cycles--;
                        val1 = Memory.mem_readw(si_base + reg_esi.dword);
                        val2 = Memory.mem_readw(di_base + reg_edi.dword);
                        reg_esi.dword += add_index;
                        reg_edi.dword += add_index;
                        if ((prefixes & PREFIX_REP) != 0) reg_ecx.dword--;
                        if ((val1 == val2) != rep_zero) break;
                    }
                    CMPW(val2, val1);
                }
                break;
            case R_CMPSD:
                {
                    add_index <<= 2;
                    int val1 = 0, val2 = 0;
                    for (; count > 0; ) {
                        count--;
                        CPU.CPU_Cycles--;
                        val1 = Memory.mem_readd(si_base + reg_esi.dword);
                        val2 = Memory.mem_readd(di_base + reg_edi.dword);
                        reg_esi.dword += add_index;
                        reg_edi.dword += add_index;
                        if ((prefixes & PREFIX_REP) != 0) reg_ecx.dword--;
                        if ((val1 == val2) != rep_zero) break;
                    }
                    CMPD(val2, val1);
                }
                break;
            default:
                if (Log.level <= LogSeverities.LOG_ERROR) Log.log(LogTypes.LOG_CPU, LogSeverities.LOG_ERROR, "Unhandled string op " + type);
                Log.exit("Unhandled string op " + type);
        }
    }
}
