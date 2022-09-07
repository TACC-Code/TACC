class BackupThread extends Thread {
    public void write(Tag tag, RandomAccessFile raf, RandomAccessFile rafTemp) throws CannotWriteException, IOException {
        metadataBlockPadding.removeAllElements();
        metadataBlockApplication.removeAllElements();
        metadataBlockSeekTable.removeAllElements();
        metadataBlockCueSheet.removeAllElements();
        byte[] b = new byte[4];
        raf.readFully(b);
        String flac = new String(b);
        if (!flac.equals("fLaC")) throw new CannotWriteException("This is not a FLAC file");
        boolean isLastBlock = false;
        while (!isLastBlock) {
            b = new byte[4];
            raf.readFully(b);
            MetadataBlockHeader mbh = new MetadataBlockHeader(b);
            switch(mbh.getBlockType()) {
                case MetadataBlockHeader.VORBIS_COMMENT:
                    handlePadding(mbh, raf);
                    break;
                case MetadataBlockHeader.PADDING:
                    handlePadding(mbh, raf);
                    break;
                case MetadataBlockHeader.APPLICATION:
                    handleApplication(mbh, raf);
                    break;
                case MetadataBlockHeader.SEEKTABLE:
                    handleSeekTable(mbh, raf);
                    break;
                case MetadataBlockHeader.CUESHEET:
                    handleCueSheet(mbh, raf);
                    break;
                default:
                    skipBlock(mbh, raf);
                    break;
            }
            isLastBlock = mbh.isLastBlock();
        }
        int availableRoom = computeAvailableRoom();
        int newTagSize = tc.getTagLength(tag);
        int neededRoom = newTagSize + computeNeededRoom();
        raf.seek(0);
        if (availableRoom >= neededRoom) {
            raf.seek(42);
            for (int i = 0; i < metadataBlockApplication.size(); i++) {
                raf.write(((MetadataBlock) metadataBlockApplication.elementAt(i)).getHeader().getBytes());
                raf.write(((MetadataBlock) metadataBlockApplication.elementAt(i)).getData().getBytes());
            }
            for (int i = 0; i < metadataBlockSeekTable.size(); i++) {
                raf.write(((MetadataBlock) metadataBlockSeekTable.elementAt(i)).getHeader().getBytes());
                raf.write(((MetadataBlock) metadataBlockSeekTable.elementAt(i)).getData().getBytes());
            }
            for (int i = 0; i < metadataBlockCueSheet.size(); i++) {
                raf.write(((MetadataBlock) metadataBlockCueSheet.elementAt(i)).getHeader().getBytes());
                raf.write(((MetadataBlock) metadataBlockCueSheet.elementAt(i)).getData().getBytes());
            }
            raf.getChannel().write(tc.convert(tag, availableRoom - neededRoom));
        } else {
            FileChannel fc = raf.getChannel();
            b = new byte[42];
            raf.readFully(b);
            raf.seek(availableRoom + 42);
            FileChannel tempFC = rafTemp.getChannel();
            rafTemp.write(b);
            for (int i = 0; i < metadataBlockApplication.size(); i++) {
                rafTemp.write(((MetadataBlock) metadataBlockApplication.elementAt(i)).getHeader().getBytes());
                rafTemp.write(((MetadataBlock) metadataBlockApplication.elementAt(i)).getData().getBytes());
            }
            for (int i = 0; i < metadataBlockSeekTable.size(); i++) {
                rafTemp.write(((MetadataBlock) metadataBlockSeekTable.elementAt(i)).getHeader().getBytes());
                rafTemp.write(((MetadataBlock) metadataBlockSeekTable.elementAt(i)).getData().getBytes());
            }
            for (int i = 0; i < metadataBlockCueSheet.size(); i++) {
                rafTemp.write(((MetadataBlock) metadataBlockCueSheet.elementAt(i)).getHeader().getBytes());
                rafTemp.write(((MetadataBlock) metadataBlockCueSheet.elementAt(i)).getData().getBytes());
            }
            rafTemp.write(tc.convert(tag, FlacTagCreator.DEFAULT_PADDING).array());
            tempFC.transferFrom(fc, tempFC.position(), fc.size());
        }
    }
}
