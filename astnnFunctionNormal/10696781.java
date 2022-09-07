class BackupThread extends Thread {
        public void handler(int offset, int data) {
            Z80_Regs regs = new Z80_Regs();
            switch(data) {
                case 0x10:
                    return;
                case 0x34:
                    break;
                case 0x81:
                    break;
                case 0x82:
                    Z80_GetRegs(regs);
                    switch(regs.HL2) {
                        case 0x1BEE:
                            bosco_sample_play(0x0020 * 2, 0x08D7 * 2);
                            break;
                        case 0x1BF1:
                            bosco_sample_play(0x8F7 * 2, 0x0906 * 2);
                            break;
                        case 0x1BF4:
                            bosco_sample_play(0x11FD * 2, 0x07DD * 2);
                            break;
                        case 0x1BF7:
                            bosco_sample_play(0x19DA * 2, 0x07DE * 2);
                            break;
                        case 0x1BFA:
                            bosco_sample_play(0x21B8 * 2, 0x079F * 2);
                            break;
                    }
                    break;
                case 0x91:
                    cpu_writemem(0x9000, 0);
                    cpu_writemem(0x9000 + 1, 0);
                    cpu_writemem(0x9000 + 2, 0);
                    cpu_writemem(0x9000 + 3, 0);
                    break;
                case 0xA1:
                    break;
            }
            Z80_GetRegs(regs);
            while (regs.BC2 > 0) {
                cpu_writemem(regs.DE2, cpu_readmem(regs.HL2));
                regs.DE2 = (regs.DE2 + 1 & 0xFFFF);
                regs.HL2 = (regs.HL2 + 1 & 0xFFFF);
                regs.BC2 = (regs.BC2 - 1 & 0xFFFF);
            }
            Z80_SetRegs(regs);
        }
}
