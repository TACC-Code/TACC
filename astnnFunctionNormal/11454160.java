class BackupThread extends Thread {
    public void write(Tag tag, RandomAccessFile raf, RandomAccessFile rafTemp) throws CannotWriteException, IOException {
        logger.info("Writing tag");
        metadataBlockPadding.clear();
        metadataBlockApplication.clear();
        metadataBlockSeekTable.clear();
        metadataBlockCueSheet.clear();
        byte[] b = new byte[FlacStream.FLAC_STREAM_IDENTIFIER_LENGTH];
        raf.readFully(b);
        String flac = new String(b);
        if (!flac.equals(FlacStream.FLAC_STREAM_IDENTIFIER)) {
            throw new CannotWriteException("This is not a FLAC file");
        }
        boolean isLastBlock = false;
        while (!isLastBlock) {
            MetadataBlockHeader mbh = MetadataBlockHeader.readHeader(raf);
            switch(mbh.getBlockType()) {
                case VORBIS_COMMENT:
                case PADDING:
                case PICTURE:
                    {
                        raf.seek(raf.getFilePointer() + mbh.getDataLength());
                        MetadataBlockData mbd = new MetadataBlockDataPadding(mbh.getDataLength());
                        metadataBlockPadding.add(new MetadataBlock(mbh, mbd));
                        break;
                    }
                case APPLICATION:
                    {
                        MetadataBlockData mbd = new MetadataBlockDataApplication(mbh, raf);
                        metadataBlockApplication.add(new MetadataBlock(mbh, mbd));
                        break;
                    }
                case SEEKTABLE:
                    {
                        MetadataBlockData mbd = new MetadataBlockDataSeekTable(mbh, raf);
                        metadataBlockSeekTable.add(new MetadataBlock(mbh, mbd));
                        break;
                    }
                case CUESHEET:
                    {
                        MetadataBlockData mbd = new MetadataBlockDataCueSheet(mbh, raf);
                        metadataBlockCueSheet.add(new MetadataBlock(mbh, mbd));
                        break;
                    }
                default:
                    {
                        raf.seek(raf.getFilePointer() + mbh.getDataLength());
                        break;
                    }
            }
            isLastBlock = mbh.isLastBlock();
        }
        int availableRoom = computeAvailableRoom();
        int newTagSize = tc.convert(tag).limit();
        int neededRoom = newTagSize + computeNeededRoom();
        raf.seek(0);
        logger.info("Writing tag available bytes:" + availableRoom + ":needed bytes:" + neededRoom);
        if ((availableRoom == neededRoom) || (availableRoom > neededRoom + MetadataBlockHeader.HEADER_LENGTH)) {
            raf.seek(FlacStream.FLAC_STREAM_IDENTIFIER_LENGTH + MetadataBlockHeader.HEADER_LENGTH + MetadataBlockDataStreamInfo.STREAM_INFO_DATA_LENGTH);
            for (int i = 0; i < metadataBlockApplication.size(); i++) {
                raf.write(metadataBlockApplication.get(i).getHeader().getBytesWithoutIsLastBlockFlag());
                raf.write(metadataBlockApplication.get(i).getData().getBytes());
            }
            for (int i = 0; i < metadataBlockSeekTable.size(); i++) {
                raf.write(metadataBlockSeekTable.get(i).getHeader().getBytesWithoutIsLastBlockFlag());
                raf.write(metadataBlockSeekTable.get(i).getData().getBytes());
            }
            for (int i = 0; i < metadataBlockCueSheet.size(); i++) {
                raf.write(metadataBlockCueSheet.get(i).getHeader().getBytesWithoutIsLastBlockFlag());
                raf.write(metadataBlockCueSheet.get(i).getData().getBytes());
            }
            raf.getChannel().write(tc.convert(tag, availableRoom - neededRoom));
        } else {
            FileChannel fc = raf.getChannel();
            b = new byte[FlacStream.FLAC_STREAM_IDENTIFIER_LENGTH + MetadataBlockHeader.HEADER_LENGTH + MetadataBlockDataStreamInfo.STREAM_INFO_DATA_LENGTH];
            raf.readFully(b);
            raf.seek(availableRoom + FlacStream.FLAC_STREAM_IDENTIFIER_LENGTH + MetadataBlockHeader.HEADER_LENGTH + MetadataBlockDataStreamInfo.STREAM_INFO_DATA_LENGTH);
            FileChannel tempFC = rafTemp.getChannel();
            rafTemp.write(b);
            for (int i = 0; i < metadataBlockApplication.size(); i++) {
                rafTemp.write(metadataBlockApplication.get(i).getHeader().getBytesWithoutIsLastBlockFlag());
                rafTemp.write(metadataBlockApplication.get(i).getData().getBytes());
            }
            for (int i = 0; i < metadataBlockSeekTable.size(); i++) {
                rafTemp.write(metadataBlockSeekTable.get(i).getHeader().getBytesWithoutIsLastBlockFlag());
                rafTemp.write(metadataBlockSeekTable.get(i).getData().getBytes());
            }
            for (int i = 0; i < metadataBlockCueSheet.size(); i++) {
                rafTemp.write(metadataBlockCueSheet.get(i).getHeader().getBytesWithoutIsLastBlockFlag());
                rafTemp.write(metadataBlockCueSheet.get(i).getData().getBytes());
            }
            rafTemp.write(tc.convert(tag, FlacTagCreator.DEFAULT_PADDING).array());
            tempFC.transferFrom(fc, tempFC.position(), fc.size());
        }
    }
}
