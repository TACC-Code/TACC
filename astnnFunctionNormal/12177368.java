class BackupThread extends Thread {
    @Override
    public boolean serverProcessInterfaceTransaction(final IProcessorDefinition processor, final ITransactionModel transaction, final IChainStore chain, final ServiceCall call) throws Exception {
        IControllerMethod method = new IControllerMethod() {

            public Object execute() throws Exception {
                FormModel claimsForm = BaseData.getClinical().getForm(((InterfaceTransactionModel) transaction).getForeignKeyId(), chain, call);
                List<Long> formTypeRefIds = BaseData.getClinical().getFormTypeRefIdsByFormGroupRefId(FormGroupReference.ACTIVELIST.getRefId(), call);
                List<FormModel> forms = new ArrayList<FormModel>();
                for (Long formTypeRefId : formTypeRefIds) {
                    FormModel form = BaseData.getClinical().getActiveListForm(claimsForm.getPatientId(), claimsForm.getVisitId(), formTypeRefId, call);
                    forms.add(form);
                }
                StringBuffer sb = new StringBuffer(4096 * 64);
                sb.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?><report>");
                for (FormModel form : forms) {
                    String xml = BaseData.getReport().getReportXml(ReportReference.SYSTEMDEFAULTFORMPRINT.getRefId(), form, call);
                    xml = xml.replace("<report>", "").replace("</report>", "").replace("<?xml version=\"1.0\" encoding=\"UTF-8\"?>", "");
                    sb.append(xml);
                }
                sb.append("</report>");
                long settingsFormId = processor.getProcessSettingsFormId();
                if (settingsFormId == 0L) {
                    throw new Exception("No settings form on processor");
                }
                FormModel settingsForm = ClinicalServer.getFormFromCache(settingsFormId);
                long dataToClaimsXslFileId = settingsForm.getIntValueForRecordItem(4959753L);
                if (dataToClaimsXslFileId == 0L) {
                    throw new Exception("No data to claims xsl file defined");
                }
                File tempXMLFile = SystemUtil.getTemporaryFile("xml");
                File tempDataFile = SystemUtil.getTemporaryFile("xml");
                File x12File = SystemUtil.getTemporaryFile("xml");
                File dataToClaimsXslFile = SystemUtil.getTemporaryFile("xsl");
                FileSystemUtil.createBinaryFile(dataToClaimsXslFile, SystemServer.getFileContents(dataToClaimsXslFileId).getBytes());
                FileSystemUtil.createFile(tempXMLFile.getAbsolutePath(), sb.toString());
                XsltUtil.createTextFile(tempXMLFile, dataToClaimsXslFile, tempDataFile);
                long xslX12FileId = settingsForm.getIntValueForRecordItem(4959754L);
                if (dataToClaimsXslFileId == 0L) {
                    throw new Exception("No data to claims xsl file defined");
                }
                File xslX12File = SystemUtil.getTemporaryFile("xsl");
                FileSystemUtil.createBinaryFile(xslX12File, SystemServer.getFileContents(xslX12FileId).getBytes());
                XsltUtil.createTextFile(tempDataFile, xslX12File, x12File);
                FileUtils.copyFile(tempXMLFile, new File("C:\\temp\\data.xml"));
                FileUtils.copyFile(tempDataFile, new File("C:\\temp\\claim_data.xml"));
                FileUtils.copyFile(x12File, new File("C:\\temp\\x12.txt"));
                claimsForm.setFormStateRef(new DisplayModel(50003630L));
                BaseData.getClinical().store(claimsForm, chain, call);
                return true;
            }
        };
        return (Boolean) call(method, call);
    }
}
