class BackupThread extends Thread {
    private int[] resizeInstructions(final int[] indexes, final int[] sizes, final int len) {
        byte[] b = code.data;
        int u, v, label;
        int i, j;
        int[] allIndexes = new int[len];
        int[] allSizes = new int[len];
        boolean[] resize;
        int newOffset;
        System.arraycopy(indexes, 0, allIndexes, 0, len);
        System.arraycopy(sizes, 0, allSizes, 0, len);
        resize = new boolean[code.length];
        int state = 3;
        do {
            if (state == 3) {
                state = 2;
            }
            u = 0;
            while (u < b.length) {
                int opcode = b[u] & 0xFF;
                int insert = 0;
                switch(ClassWriter.TYPE[opcode]) {
                    case ClassWriter.NOARG_INSN:
                    case ClassWriter.IMPLVAR_INSN:
                        u += 1;
                        break;
                    case ClassWriter.LABEL_INSN:
                        if (opcode > 201) {
                            opcode = opcode < 218 ? opcode - 49 : opcode - 20;
                            label = u + readUnsignedShort(b, u + 1);
                        } else {
                            label = u + readShort(b, u + 1);
                        }
                        newOffset = getNewOffset(allIndexes, allSizes, u, label);
                        if (newOffset < Short.MIN_VALUE || newOffset > Short.MAX_VALUE) {
                            if (!resize[u]) {
                                if (opcode == Opcodes.GOTO || opcode == Opcodes.JSR) {
                                    insert = 2;
                                } else {
                                    insert = 5;
                                }
                                resize[u] = true;
                            }
                        }
                        u += 3;
                        break;
                    case ClassWriter.LABELW_INSN:
                        u += 5;
                        break;
                    case ClassWriter.TABL_INSN:
                        if (state == 1) {
                            newOffset = getNewOffset(allIndexes, allSizes, 0, u);
                            insert = -(newOffset & 3);
                        } else if (!resize[u]) {
                            insert = u & 3;
                            resize[u] = true;
                        }
                        u = u + 4 - (u & 3);
                        u += 4 * (readInt(b, u + 8) - readInt(b, u + 4) + 1) + 12;
                        break;
                    case ClassWriter.LOOK_INSN:
                        if (state == 1) {
                            newOffset = getNewOffset(allIndexes, allSizes, 0, u);
                            insert = -(newOffset & 3);
                        } else if (!resize[u]) {
                            insert = u & 3;
                            resize[u] = true;
                        }
                        u = u + 4 - (u & 3);
                        u += 8 * readInt(b, u + 4) + 8;
                        break;
                    case ClassWriter.WIDE_INSN:
                        opcode = b[u + 1] & 0xFF;
                        if (opcode == Opcodes.IINC) {
                            u += 6;
                        } else {
                            u += 4;
                        }
                        break;
                    case ClassWriter.VAR_INSN:
                    case ClassWriter.SBYTE_INSN:
                    case ClassWriter.LDC_INSN:
                        u += 2;
                        break;
                    case ClassWriter.SHORT_INSN:
                    case ClassWriter.LDCW_INSN:
                    case ClassWriter.FIELDORMETH_INSN:
                    case ClassWriter.TYPE_INSN:
                    case ClassWriter.IINC_INSN:
                        u += 3;
                        break;
                    case ClassWriter.ITFMETH_INSN:
                        u += 5;
                        break;
                    default:
                        u += 4;
                        break;
                }
                if (insert != 0) {
                    int[] newIndexes = new int[allIndexes.length + 1];
                    int[] newSizes = new int[allSizes.length + 1];
                    System.arraycopy(allIndexes, 0, newIndexes, 0, allIndexes.length);
                    System.arraycopy(allSizes, 0, newSizes, 0, allSizes.length);
                    newIndexes[allIndexes.length] = u;
                    newSizes[allSizes.length] = insert;
                    allIndexes = newIndexes;
                    allSizes = newSizes;
                    if (insert > 0) {
                        state = 3;
                    }
                }
            }
            if (state < 3) {
                --state;
            }
        } while (state != 0);
        ByteVector newCode = new ByteVector(code.length);
        u = 0;
        while (u < code.length) {
            for (i = allIndexes.length - 1; i >= 0; --i) {
                if (allIndexes[i] == u) {
                    if (i < len) {
                        if (sizes[i] > 0) {
                            newCode.putByteArray(null, 0, sizes[i]);
                        } else {
                            newCode.length += sizes[i];
                        }
                        indexes[i] = newCode.length;
                    }
                }
            }
            int opcode = b[u] & 0xFF;
            switch(ClassWriter.TYPE[opcode]) {
                case ClassWriter.NOARG_INSN:
                case ClassWriter.IMPLVAR_INSN:
                    newCode.putByte(opcode);
                    u += 1;
                    break;
                case ClassWriter.LABEL_INSN:
                    if (opcode > 201) {
                        opcode = opcode < 218 ? opcode - 49 : opcode - 20;
                        label = u + readUnsignedShort(b, u + 1);
                    } else {
                        label = u + readShort(b, u + 1);
                    }
                    newOffset = getNewOffset(allIndexes, allSizes, u, label);
                    if (resize[u]) {
                        if (opcode == Opcodes.GOTO) {
                            newCode.putByte(200);
                        } else if (opcode == Opcodes.JSR) {
                            newCode.putByte(201);
                        } else {
                            newCode.putByte(opcode <= 166 ? ((opcode + 1) ^ 1) - 1 : opcode ^ 1);
                            newCode.putShort(8);
                            newCode.putByte(200);
                            newOffset -= 3;
                        }
                        newCode.putInt(newOffset);
                    } else {
                        newCode.putByte(opcode);
                        newCode.putShort(newOffset);
                    }
                    u += 3;
                    break;
                case ClassWriter.LABELW_INSN:
                    label = u + readInt(b, u + 1);
                    newOffset = getNewOffset(allIndexes, allSizes, u, label);
                    newCode.putByte(opcode);
                    newCode.putInt(newOffset);
                    u += 5;
                    break;
                case ClassWriter.TABL_INSN:
                    v = u;
                    u = u + 4 - (v & 3);
                    newCode.putByte(Opcodes.TABLESWITCH);
                    while (newCode.length % 4 != 0) {
                        newCode.putByte(0);
                    }
                    label = v + readInt(b, u);
                    u += 4;
                    newOffset = getNewOffset(allIndexes, allSizes, v, label);
                    newCode.putInt(newOffset);
                    j = readInt(b, u);
                    u += 4;
                    newCode.putInt(j);
                    j = readInt(b, u) - j + 1;
                    u += 4;
                    newCode.putInt(readInt(b, u - 4));
                    for (; j > 0; --j) {
                        label = v + readInt(b, u);
                        u += 4;
                        newOffset = getNewOffset(allIndexes, allSizes, v, label);
                        newCode.putInt(newOffset);
                    }
                    break;
                case ClassWriter.LOOK_INSN:
                    v = u;
                    u = u + 4 - (v & 3);
                    newCode.putByte(Opcodes.LOOKUPSWITCH);
                    while (newCode.length % 4 != 0) {
                        newCode.putByte(0);
                    }
                    label = v + readInt(b, u);
                    u += 4;
                    newOffset = getNewOffset(allIndexes, allSizes, v, label);
                    newCode.putInt(newOffset);
                    j = readInt(b, u);
                    u += 4;
                    newCode.putInt(j);
                    for (; j > 0; --j) {
                        newCode.putInt(readInt(b, u));
                        u += 4;
                        label = v + readInt(b, u);
                        u += 4;
                        newOffset = getNewOffset(allIndexes, allSizes, v, label);
                        newCode.putInt(newOffset);
                    }
                    break;
                case ClassWriter.WIDE_INSN:
                    opcode = b[u + 1] & 0xFF;
                    if (opcode == Opcodes.IINC) {
                        newCode.putByteArray(b, u, 6);
                        u += 6;
                    } else {
                        newCode.putByteArray(b, u, 4);
                        u += 4;
                    }
                    break;
                case ClassWriter.VAR_INSN:
                case ClassWriter.SBYTE_INSN:
                case ClassWriter.LDC_INSN:
                    newCode.putByteArray(b, u, 2);
                    u += 2;
                    break;
                case ClassWriter.SHORT_INSN:
                case ClassWriter.LDCW_INSN:
                case ClassWriter.FIELDORMETH_INSN:
                case ClassWriter.TYPE_INSN:
                case ClassWriter.IINC_INSN:
                    newCode.putByteArray(b, u, 3);
                    u += 3;
                    break;
                case ClassWriter.ITFMETH_INSN:
                    newCode.putByteArray(b, u, 5);
                    u += 5;
                    break;
                default:
                    newCode.putByteArray(b, u, 4);
                    u += 4;
                    break;
            }
        }
        Handler h = catchTable;
        while (h != null) {
            getNewOffset(allIndexes, allSizes, h.start);
            getNewOffset(allIndexes, allSizes, h.end);
            getNewOffset(allIndexes, allSizes, h.handler);
            h = h.next;
        }
        for (i = 0; i < 2; ++i) {
            ByteVector bv = i == 0 ? localVar : localVarType;
            if (bv != null) {
                b = bv.data;
                u = 0;
                while (u < bv.length) {
                    label = readUnsignedShort(b, u);
                    newOffset = getNewOffset(allIndexes, allSizes, 0, label);
                    writeShort(b, u, newOffset);
                    label += readUnsignedShort(b, u + 2);
                    newOffset = getNewOffset(allIndexes, allSizes, 0, label) - newOffset;
                    writeShort(b, u + 2, newOffset);
                    u += 10;
                }
            }
        }
        if (lineNumber != null) {
            b = lineNumber.data;
            u = 0;
            while (u < lineNumber.length) {
                writeShort(b, u, getNewOffset(allIndexes, allSizes, 0, readUnsignedShort(b, u)));
                u += 4;
            }
        }
        while (cattrs != null) {
            Label[] labels = cattrs.getLabels();
            if (labels != null) {
                for (i = labels.length - 1; i >= 0; --i) {
                    if (!labels[i].resized) {
                        labels[i].position = getNewOffset(allIndexes, allSizes, 0, labels[i].position);
                        labels[i].resized = true;
                    }
                }
            }
        }
        code = newCode;
        return indexes;
    }
}
