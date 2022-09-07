class BackupThread extends Thread {
    private void LoadELFSections(ByteBuffer f, SceModule module, int baseAddress, Elf32 elf, int elfOffset, boolean analyzeOnly) throws IOException {
        List<Elf32SectionHeader> sectionHeaderList = elf.getSectionHeaderList();
        Memory mem = Memory.getInstance();
        for (Elf32SectionHeader shdr : sectionHeaderList) {
            if (log.isTraceEnabled()) {
                log.trace(String.format("ELF Section Header: %s", shdr.toString()));
            }
            int memOffset = baseAddress + (int) shdr.getSh_addr();
            if (!Memory.isAddressGood(memOffset)) {
                memOffset = (int) shdr.getSh_addr();
            }
            int len = (int) shdr.getSh_size();
            int flags = shdr.getSh_flags();
            if (flags != SHF_NONE && Memory.isAddressGood(memOffset)) {
                boolean read = (flags & SHF_ALLOCATE) != 0;
                boolean write = (flags & SHF_WRITE) != 0;
                boolean execute = (flags & SHF_EXECUTE) != 0;
                MemorySection memorySection = new MemorySection(memOffset, len, read, write, execute);
                MemorySections.getInstance().addMemorySection(memorySection);
            }
            if ((flags & SHF_ALLOCATE) != 0) {
                switch(shdr.getSh_type()) {
                    case Elf32SectionHeader.SHT_PROGBITS:
                        {
                            if (!Memory.isAddressGood(memOffset)) {
                                log.warn(String.format("Section header (type 1) has invalid memory offset 0x%08X!", memOffset));
                            }
                            if (memOffset < module.loadAddressLow) {
                                log.warn(String.format("%s: section allocates more than program %08X - %08X", shdr.getSh_namez(), memOffset, (memOffset + len)));
                                module.loadAddressLow = memOffset;
                            }
                            if (memOffset + len > module.loadAddressHigh) {
                                log.warn(String.format("%s: section allocates more than program %08X - %08X", shdr.getSh_namez(), memOffset, (memOffset + len)));
                                module.loadAddressHigh = memOffset + len;
                            }
                            break;
                        }
                    case Elf32SectionHeader.SHT_NOBITS:
                        {
                            if (!Memory.isAddressGood(memOffset)) {
                                log.warn(String.format("Section header (type 8) has invalid memory offset 0x%08X!", memOffset));
                            }
                            if (len == 0) {
                                if (log.isDebugEnabled()) {
                                    log.debug(String.format("%s: ignoring zero-length type 8 section %08X", shdr.getSh_namez(), memOffset));
                                }
                            } else {
                                if (log.isDebugEnabled()) {
                                    log.debug(String.format("%s: clearing section %08X - %08X (len %08X)", shdr.getSh_namez(), memOffset, (memOffset + len), len));
                                }
                                if (!analyzeOnly) {
                                    mem.memset(memOffset, (byte) 0x0, len);
                                }
                                if (memOffset < module.loadAddressLow) {
                                    module.loadAddressLow = memOffset;
                                    if (log.isDebugEnabled()) {
                                        log.debug(String.format("%s: new loadAddressLow %08X (+%08X)", shdr.getSh_namez(), module.loadAddressLow, len));
                                    }
                                }
                                if (memOffset + len > module.loadAddressHigh) {
                                    module.loadAddressHigh = memOffset + len;
                                    if (log.isDebugEnabled()) {
                                        log.debug(String.format("%s: new loadAddressHigh %08X (+%08X)", shdr.getSh_namez(), module.loadAddressHigh, len));
                                    }
                                }
                            }
                            break;
                        }
                }
            }
        }
        Elf32SectionHeader shdr = elf.getSectionHeader(".text");
        if (shdr != null) {
            if (log.isTraceEnabled()) {
                log.trace(String.format("SH: Storing text size %08X %d", shdr.getSh_size(), shdr.getSh_size()));
            }
            module.text_addr = (int) (baseAddress + shdr.getSh_addr());
            module.text_size = (int) shdr.getSh_size();
        }
        shdr = elf.getSectionHeader(".data");
        if (shdr != null) {
            if (log.isTraceEnabled()) {
                log.trace(String.format("SH: Storing data size %08X %d", shdr.getSh_size(), shdr.getSh_size()));
            }
            module.data_size = (int) shdr.getSh_size();
        }
        shdr = elf.getSectionHeader(".bss");
        if (shdr != null && shdr.getSh_size() != 0) {
            if (log.isTraceEnabled()) {
                log.trace(String.format("SH: Storing bss size %08X %d", shdr.getSh_size(), shdr.getSh_size()));
            }
            if (module.bss_size == (int) shdr.getSh_size()) {
                if (log.isTraceEnabled()) {
                    log.trace("SH: Same bss size already set");
                }
            } else if (module.bss_size > (int) shdr.getSh_size()) {
                if (log.isTraceEnabled()) {
                    log.trace(String.format("SH: Larger bss size already set (%08X > %08X)", module.bss_size, shdr.getSh_size()));
                }
            } else if (module.bss_size != 0) {
                log.warn(String.format("SH: Overwriting bss size %08X with %08X", module.bss_size, shdr.getSh_size()));
                module.bss_size = (int) shdr.getSh_size();
            } else {
                log.info("SH: bss size not already set");
                module.bss_size = (int) shdr.getSh_size();
            }
        }
        module.nsegment += 1;
        module.segmentaddr[0] = module.loadAddressLow;
        module.segmentsize[0] = module.loadAddressHigh - module.loadAddressLow;
    }
}
