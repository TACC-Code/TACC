class BackupThread extends Thread {
    public Content recycle(Integer id) {
        Content content = findById(id);
        List<Map<String, Object>> mapList = preChange(content);
        byte contentStep = content.getCheckStep();
        byte finalStep = content.getChannel().getFinalStepExtends();
        if (contentStep >= finalStep && !content.getRejected()) {
            content.setStatus(ContentCheck.CHECKED);
        } else {
            content.setStatus(ContentCheck.CHECKING);
        }
        afterChange(content, mapList);
        return content;
    }
}
