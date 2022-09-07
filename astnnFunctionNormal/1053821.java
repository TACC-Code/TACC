class BackupThread extends Thread {
    private void setupPipe(String digestString) {
        propagatedPipeAdv = (PipeAdvertisement) AdvertisementFactory.newAdvertisement(PipeAdvertisement.getAdvertisementType());
        try {
            byte[] bid = MessageDigest.getInstance("MD5").digest(digestString.getBytes("ISO-8859-1"));
            PipeID pipeID = IDFactory.newPipeID(peerGroup.getPeerGroupID(), bid);
            propagatedPipeAdv.setPipeID(pipeID);
            propagatedPipeAdv.setType(PipeService.PropagateType);
            propagatedPipeAdv.setName("A chattering propagate pipe");
            propagatedPipeAdv.setDescription("verbose description");
            PipeService pipeService = peerGroup.getPipeService();
            inputPipe = pipeService.createInputPipe(propagatedPipeAdv, this);
            System.out.println("Propagate pipes and listeners created");
            System.out.println("Propagate PipeID: " + pipeID.toString());
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
