class BackupThread extends Thread {
    public void stateLogic(Module module, StateMachine mach) {
        module.declare(memoryDec);
        final Wire oldTime = new Wire("ccOldTime", 32);
        final Wire isPassThrough = new Wire("ccPassThrough", 1);
        final Compare.EQ ef = new Compare.EQ(this.readPointer, this.writePointer);
        final Logical.And simultaneous = new Logical.And(mach.getAllGoWire(), mach.getAllDoneWire());
        module.state(new Assign.Continuous(isPassThrough, new Logical.And(ef, new Group(simultaneous))));
        module.state(new Assign.Continuous(oldTime, new Conditional(new Group(isPassThrough), this.globalTime, new MemoryElement(this.memory, this.readPointer))));
        module.state(new Assign.Continuous(this.cycles, new net.sf.openforge.verilog.model.Math.Subtract(this.globalTime, oldTime)));
        stateGlobalTimer(module, mach);
        stateFifo(module, mach, isPassThrough);
        stateCapture(module, mach);
    }
}
