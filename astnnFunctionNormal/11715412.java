class BackupThread extends Thread {
    public Content save(Content bean, ContentExt ext, ContentTxt txt, Integer[] channelIds, Integer[] topicIds, Integer[] viewGroupIds, String[] tagArr, String[] attachmentPaths, String[] attachmentNames, String[] attachmentFilenames, String[] picPaths, String[] picDescs, Integer channelId, Integer typeId, Boolean draft, CmsUser user, boolean forMember) {
        bean.setChannel(channelMng.findById(channelId));
        bean.setType(contentTypeMng.findById(typeId));
        bean.setUser(user);
        Byte userStep;
        if (forMember) {
            userStep = 0;
        } else {
            CmsSite site = bean.getSite();
            userStep = user.getCheckStep(site.getId());
        }
        if (draft != null && draft) {
            bean.setStatus(ContentCheck.DRAFT);
        } else {
            if (userStep >= bean.getChannel().getFinalStepExtends()) {
                bean.setStatus(ContentCheck.CHECKED);
            } else {
                bean.setStatus(ContentCheck.CHECKING);
            }
        }
        bean.setHasTitleImg(!StringUtils.isBlank(ext.getTitleImg()));
        bean.init();
        preSave(bean);
        dao.save(bean);
        contentExtMng.save(ext, bean);
        contentTxtMng.save(txt, bean);
        ContentCheck check = new ContentCheck();
        check.setCheckStep(userStep);
        contentCheckMng.save(check, bean);
        contentCountMng.save(new ContentCount(), bean);
        if (channelIds != null && channelIds.length > 0) {
            for (Integer cid : channelIds) {
                bean.addToChannels(channelMng.findById(cid));
            }
        }
        bean.addToChannels(channelMng.findById(channelId));
        if (topicIds != null && topicIds.length > 0) {
            for (Integer tid : topicIds) {
                bean.addToTopics(cmsTopicMng.findById(tid));
            }
        }
        if (viewGroupIds != null && viewGroupIds.length > 0) {
            for (Integer gid : viewGroupIds) {
                bean.addToGroups(cmsGroupMng.findById(gid));
            }
        }
        List<ContentTag> tags = contentTagMng.saveTags(tagArr);
        bean.setTags(tags);
        if (attachmentPaths != null && attachmentPaths.length > 0) {
            for (int i = 0, len = attachmentPaths.length; i < len; i++) {
                if (!StringUtils.isBlank(attachmentPaths[i])) {
                    bean.addToAttachmemts(attachmentPaths[i], attachmentNames[i], attachmentFilenames[i]);
                }
            }
        }
        if (picPaths != null && picPaths.length > 0) {
            for (int i = 0, len = picPaths.length; i < len; i++) {
                if (!StringUtils.isBlank(picPaths[i])) {
                    bean.addToPictures(picPaths[i], picDescs[i]);
                }
            }
        }
        afterSave(bean);
        return bean;
    }
}
