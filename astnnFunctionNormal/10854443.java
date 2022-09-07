class BackupThread extends Thread {
    public ActionForward updateUsage(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws IOException {
        try {
            UsageForm usageForm = (UsageForm) form;
            UsageService usageService = Locator.lookupService(UsageService.class);
            response.setContentType("text/html");
            response.setCharacterEncoding("UTF-8");
            Usage usage = usageService.getUsageByUsageId(usageForm.getUsageId(), true);
            if (usage == null) {
                response.getWriter().write("{success:true,message:'This usage information has already been deleted'}");
                return mapping.findForward("");
            }
            List<Usage> usageList = usageService.getUsageByUsageName(usageForm.getUsageName());
            if (usageList.size() > 0 && usageList.get(0).getUsageId() != usageForm.getUsageId()) {
                response.getWriter().write("{success:false,message:'Usage name: " + usageForm.getUsageName() + " already existed'}");
                return mapping.findForward("");
            }
            usageList = usageService.getUsageByUsageCode(usageForm.getUsageCode());
            if (usageList.size() > 0 && usageList.get(0).getUsageId() != usageForm.getUsageId()) {
                response.getWriter().write("{success:false,message:'Usage code: " + usageForm.getUsageCode() + " already existed'}");
                return mapping.findForward("");
            }
            usage.setUsageName(usageForm.getUsageName());
            usage.setUsageCode(usageForm.getUsageCode());
            usage.setUsageRemark(usageForm.getUsageRemark());
            usageService.updateUsage(usage);
            response.getWriter().write("{success:true,message:'Modify usage information successfully'}");
            return mapping.findForward("");
        } catch (Exception e) {
            e.printStackTrace();
            response.getWriter().write("{success:false,message:'Unexpected exception occurred'}");
            return mapping.findForward("");
        }
    }
}
