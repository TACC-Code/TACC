class BackupThread extends Thread {
    public void TestMemory(Memory mem, String comment) {
        startTest(comment);
        if (mem == null) {
            fail("Memory could not be created");
            return;
        }
        {
            if (mem.allocate()) {
                success("allocate");
            } else {
                fail("allocate");
                return;
            }
        }
        {
            boolean success = true;
            int address = MemoryMap.START_RAM;
            for (int i = 0; i < 100; i++) {
                mem.write8(address + i, (byte) i);
            }
            for (int i = 0; i < 100; i++) {
                int b = mem.read8(address + i);
                if (b != i) {
                    success = fail("write8/read8 " + i);
                }
            }
            if (success) {
                success("write8/read8");
            }
        }
        {
            boolean success = true;
            int address = MemoryMap.START_RAM;
            for (int i = 0; i < 10000; i += 2) {
                mem.write16(address + i, (short) i);
            }
            for (int i = 0; i < 10000; i += 2) {
                int b = mem.read16(address + i);
                if (b != i) {
                    success = fail("write16/read16 " + i);
                }
            }
            if (success) {
                success("write16/read16");
            }
        }
        {
            boolean success = true;
            int address = MemoryMap.START_RAM;
            for (int i = 0; i < 100000; i += 4) {
                mem.write32(address + i, i);
            }
            for (int i = 0; i < 100000; i += 4) {
                int b = mem.read32(address + i);
                if (b != i) {
                    success = fail("write32/read32 " + i);
                }
            }
            if (success) {
                success("write32/read32");
            }
        }
        {
            boolean success = true;
            int address = MemoryMap.START_RAM + 1;
            int data = 0x89;
            int limitData = 0x12;
            int length = 100;
            mem.write8(address - 1, (byte) limitData);
            mem.write8(address + length, (byte) limitData);
            mem.memset(address, (byte) data, length);
            for (int i = 0; i < length; i++) {
                int b = mem.read8(address + i);
                if (b != data) {
                    success = fail("memset " + i);
                }
            }
            if (mem.read8(address - 1) != limitData) {
                success = fail("memset lower limit");
            }
            if (mem.read8(address + length) != limitData) {
                success = fail("memset upper limit");
            }
            if (success) {
                success("memset");
            }
        }
        {
            boolean success = true;
            if (Memory.isAddressGood(MemoryMap.START_RAM - 1)) {
                success = fail("isAddressGood START_RAM - 1");
            }
            if (!Memory.isAddressGood(MemoryMap.START_RAM)) {
                success = fail("isAddressGood START_RAM");
            }
            if (Memory.isAddressGood(0)) {
                success = fail("isAddressGood 0");
            }
            if (success) {
                success("isAddressGood");
            }
        }
        {
            boolean success = true;
            byte[] bytes = new byte[] { 0x12, 0x23, 0x34, 0x45, 0x56, 0x67, 0x78, (byte) 0x89, (byte) 0x9a };
            ByteBuffer buffer = ByteBuffer.wrap(bytes);
            int length = bytes.length;
            int address = MemoryMap.START_RAM + 1;
            int limitData = 0x11;
            mem.write8(address - 1, (byte) limitData);
            mem.write8(address + length, (byte) limitData);
            mem.copyToMemory(address, buffer, length);
            for (int i = 0; i < length; i++) {
                int b = mem.read8(address + i);
                if (b != (bytes[i] & 0xFF)) {
                    success = fail("copyToMemory " + i);
                }
            }
            if (mem.read8(address - 1) != limitData) {
                success = fail("copyToMemory lower limit");
            }
            if (mem.read8(address + length) != limitData) {
                success = fail("copyToMemory upper limit");
            }
            if (success) {
                success("copyToMemory");
            }
        }
        {
            boolean success = true;
            byte[] bytes = new byte[] { 0x12, 0x23, 0x34, 0x45, 0x56, 0x67, 0x78, (byte) 0x89, (byte) 0x9a, (byte) 0xab, (byte) 0xbc, (byte) 0xcd };
            ByteBuffer referenceByteBuffer = ByteBuffer.wrap(bytes);
            int length = bytes.length;
            int address = MemoryMap.START_RAM + 4;
            mem.copyToMemory(address, referenceByteBuffer, length);
            referenceByteBuffer.rewind();
            int[] ints = new int[] { 0x45342312, 0x89786756, 0xcdbcab9a };
            IntBuffer referenceIntBuffer = IntBuffer.wrap(ints);
            Buffer buffer = mem.getBuffer(address, length);
            if ((buffer.capacity() * Utilities.bufferElementSize(buffer)) != length) {
                success = fail("getBuffer capacity");
            }
            if (buffer instanceof ByteBuffer) {
                if (!referenceByteBuffer.equals(buffer)) {
                    success = fail("getBuffer Byte content");
                }
            } else if (buffer instanceof IntBuffer) {
                if (!referenceIntBuffer.equals(buffer)) {
                    success = fail("getBuffer Int content");
                }
            } else {
                success = fail("getBuffer unknown content");
            }
            if (success) {
                success("getBuffer");
            }
        }
        {
            boolean success = true;
            mem.write32(0, 0x12345678);
            if (mem.read32(0) != 0) {
                success = fail("Invalid memory could be accessed");
            }
            mem.write32(0x8f800020, 0x12345678);
            if (mem.read32(0x8f800020) != 0) {
                success = fail("Invalid memory could be accessed");
            }
            if (success) {
                success("Check of Invalid memory access");
            }
        }
        endTest();
    }
}
