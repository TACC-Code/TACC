class BackupThread extends Thread {
    public void disAssemble(int[] memory, int pc, int acc, int x, int y, byte status, int interruptInExec, int lastInterrupt) {
        if (DEBUG || (interruptInExec > 0)) {
            int startPC = pc;
            StringBuffer line = new StringBuffer();
            if (!noinstructions) {
                line.append(Integer.toHexString(pc));
                for (int i = 5 - line.length(); i >= 0; i--) {
                    line.append(" ");
                }
                int data = MOS6510Ops.INSTRUCTION_SET[memory[pc++]];
                int op = data & MOS6510Ops.OP_MASK;
                int addrMode = data & MOS6510Ops.ADDRESSING_MASK;
                int adMode = addrMode >> 8;
                boolean read = (data & MOS6510Ops.READ) != 0;
                boolean write = (data & MOS6510Ops.WRITE) != 0;
                int adr = 0;
                int tmp = 0;
                int p1 = memory[pc];
                line.append(MOS6510Ops.INS_STR[op]);
                line.append(MOS6510Ops.ADR_STR_PRE[adMode]);
                switch(addrMode) {
                    case MOS6510Ops.IMMEDIATE:
                        pc++;
                        data = p1;
                        line.append("$" + Hex.hex2(data));
                        break;
                    case MOS6510Ops.ABSOLUTE:
                        pc++;
                        adr = (fetchByte(pc++) << 8) + p1;
                        line.append("$" + Hex.hex2(adr));
                        if (read) {
                            data = fetchByte(adr);
                            line.append("=" + Hex.hex2(data));
                        }
                        break;
                    case MOS6510Ops.ZERO:
                        pc++;
                        adr = p1;
                        line.append("$" + Hex.hex2(adr));
                        if (read) {
                            data = fetchByte(adr);
                            line.append("=" + Hex.hex2(data));
                        }
                        break;
                    case MOS6510Ops.ZERO_X:
                    case MOS6510Ops.ZERO_Y:
                        pc++;
                        fetchByte(p1);
                        if (addrMode == MOS6510Ops.ZERO_X) adr = (p1 + x) & 0xff; else adr = (p1 + y) & 0xff;
                        line.append("$" + Hex.hex2(adr));
                        if (read) {
                            data = fetchByte(adr);
                            line.append("=" + Hex.hex2(data));
                        }
                        break;
                    case MOS6510Ops.ABSOLUTE_X:
                    case MOS6510Ops.ABSOLUTE_Y:
                        pc++;
                        adr = fetchByte(pc++) << 8;
                        if (addrMode == MOS6510Ops.ABSOLUTE_X) p1 += x; else p1 += y;
                        data = fetchByte(adr + (p1 & 0xff));
                        adr += p1;
                        line.append("$" + Hex.hex2(adr));
                        if (read && (p1 > 0xff || write)) {
                            data = fetchByte(adr);
                            line.append("=" + Hex.hex2(data));
                        }
                        break;
                    case MOS6510Ops.RELATIVE:
                        pc++;
                        adr = pc + (byte) p1;
                        if (((adr ^ pc) & 0xff00) > 0) {
                            tmp = 2;
                        } else {
                            tmp = 1;
                        }
                        line.append("$" + Hex.hex2(adr));
                        break;
                    case MOS6510Ops.ACCUMULATOR:
                        data = acc;
                        write = false;
                        break;
                    case MOS6510Ops.INDIRECT_X:
                        pc++;
                        tmp = (p1 + x) & 0xff;
                        adr = (fetchByte(tmp + 1) << 8);
                        adr |= fetchByte(tmp);
                        line.append("$" + Hex.hex2(adr));
                        if (read) {
                            data = fetchByte(adr);
                            line.append("=" + Hex.hex2(data));
                        }
                        break;
                    case MOS6510Ops.INDIRECT_Y:
                        pc++;
                        adr = (fetchByte(p1 + 1) << 8);
                        p1 = fetchByte(p1);
                        p1 += y;
                        data = fetchByte(adr + (p1 & 0xff));
                        adr += p1;
                        line.append("$" + Hex.hex2(adr));
                        if (read && (p1 > 0xff || write)) {
                            data = fetchByte(adr);
                            line.append("=" + Hex.hex2(data));
                        }
                        break;
                    case MOS6510Ops.INDIRECT:
                        pc++;
                        adr = (fetchByte(pc) << 8) + p1;
                        tmp = (adr & 0xfff00) | ((adr + 1) & 0xff);
                        adr = (fetchByte(tmp) << 8) + fetchByte(adr);
                        line.append("$" + Hex.hex2(adr));
                        break;
                }
                line.append(MOS6510Ops.ADR_STR_POST[adMode]);
                StringBuffer bytes = new StringBuffer();
                for (int i = startPC; i < pc; i++) {
                    bytes.append(Integer.toString(memory[i], 16));
                    bytes.append(" ");
                }
                for (int i = 9 - bytes.length(); i > 0; i--) {
                    bytes.append(" ");
                }
                if (interruptInExec > 0) {
                    if (lastInterrupt == MOS6510Core.NMI_INT) bytes.append("[NMI] " + interruptInExec + " "); else bytes.append("[IRQ] " + interruptInExec + " ");
                } else {
                    bytes.append("        ");
                }
                if (line.length() > 6) line.insert(6, bytes.toString());
                for (int i = 45 - line.length(); i > 0; i--) {
                    line.append(" ");
                }
                if (!noinstructions) {
                    line.append("A:");
                    line.append(Hex.hex2(acc));
                    line.append(" X:");
                    line.append(Hex.hex2(x));
                    line.append(" Y:");
                    line.append(Hex.hex2(y));
                    line.append(" ");
                    boolean carry = ((status & 0x01) != 0);
                    boolean zero = ((status & 0x02) != 0);
                    boolean disableInterupt = ((status & 0x04) != 0);
                    boolean decimal = ((status & 0x08) != 0);
                    boolean brk = ((status & 0x10) != 0);
                    boolean overflow = ((status & 0x40) != 0);
                    boolean sign = ((status & 0x80) != 0);
                    line.append(carry ? "C" : "-");
                    line.append(zero ? "Z" : "-");
                    line.append(disableInterupt ? "I" : "-");
                    line.append(decimal ? "D" : "-");
                    line.append(brk ? "B" : "-");
                    line.append(overflow ? "O" : "-");
                    line.append(sign ? "S" : "-");
                    if (DEBUG_IRQ) {
                        line.append(" ");
                        line.append(cpu.NMILow ? "N" : "-");
                        line.append(cpu.getIRQLow() ? "I" : "-");
                    }
                    line.append(" S:" + Hex.hex2(cpu.getSP()));
                    line.append(" " + Hex.hex2((int) (cpu.cycles - lastCycles)));
                    getFunction(memory, startPC, line);
                }
                System.out.println(line);
                lastCycles = cpu.cycles;
            }
        }
    }
}
