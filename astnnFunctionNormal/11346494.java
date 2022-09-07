class BackupThread extends Thread {
    private void readHeader(ImageInputStream iis) throws IOException {
        int L = (int) iis.readBits(8);
        int S = (int) iis.readBits(8);
        if (L != 76 || S != 83) {
            System.out.println("L: " + L + " S:" + S);
            System.out.println("TIPO FILE NON SUPPORTATO");
            throw new IOException("Tipo file non supportato");
        }
        codifica = (int) iis.readBits(8);
        colorSpace = ColorSpaceManager.getColorSpace(codifica);
        imgH = (int) iis.readBits(16);
        imgW = (int) iis.readBits(16);
        channelsNumber = colorSpace.getChannelsNumber();
        qMatrices = new ArrayList<float[][]>(channelsNumber);
        for (int i = 0; i < channelsNumber; i++) {
            qMatrices.add(LSDequantizer.readMatrix(iis));
        }
        minDcCodeLen = new int[channelsNumber];
        chdDCTables = new int[channelsNumber][][];
        for (int i = 0; i < channelsNumber; i++) {
            minDcCodeLen[i] = (int) iis.readBits(8);
            chdDCTables[i] = CHDEntry.readCHDDCTable(iis);
        }
        minAcCodeLen = new int[channelsNumber];
        chdACTables = new int[channelsNumber][][];
        for (int i = 0; i < channelsNumber; i++) {
            minAcCodeLen[i] = (int) iis.readBits(8);
            chdACTables[i] = CHDEntry.readCHDACTable(iis);
        }
        System.out.println("iis pos : " + iis.getStreamPosition() + " offset : " + iis.getBitOffset());
    }
}
