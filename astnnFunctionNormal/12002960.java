class BackupThread extends Thread {
    public Module defineModule() {
        XilinxDevice xd = EngineThread.getGenericJob().getPart(CodeLabel.UNSCOPED);
        DualPortRam match = (DualPortRam) getLowestCost(DualPortRam.getDualPortMappers(xd, isLUT()));
        String sim_include_file = MemoryMapper.SIM_INCLUDE_PATH + match.getName() + ".v";
        String synth_include_file = MemoryMapper.SYNTH_INCLUDE_PATH + "unisim_comp.v";
        MemoryModule memoryModule = new MemoryModule(getName(), Collections.singleton(new MappedModule(match.getName(), sim_include_file, synth_include_file)));
        RamImplementation ramImpl = getImplementationRams(match, mpA, mpB);
        DualPortRam[][] ram_array = ramImpl.getRamArray();
        Net[] extra_douta_wires = ramImpl.getSpareA();
        Net[] extra_doutb_wires = ramImpl.getSpareB();
        memoryModule.addPort(clkPort);
        memoryModule.addPort(mpA.ren);
        if (mpA.writes()) {
            memoryModule.addPort(mpA.wen);
            memoryModule.addPort(mpA.din);
        }
        memoryModule.addPort(mpB.ren);
        if (mpB.writes()) {
            memoryModule.addPort(mpB.wen);
            memoryModule.addPort(mpB.din);
        }
        memoryModule.addPort(mpA.adr);
        memoryModule.addPort(mpB.adr);
        if (mpA.reads()) {
            memoryModule.addPort(mpA.dout);
        }
        if (mpB.reads()) {
            memoryModule.addPort(mpB.dout);
        }
        memoryModule.addPort(mpA.done);
        memoryModule.addPort(mpB.done);
        int result_depth = (int) java.lang.Math.ceil((double) getDepth() / (double) match.getDepth());
        Net[] wea = new Wire[result_depth];
        Net[] web = new Wire[result_depth];
        Net[] pre_douta = new Wire[result_depth];
        Net[] pre_doutb = new Wire[result_depth];
        for (int d = 0; d < result_depth; d++) {
            wea[d] = new Wire("wea_" + d, 1);
            if (mpA.writes()) {
                memoryModule.declare(wea[d]);
            }
            web[d] = new Wire("web_" + d, 1);
            if (mpB.writes()) {
                memoryModule.declare(web[d]);
            }
            pre_douta[d] = new Wire("pre_douta_" + d, getDataWidth());
            if (mpA.reads()) {
                memoryModule.declare(pre_douta[d]);
            }
            pre_doutb[d] = new Wire("pre_doutb_" + d, getDataWidth());
            if (mpB.reads()) {
                memoryModule.declare(pre_doutb[d]);
            }
            if (extra_douta_wires[d] != null && extra_douta_wires[d].getIdentifier().getToken().length() > 0) {
                memoryModule.declare(extra_douta_wires[d]);
            }
            if (extra_doutb_wires[d] != null && extra_doutb_wires[d].getIdentifier().getToken().length() > 0) {
                memoryModule.declare(extra_doutb_wires[d]);
            }
        }
        Register mux_outa = new Register("mux_outa", getDataWidth());
        if (mpA.reads()) {
            memoryModule.declare(mux_outa);
        }
        Register mux_outb = new Register("mux_outb", getDataWidth());
        if (mpB.reads()) {
            memoryModule.declare(mux_outb);
        }
        int extra_address_bits = getAddrWidth() - ram_array[0][0].getLibAddressWidth();
        for (int d = 0; d < result_depth; d++) {
            if (mpA.writes()) {
                if (extra_address_bits > 0) {
                    HexNumber hex_d = new HexNumber(new HexConstant(Integer.toHexString(d), extra_address_bits));
                    Expression addrA_equal = new Compare.Equals(mpA.adr.getRange(getAddrWidth() - 1, getAddrWidth() - extra_address_bits), hex_d);
                    Expression weA_and_addrA_equal = new Bitwise.And(mpA.wen, new Group(addrA_equal));
                    memoryModule.state(new Assign.Continuous(wea[d], weA_and_addrA_equal));
                } else {
                    memoryModule.state(new Assign.Continuous(wea[d], mpA.wen));
                }
            }
            if (mpB.writes()) {
                if (extra_address_bits > 0) {
                    HexNumber hex_d = new HexNumber(new HexConstant(Integer.toHexString(d), extra_address_bits));
                    Expression addrB_equal = new Compare.Equals(mpB.adr.getRange(getAddrWidth() - 1, getAddrWidth() - extra_address_bits), hex_d);
                    Expression weB_and_addrB_equal = new Bitwise.And(mpB.wen, new Group(addrB_equal));
                    memoryModule.state(new Assign.Continuous(web[d], weB_and_addrB_equal));
                } else {
                    memoryModule.state(new Assign.Continuous(web[d], mpB.wen));
                }
            }
        }
        Latency readLatency = getMemBank().getImplementation().getReadLatency();
        final boolean registerLutRead = ((getMemBank().getImplementation().getReadLatency().getMinClocks() > 0) && getMemBank().getImplementation().isLUT());
        SinglePortRamWriter.mergeResults(extra_address_bits, result_depth, getAddrWidth(), getDataWidth(), pre_douta, mux_outa, memoryModule, mpA.adr, clkPort, readLatency, registerLutRead, "A");
        SinglePortRamWriter.mergeResults(extra_address_bits, result_depth, getAddrWidth(), getDataWidth(), pre_doutb, mux_outb, memoryModule, mpB.adr, clkPort, readLatency, registerLutRead, "B");
        SequentialBlock doneBlock = new SequentialBlock();
        Register weaDone = new Register("wea_done", 1);
        Register webDone = new Register("web_done", 1);
        Register reaDone = new Register("rea_done", 1);
        Register rebDone = new Register("reb_done", 1);
        if (mpA.writes()) doneBlock.add(new Assign.NonBlocking(weaDone, mpA.wen));
        if (mpA.reads()) doneBlock.add(new Assign.NonBlocking(reaDone, mpA.ren));
        if (mpB.writes()) doneBlock.add(new Assign.NonBlocking(webDone, mpB.wen));
        if (mpB.reads()) doneBlock.add(new Assign.NonBlocking(rebDone, mpB.ren));
        EventControl clkEvent = new EventControl(new EventExpression.PosEdge(clkPort));
        memoryModule.state(new Always(new ProceduralTimingBlock(clkEvent, doneBlock)));
        if (mpA.writes() && mpA.reads()) memoryModule.state(new Assign.Continuous(mpA.done, new Bitwise.Or(weaDone, reaDone))); else if (mpA.writes()) memoryModule.state(new Assign.Continuous(mpA.done, weaDone)); else memoryModule.state(new Assign.Continuous(mpA.done, reaDone));
        if (mpB.writes() && mpB.reads()) memoryModule.state(new Assign.Continuous(mpB.done, new Bitwise.Or(webDone, rebDone))); else if (mpB.writes()) memoryModule.state(new Assign.Continuous(mpB.done, webDone)); else memoryModule.state(new Assign.Continuous(mpB.done, rebDone));
        if (mpA.reads()) {
            memoryModule.state(new Assign.Continuous(mpA.dout, mux_outa));
        }
        if (mpB.reads()) {
            memoryModule.state(new Assign.Continuous(mpB.dout, mux_outb));
        }
        debugContents(memoryModule);
        for (int c = 0; c < ram_array[0].length; c++) {
            for (int r = 0; r < ram_array.length; r++) {
                String mem_inst_comment = "Memory array element: COL: " + c + ", ROW: " + r;
                memoryModule.state(new InlineComment(mem_inst_comment, Comment.SHORT));
                DualPortRam ram = ram_array[r][c];
                memoryModule.state(ram.initialize());
                ModuleInstance instance = ram.instantiate();
                if (ram.isBlockRam16()) {
                    instance.addParameterValue(new ParameterSetting("WRITE_MODE_A", "\"" + mpA.getWriteMode() + "\""));
                    instance.addParameterValue(new ParameterSetting("WRITE_MODE_B", "\"" + mpB.getWriteMode() + "\""));
                }
                memoryModule.state(instance);
            }
        }
        return memoryModule;
    }
}
