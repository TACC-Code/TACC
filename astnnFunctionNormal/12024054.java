class BackupThread extends Thread {
    public DefaultTreeModel buildTree(RandomAccessFile raf, boolean closeExit) throws IOException, CannotReadException {
        FileChannel fc = null;
        try {
            fc = raf.getChannel();
            fc.position(0);
            rootNode = new DefaultMutableTreeNode();
            dataTree = new DefaultTreeModel(rootNode);
            ByteBuffer headerBuffer = ByteBuffer.allocate(Mp4BoxHeader.HEADER_LENGTH);
            while (fc.position() < fc.size()) {
                Mp4BoxHeader boxHeader = new Mp4BoxHeader();
                headerBuffer.clear();
                fc.read(headerBuffer);
                headerBuffer.rewind();
                try {
                    boxHeader.update(headerBuffer);
                } catch (NullBoxIdException ne) {
                    if (moovNode != null & mdatNode != null) {
                        NullPadding np = new NullPadding(fc.position() - Mp4BoxHeader.HEADER_LENGTH, fc.size());
                        DefaultMutableTreeNode trailingPaddingNode = new DefaultMutableTreeNode(np);
                        rootNode.add(trailingPaddingNode);
                        logger.warning(ErrorMessage.NULL_PADDING_FOUND_AT_END_OF_MP4.getMsg(np.getFilePos()));
                        break;
                    } else {
                        throw ne;
                    }
                }
                boxHeader.setFilePos(fc.position() - Mp4BoxHeader.HEADER_LENGTH);
                DefaultMutableTreeNode newAtom = new DefaultMutableTreeNode(boxHeader);
                if (boxHeader.getId().equals(Mp4NotMetaFieldKey.MOOV.getFieldName())) {
                    moovNode = newAtom;
                    moovHeader = boxHeader;
                    long filePosStart = fc.position();
                    moovBuffer = ByteBuffer.allocate(boxHeader.getDataLength());
                    fc.read(moovBuffer);
                    moovBuffer.rewind();
                    buildChildrenOfNode(moovBuffer, newAtom);
                    fc.position(filePosStart);
                } else if (boxHeader.getId().equals(Mp4NotMetaFieldKey.FREE.getFieldName())) {
                    freeNodes.add(newAtom);
                } else if (boxHeader.getId().equals(Mp4NotMetaFieldKey.MDAT.getFieldName())) {
                    mdatNode = newAtom;
                    mdatNodes.add(newAtom);
                }
                rootNode.add(newAtom);
                fc.position(fc.position() + boxHeader.getDataLength());
            }
            return dataTree;
        } finally {
            if (mdatNode == null) {
                throw new CannotReadException(ErrorMessage.MP4_CANNOT_FIND_AUDIO.getMsg());
            }
            if (closeExit) {
                fc.close();
            }
        }
    }
}
