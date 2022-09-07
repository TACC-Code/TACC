class BackupThread extends Thread {
    public void blankToNull() {
        if (StringUtils.isBlank(getLink())) {
            setLink(null);
        }
        if (StringUtils.isBlank(getTplChannel())) {
            setTplChannel(null);
        }
        if (StringUtils.isBlank(getTplContent())) {
            setTplContent(null);
        }
        if (StringUtils.isBlank(getTitle())) {
            setTitle(null);
        }
        if (StringUtils.isBlank(getKeywords())) {
            setKeywords(null);
        }
        if (StringUtils.isBlank(getDescription())) {
            setDescription(null);
        }
        if (StringUtils.isBlank(getTitleImg())) {
            setTitleImg(null);
        }
        if (StringUtils.isBlank(getContentImg())) {
            setContentImg(null);
        }
        if (StringUtils.isBlank(getChannelRule())) {
            setChannelRule(null);
        }
        if (StringUtils.isBlank(getContentRule())) {
            setContentRule(null);
        }
    }
}
