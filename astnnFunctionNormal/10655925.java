class BackupThread extends Thread {
    private IGImage createImage(IGFolder igFolder, URL urlImage, int order, ServiceContext serviceContext) throws FileNotFoundException, IOException, SystemException, MalformedURLException {
        _log.info(utilI18N.getMessage("info.creatingimage", locale) + ": " + urlImage.toString());
        String nameFile = FilenameUtils.getName(urlImage.getFile());
        String extImage = FilenameUtils.getExtension(nameFile).toLowerCase();
        StringBuilder nameImage = new StringBuilder();
        if (!nameFile.contains("?") && (!"".equals(extImage))) {
            try {
                nameImage.append(igFolder.getName());
                nameImage.append("_img");
                nameImage.append(order);
                nameImage.append(".");
                nameImage.append(extImage);
                IGImage igImage = IGImageServiceUtil.getImageByFolderIdAndNameWithExtension(igFolder.getFolderId(), nameImage.toString());
                return igImage;
            } catch (NoSuchImageException e) {
            } catch (PortalException e) {
            }
        }
        InputStream urlStream = urlImage.openStream();
        String extension = "";
        if (nameFile.contains("?") || ("".equals(extImage))) {
            InputStream urlStreamExt = urlImage.openStream();
            extension = getFormatName(urlStreamExt);
        } else {
            extension = extImage;
        }
        extension = extension.toLowerCase();
        if ("jpeg".equals(extension)) {
            extension = "jpg";
        }
        BufferedImage image = ImageIO.read(urlStream);
        nameImage = new StringBuilder();
        nameImage.append(igFolder.getName()).append("_img").append(order).append(".").append(extension);
        File tempFile = new File("temp/temp." + extension);
        ImageIO.write(image, extension, tempFile);
        IGImage igImage = null;
        try {
            igImage = IGImageServiceUtil.addImage(igFolder.getFolderId(), nameImage.toString(), nameImage.toString(), tempFile, extension, serviceContext);
        } catch (DuplicateImageNameException e) {
            try {
                igImage = IGImageServiceUtil.getImageByFolderIdAndNameWithExtension(igFolder.getFolderId(), nameImage.toString());
            } catch (PortalException e1) {
                throw new SystemException(e1.getMessage());
            }
        } catch (PortalException e) {
            throw new SystemException(e.getMessage());
        }
        return igImage;
    }
}
