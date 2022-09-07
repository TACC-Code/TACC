class BackupThread extends Thread {
    public Content check(Integer id, CmsUser user) {
        Content content = findById(id);
        List<Map<String, Object>> mapList = preChange(content);
        ContentCheck check = content.getContentCheck();
        byte userStep = user.getCheckStep(content.getSite().getId());
        byte contentStep = check.getCheckStep();
        byte finalStep = content.getChannel().getFinalStepExtends();
        if (userStep < contentStep) {
            return content;
        }
        check.setRejected(false);
        if (userStep > contentStep) {
            check.setCheckOpinion(null);
        }
        check.setCheckStep(userStep);
        if (userStep >= finalStep) {
            content.setStatus(ContentCheck.CHECKED);
            check.setCheckOpinion(null);
        }
        afterChange(content, mapList);
        return content;
    }
}
