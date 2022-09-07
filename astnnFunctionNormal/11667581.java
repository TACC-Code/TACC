class BackupThread extends Thread {
    public void visit(AVRInstr.ANDI i) {
        $write_int8(i.rd, performAnd($read_int8(i.rd), i.imm.value));
    }
}
