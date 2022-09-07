class BackupThread extends Thread {
    @SuppressWarnings("unchecked")
    public static String deploySecurityPolicy(String processName, PolicyType policyType) throws PolicyInstatiationFailedException {
        log.debug("trying to deploy security policy:" + policyType);
        if (!checkResourceLimitations(processName, policyType)) {
            try {
                throw new PolicyInstatiationFailedException("The policy is not valid because it must be restricted to the deployed service. Insert or corrent ResoruceMatch for policy like " + example(processName));
            } catch (XmlException e) {
                throw new PolicyInstatiationFailedException("The policy is not valid because it must be restricted to the deployed service.");
            }
        }
        String securityPoliciesFolderString = BISGridProperties.getInstance().getProperty(BISGridProperties.BISGRID_XACML_POLICIES_DIRECTORY, "xacml");
        String securityFolicyFileString = securityPoliciesFolderString + File.separator + processName + policysuffix;
        String accessControlConfig = UAS.getSecurityProperties().getProperty(IUASSecurityProperties.UAS_CHECKACCESS_PDPCONFIG);
        log.info("trying to store policy document at: " + securityFolicyFileString);
        File securityPoliciesFile = new File(securityFolicyFileString);
        try {
            PolicyDocument policy = PolicyDocument.Factory.newInstance();
            policy.setPolicy(policyType);
            log.debug("saving PolicySetDocument: " + policy);
            policy.save(securityPoliciesFile);
        } catch (IOException e) {
            throw new PolicyInstatiationFailedException("Failed to write policy in file " + securityPoliciesFile.getAbsolutePath());
        }
        log.info("adding entry in xacml config: " + accessControlConfig);
        Element list = null;
        try {
            list = getListTagInXACMLConfig(accessControlConfig);
            boolean exists = false;
            List<Element> stringChildren = (List<Element>) list.getChildren("string", Namespace.getNamespace("http://sunxacml.sourceforge.net/schema/config-0.3"));
            if (stringChildren.isEmpty()) {
                throw new PolicyInstatiationFailedException("there is no <string> in list tag. It seems that no policy is instatiated");
            } else {
                for (Element cur : stringChildren) {
                    if ("string".equals(cur.getName())) {
                        if (securityFolicyFileString.equals(cur.getText())) {
                            exists = true;
                            break;
                        }
                    }
                }
            }
            if (!exists) {
                Element newPolicy = new Element("string", Namespace.getNamespace("http://sunxacml.sourceforge.net/schema/config-0.3"));
                newPolicy.addContent(securityFolicyFileString);
                list.addContent(newPolicy);
            } else {
                log.debug("entry for this workflow already exists. write config to force reread");
            }
            writeXMLDocInFile(accessControlConfig, list);
        } catch (PolicyConfigProcessingException e1) {
            securityPoliciesFile.delete();
            throw new PolicyInstatiationFailedException(e1.getMessage());
        }
        return securityFolicyFileString;
    }
}
