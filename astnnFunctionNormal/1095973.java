class BackupThread extends Thread {
    private void writeEncryptedPsf(Memory mem, String path, String psfName, PspUtilitySavedataSFOParam sfoParam, String dataName, int dataLength, byte[] key, int mode) throws IOException {
        SeekableRandomFile psfOutput = getDataOutput(path, psfName);
        SeekableRandomFile dataOutput = getDataOutput(path, dataName);
        if ((psfOutput == null) || (dataOutput == null)) {
            return;
        }
        CryptoEngine crypto = new CryptoEngine();
        byte[] dataBuffer = new byte[dataLength];
        dataOutput.readFully(dataBuffer);
        PSF psf = new PSF();
        psf.put("CATEGORY", "MS", 4);
        psf.put("PARENTAL_LEVEL", sfoParam.parentalLevel);
        psf.put("SAVEDATA_DETAIL", sfoParam.detail, 1024);
        psf.put("SAVEDATA_DIRECTORY", gameName + saveName, 64);
        crypto.UpdateSavedataHashes(psf, dataBuffer, dataLength, key, dataName, mode);
        psf.put("SAVEDATA_TITLE", sfoParam.savedataTitle, 128);
        psf.put("TITLE", sfoParam.title, 128);
        psf.write(psfOutput.getChannel().map(MapMode.READ_WRITE, 0, psf.size()));
    }
}
