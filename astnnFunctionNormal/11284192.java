class BackupThread extends Thread {
    public void copyImage(File srcImage, String attribs) {
        Integer scale = new ImageTag().getScale(attribs);
        if (srcImage.exists()) {
            File destinationFile = new File(this.imageDestinationPath, FilenameUtils.getName(srcImage.getPath()));
            if (!destinationFile.exists()) {
                try {
                    FileUtils.copyFileToDirectory(srcImage, this.imageDestinationPath);
                    LOG.info("copying image: " + srcImage.getPath());
                    ImageOutputStream stream = new FileImageOutputStream(destinationFile);
                    if (scale == null && Utilities.getImageWidth(srcImage) > PAGE_WIDTH) {
                        scale = 1;
                    }
                    if (scale != null) {
                        Utilities.resizeImage(srcImage, stream, Utilities.getFormatName(srcImage), PAGE_WIDTH, (double) scale / 100);
                        srcImage = destinationFile;
                    }
                    Utilities.getImageWithLogo(srcImage, stream, Utilities.getFormatName(srcImage), logo);
                    stream.close();
                } catch (IOException e) {
                    LOG.warn("Error while copying " + srcImage.getPath() + ":\n" + "\t\t" + e.getMessage());
                    throw new TubainaException("Couldn't copy image", e);
                }
            } else {
                LOG.warn("Error while copying '" + srcImage.getPath() + "':\n" + "\t\tDestination image '" + destinationFile.getPath() + "' already exists");
            }
        } else {
            LOG.warn("Image: '" + srcImage.getPath() + "' doesn't exist");
            throw new TubainaException("Image Doesn't Exists");
        }
    }
}
