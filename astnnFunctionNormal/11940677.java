class BackupThread extends Thread {
    public static String PopulateTemplate(HttpServletRequest request, boolean getTemplateFullPathOnly, boolean getTemplateFilenameOnly, boolean populateDMCodes, String selectedBeamlineName, List hashDMCodesForBeamline, boolean populateForExport, int nbContainersToExport, boolean populateForShipment, int shippingId) {
        String populatedTemplatePath = "";
        try {
            String xlsPath;
            String proposalCode;
            String proposalNumber;
            String populatedTemplateFileName;
            GregorianCalendar calendar = new GregorianCalendar();
            String today = ".xls";
            if (request != null) {
                xlsPath = Constants.TEMPLATE_POPULATED_RELATIVE_PATH;
                if (populateForShipment) xlsPath = Constants.TEMPLATE_RELATIVE_DIRECTORY_PATH + Constants.TEMPLATE_XLS_POPULATED_FROM_SHIPMENT; else if (populateForExport) {
                    switch(nbContainersToExport) {
                        case 1:
                            xlsPath = Constants.TEMPLATE_RELATIVE_DIRECTORY_PATH + Constants.TEMPLATE_XLS_POPULATED_FOR_EXPORT_FILENAME1;
                            break;
                        case 2:
                            xlsPath = Constants.TEMPLATE_RELATIVE_DIRECTORY_PATH + Constants.TEMPLATE_XLS_POPULATED_FOR_EXPORT_FILENAME2;
                            break;
                        case 3:
                            xlsPath = Constants.TEMPLATE_RELATIVE_DIRECTORY_PATH + Constants.TEMPLATE_XLS_POPULATED_FOR_EXPORT_FILENAME3;
                            break;
                        case 4:
                            xlsPath = Constants.TEMPLATE_RELATIVE_DIRECTORY_PATH + Constants.TEMPLATE_XLS_POPULATED_FOR_EXPORT_FILENAME4;
                            break;
                        case 5:
                            xlsPath = Constants.TEMPLATE_RELATIVE_DIRECTORY_PATH + Constants.TEMPLATE_XLS_POPULATED_FOR_EXPORT_FILENAME5;
                            break;
                        case 6:
                            xlsPath = Constants.TEMPLATE_RELATIVE_DIRECTORY_PATH + Constants.TEMPLATE_XLS_POPULATED_FOR_EXPORT_FILENAME6;
                            break;
                        case 7:
                            xlsPath = Constants.TEMPLATE_RELATIVE_DIRECTORY_PATH + Constants.TEMPLATE_XLS_POPULATED_FOR_EXPORT_FILENAME7;
                            break;
                        case 8:
                            xlsPath = Constants.TEMPLATE_RELATIVE_DIRECTORY_PATH + Constants.TEMPLATE_XLS_POPULATED_FOR_EXPORT_FILENAME8;
                            break;
                        case 9:
                            xlsPath = Constants.TEMPLATE_RELATIVE_DIRECTORY_PATH + Constants.TEMPLATE_XLS_POPULATED_FOR_EXPORT_FILENAME9;
                            break;
                        case 10:
                            xlsPath = Constants.TEMPLATE_RELATIVE_DIRECTORY_PATH + Constants.TEMPLATE_XLS_POPULATED_FOR_EXPORT_FILENAME10;
                            break;
                    }
                }
                proposalCode = (String) request.getSession().getAttribute(Constants.PROPOSAL_CODE);
                proposalNumber = String.valueOf(request.getSession().getAttribute(Constants.PROPOSAL_NUMBER));
                if (populateForShipment) populatedTemplateFileName = Constants.TEMPLATE_RELATIVE_DIRECTORY_PATH + proposalCode + proposalNumber + "_shipment_" + shippingId + today; else populatedTemplateFileName = Constants.TEMPLATE_RELATIVE_DIRECTORY_PATH + proposalCode + proposalNumber + ((populateDMCodes) ? "_#" : "") + today;
                populatedTemplatePath = request.getContextPath() + populatedTemplateFileName;
                if (getTemplateFilenameOnly && populateForShipment) return proposalCode + proposalNumber + "_shipment_" + shippingId + today;
                if (getTemplateFilenameOnly && !populateForShipment) return proposalCode + proposalNumber + ((populateDMCodes) ? "_#" : "") + today;
                xlsPath = request.getRealPath(xlsPath);
                populatedTemplateFileName = request.getRealPath(populatedTemplateFileName);
            } else {
                xlsPath = "C:/" + Constants.TEMPLATE_POPULATED_RELATIVE_PATH;
                proposalCode = "ehtpx";
                proposalNumber = "1";
                populatedTemplateFileName = "C:/" + Constants.TEMPLATE_RELATIVE_DIRECTORY_PATH + proposalCode + proposalNumber + today;
            }
            if (getTemplateFullPathOnly) return populatedTemplateFileName;
            String beamlineName = selectedBeamlineName;
            String[][] dmCodesinSC = null;
            if (populateDMCodes) {
                dmCodesinSC = new String[Constants.SC_BASKET_CAPACITY + 1][Constants.BASKET_SAMPLE_CAPACITY + 1];
                DatamatrixInSampleChangerFacadeLocal dms = DatamatrixInSampleChangerFacadeUtil.getLocalHome().create();
                ProposalLightValue prop = DBTools.getProposal(proposalCode, new Integer(proposalNumber));
                if (prop != null) {
                    Integer proposalId = prop.getProposalId();
                    List lstDMCodes = (List) dms.findByProposalIdAndBeamlineName(proposalId, beamlineName);
                    for (int i = 0; i < lstDMCodes.size(); i++) {
                        DatamatrixInSampleChangerValue dmInSC = (DatamatrixInSampleChangerValue) lstDMCodes.get(i);
                        Integer basketLocation = dmInSC.getContainerLocationInSc();
                        Integer sampleLocation = dmInSC.getLocationInContainer();
                        String dmCode = dmInSC.getDatamatrixCode();
                        if (basketLocation <= Constants.SC_BASKET_CAPACITY && sampleLocation <= Constants.BASKET_SAMPLE_CAPACITY) {
                            dmCodesinSC[basketLocation][sampleLocation] = dmCode;
                        }
                    }
                }
            }
            File originalTemplate = new File(xlsPath);
            File populatedTemplate = new File(populatedTemplateFileName);
            FileUtils.copyFile(originalTemplate, populatedTemplate);
            eHTPXXLSParser parser = new eHTPXXLSParser();
            File xlsTemplate = new File(xlsPath);
            File xlsPopulatedTemplate = new File(populatedTemplateFileName);
            FileUtils.copyFile(xlsTemplate, xlsPopulatedTemplate);
            ProposalFacadeLocal _proposal = ProposalFacadeUtil.getLocalHome().create();
            List proposals = (List) _proposal.findByCodeAndNumber(proposalCode, new Integer(proposalNumber));
            ProposalLightValue proposalLight = (ProposalLightValue) proposals.get(0);
            ProteinFacadeLocal _protein = ProteinFacadeUtil.getLocalHome().create();
            List listProteins = (List) _protein.findByProposalId(proposalLight.getPrimaryKey());
            parser.populate(xlsPath, populatedTemplateFileName, listProteins, dmCodesinSC);
            if (populateForShipment) parser.populateExistingShipment(populatedTemplateFileName, populatedTemplateFileName, shippingId);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return populatedTemplatePath;
    }
}
