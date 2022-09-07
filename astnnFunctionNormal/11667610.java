class BackupThread extends Thread {
    public void visit(AVRInstr.CBR i) {
        $write_int8(i.rd, performAnd($read_int8(i.rd), ~i.imm.value));
    }
}
