class BackupThread extends Thread {
    public void visit(AVRInstr.SUBI i) {
        $write_int8(i.rd, performSubtraction($read_int8(i.rd), i.imm.value, 0));
    }
}
