class BackupThread extends Thread {
    protected static void GRP2D(final short rm, int blah) {
        int which = (rm >> 3) & 7;
        if (rm >= 0xc0) {
            int val = blah & 0x1f;
            if (val == 0) return;
            Reg r = Modrm.GetEArd[rm];
            switch(which) {
                case 0x00:
                    r.dword = ROLD(val, r.dword);
                    break;
                case 0x01:
                    r.dword = RORD(val, r.dword);
                    break;
                case 0x02:
                    r.dword = RCLD(val, r.dword);
                    break;
                case 0x03:
                    r.dword = RCRD(val, r.dword);
                    break;
                case 0x04:
                case 0x06:
                    r.dword = SHLD(val, r.dword);
                    break;
                case 0x05:
                    r.dword = SHRD(val, r.dword);
                    break;
                case 0x07:
                    r.dword = SARD(val, r.dword);
                    break;
            }
        } else {
            int eaa = getEaa(rm);
            int val = blah & 0x1f;
            if (val == 0) return;
            switch(which) {
                case 0x00:
                    Memory.mem_writed(eaa, ROLD(val, Memory.mem_readd(eaa)));
                    break;
                case 0x01:
                    Memory.mem_writed(eaa, RORD(val, Memory.mem_readd(eaa)));
                    break;
                case 0x02:
                    Memory.mem_writed(eaa, RCLD(val, Memory.mem_readd(eaa)));
                    break;
                case 0x03:
                    Memory.mem_writed(eaa, RCRD(val, Memory.mem_readd(eaa)));
                    break;
                case 0x04:
                case 0x06:
                    Memory.mem_writed(eaa, SHLD(val, Memory.mem_readd(eaa)));
                    break;
                case 0x05:
                    Memory.mem_writed(eaa, SHRD(val, Memory.mem_readd(eaa)));
                    break;
                case 0x07:
                    Memory.mem_writed(eaa, SARD(val, Memory.mem_readd(eaa)));
                    break;
            }
        }
    }
}
