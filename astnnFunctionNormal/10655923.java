class BackupThread extends Thread {
    private String replaceHTMLToWebContent(URL urlWebContent, int idParentDirectory, ServiceContext serviceContext) throws IOException, SystemException {
        StringBuilder sbReplaceImg = null;
        String nameWebContent = FilenameUtils.getName(urlWebContent.getFile());
        String urlPath = urlWebContent.toString();
        String urlWebContents = urlPath.replace(nameWebContent, "");
        URLConnection urlCon = urlWebContent.openConnection();
        InputStream isWebContent = urlCon.getInputStream();
        Source source = new Source(isWebContent);
        OutputDocument outputDocument = new OutputDocument(source);
        List<StartTag> imgStartTags = source.getAllStartTags(HTMLElementName.IMG);
        if ((imgStartTags != null) && (imgStartTags.size() > 0)) {
            IGFolder igFolder = null;
            try {
                igFolder = createOrGetFolder(idParentDirectory, nameWebContent.replaceFirst("." + FilenameUtils.getExtension(nameWebContent), ""), serviceContext);
            } catch (PortalException e) {
                if (e instanceof NoSuchFolderException) {
                    throw new SystemException(utilI18N.getMessage("validate.wrongidparentdir", locale));
                } else {
                    throw new SystemException(utilI18N.getMessage("error.wrongwebcontent", locale));
                }
            }
            int ite = 0;
            for (Iterator<StartTag> i = imgStartTags.iterator(); i.hasNext(); ) {
                ite++;
                StartTag startTag = i.next();
                Attributes attributes = startTag.getAttributes();
                sbReplaceImg = new StringBuilder();
                sbReplaceImg.append("<img ");
                URL urlImage = null;
                for (Iterator<Attribute> j = attributes.iterator(); j.hasNext(); ) {
                    Attribute attr = j.next();
                    if (attr != null) {
                        if ("src".equalsIgnoreCase(attr.getName())) {
                            urlImage = null;
                            String pathImage = attr.getValue();
                            if (pathImage.startsWith("http")) {
                                urlImage = new URL(pathImage);
                            } else {
                                urlImage = new URL(urlWebContents + attr.getValue());
                            }
                            IGImage igImage = null;
                            try {
                                igImage = createImage(igFolder, urlImage, ite, serviceContext);
                            } catch (FileNotFoundException e) {
                                warns.append(utilI18N.getMessage("warn.filenotfound", locale) + ": " + urlImage.toString());
                                ite--;
                                continue;
                            } catch (MalformedURLException e) {
                                warns.append(utilI18N.getMessage("warn.urlimagemalformed", locale) + ": " + urlImage.toString());
                                ite--;
                                continue;
                            }
                            sbReplaceImg.append(" ").append("src=\"/image/image_gallery?uuid=").append(igImage.getUuid()).append("&groupId=").append(igImage.getGroupId()).append("\"");
                        } else if ("style".equalsIgnoreCase(attr.getName())) {
                            sbReplaceImg.append("");
                        } else {
                            sbReplaceImg.append(" ").append(attr);
                        }
                    }
                }
                sbReplaceImg.append(">\n").append("\n</img>");
                outputDocument.replace(startTag, sbReplaceImg.toString());
                nImages++;
            }
        }
        return outputDocument.toString();
    }
}
