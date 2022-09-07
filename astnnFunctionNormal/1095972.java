class BackupThread extends Thread {
    private void writePsf(Memory mem, String path, String name, PspUtilitySavedataSFOParam sfoParam) throws IOException {
        SeekableRandomFile fileOutput = getDataOutput(path, name);
        if (fileOutput == null) {
            return;
        }
        PSF psf = new PSF();
        psf.put("PARENTAL_LEVEL", sfoParam.parentalLevel);
        psf.put("TITLE", sfoParam.title, 128);
        psf.put("SAVEDATA_DETAIL", sfoParam.detail, 1024);
        psf.put("SAVEDATA_TITLE", sfoParam.savedataTitle, 128);
        psf.write(fileOutput.getChannel().map(MapMode.READ_WRITE, 0, psf.size()));
    }
}
