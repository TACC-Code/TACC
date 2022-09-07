class BackupThread extends Thread {
    protected void doCStore(ActiveAssociation activeAssoc, Dimse rq, Command rspCmd) throws IOException, DcmServiceException {
        Command rqCmd = rq.getCommand();
        InputStream in = rq.getDataAsStream();
        Association assoc = activeAssoc.getAssociation();
        File file = null;
        try {
            DcmDecodeParam decParam = DcmDecodeParam.valueOf(rq.getTransferSyntaxUID());
            Dataset ds = objFact.newDataset();
            DcmParser parser = pf.newDcmParser(in);
            parser.setDcmHandler(ds.getDcmHandler());
            parser.parseDataset(decParam, Tags.PixelData);
            service.logDataset("Dataset:\n", ds);
            ds.setFileMetaInfo(objFact.newFileMetaInfo(rqCmd.getAffectedSOPClassUID(), rqCmd.getAffectedSOPInstanceUID(), rq.getTransferSyntaxUID()));
            checkDataset(ds);
            Calendar today = Calendar.getInstance();
            File dir = storageDirFiles[today.get(Calendar.DAY_OF_MONTH) % storageDirFiles.length];
            file = makeFile(dir, today, ds);
            MessageDigest md = MessageDigest.getInstance("MD5");
            storeToFile(parser, ds, file, (DcmEncodeParam) decParam, md);
            final String dirPath = dir.getCanonicalPath().replace(File.separatorChar, '/');
            final String filePath = file.getCanonicalPath().replace(File.separatorChar, '/').substring(dirPath.length() + 1);
            Dataset coercedElements = updateDB(assoc, ds, dirPath, filePath, (int) file.length(), md.digest());
            if (coercedElements.isEmpty() || warningAsSuccessSet.contains(assoc.getCallingAET())) {
                rspCmd.putUS(Tags.Status, Status.Success);
            } else {
                int[] coercedTags = new int[coercedElements.size()];
                Iterator it = coercedElements.iterator();
                for (int i = 0; i < coercedTags.length; i++) {
                    coercedTags[i] = ((DcmElement) it.next()).tag();
                }
                rspCmd.putAT(Tags.OffendingElement, coercedTags);
                rspCmd.putUS(Tags.Status, Status.CoercionOfDataElements);
                ds.putAll(coercedElements);
            }
            updateStoredStudiesInfo(assoc, ds);
        } catch (DcmServiceException e) {
            service.getLog().warn(e.getMessage(), e);
            deleteFailedStorage(file);
            throw e;
        } catch (Exception e) {
            service.getLog().error(e.getMessage(), e);
            deleteFailedStorage(file);
            throw new DcmServiceException(Status.ProcessingFailure, e);
        } finally {
            in.close();
        }
    }
}
