class BackupThread extends Thread {
    public void upload(String packagePath) throws UserException {
        if (!connected | !authenticated) {
            throw new UserException("The authentication was not initiated !");
        }
        if (openSelected == false) {
            selectCardManagement(cardName);
        }
        int cardResp;
        CapFiles file = null;
        try {
            file = new CapFiles(packagePath, max_size);
        } catch (Exception e) {
            throw new UserException(e.getMessage());
        }
        log.write("File " + packagePath + " read.");
        byte[] loadFileAID = file.getAIDHeader();
        log.write("PackageAID:" + Apdu.ba2s(loadFileAID));
        byte[] securityDomainAID = cardManagerAID;
        byte[] apdu2 = ApplicationOnCard.installForLoad(loadFileAID, securityDomainAID, null, null, null);
        byte[] apduResp2 = trySend(apdu2);
        cardResp = GP_responses.get_GP_response(apduResp2);
        if (cardResp != GP_responses.SUCCESS) {
            throw new UserException("installForLoad:::" + GP_responses.getErrMsg(cardResp), cardResp);
        }
        log.write("InstallForLoad sent:(" + Apdu.ba2s(apdu2) + ")");
        log.write("Applets loading...");
        Vector data = new Vector();
        data = file.get();
        int sentBlocks = 0;
        int lastBlock = data.size() - 1;
        log.write("Block max_size :" + max_size);
        log.write("Number of blocks :" + data.size());
        byte[] block1 = (byte[]) data.elementAt(0);
        byte[] apdu1 = new byte[block1.length + 4];
        apdu1[0] = (byte) 0xC4;
        apdu1[1] = (byte) 0x82;
        apdu1[2] = (byte) 0x01;
        apdu1[3] = (byte) 0x2C;
        int offset = 4;
        System.arraycopy(block1, 0, apdu1, offset, block1.length);
        byte[] apdu1_;
        if (sentBlocks == lastBlock) {
            apdu1_ = ApplicationOnCard.load((byte) 0x80, (byte) 0x00, apdu1.length, apdu1);
        } else {
            apdu1_ = ApplicationOnCard.load((byte) 0x00, (byte) 0x00, apdu1.length, apdu1);
        }
        byte[] respAPDU = trySend(apdu1_);
        log.write("Loading...Block 0 sent...");
        cardResp = GP_responses.get_GP_response(respAPDU);
        if (cardResp != GP_responses.SUCCESS) {
            throw new UserException("Load:::" + GP_responses.getErrMsg(cardResp), cardResp);
        }
        int i;
        for (i = 1; i < lastBlock; i++) {
            byte[] block_i = (byte[]) data.elementAt(i);
            byte[] apdu_i = ApplicationOnCard.load((byte) 0x00, (byte) i, block_i.length, block_i);
            respAPDU = trySend(apdu_i);
            log.write("Loading...Block " + i + " sent...");
            cardResp = GP_responses.get_GP_response(respAPDU);
            if (cardResp != GP_responses.SUCCESS) {
                throw new UserException("Load:::" + GP_responses.getErrMsg(cardResp), cardResp);
            }
        }
        if (lastBlock != 0) {
            byte[] last = (byte[]) data.elementAt(lastBlock);
            byte[] lastAPDU = ApplicationOnCard.load((byte) 0x80, (byte) lastBlock, last.length, last);
            respAPDU = trySend(lastAPDU);
            log.write("loading...Block " + i + " sent...");
            cardResp = GP_responses.get_GP_response(respAPDU);
            if (cardResp != GP_responses.SUCCESS) {
                throw new UserException("Load:::" + GP_responses.getErrMsg(cardResp), cardResp);
            }
        }
    }
}
