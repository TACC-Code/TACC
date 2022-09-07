class BackupThread extends Thread {
    public void visit(AVRInstr.LDD i) {
        $write_int8(i.rd, map_get(sram, $read_uint16(i.ar) + i.imm.value));
    }
}
