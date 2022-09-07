class BackupThread extends Thread {
    public Content update(Content bean, ContentExt ext, ContentTxt txt, String[] tagArr, Integer[] channelIds, Integer[] topicIds, Integer[] viewGroupIds, String[] attachmentPaths, String[] attachmentNames, String[] attachmentFilenames, String[] picPaths, String[] picDescs, Map<String, String> attr, Integer channelId, Integer typeId, Boolean draft, CmsUser user, boolean forMember) {
        Content entity = findById(bean.getId());
        List<Map<String, Object>> mapList = preChange(entity);
        Updater<Content> updater = new Updater<Content>(bean);
        bean = dao.updateByUpdater(updater);
        Byte userStep;
        if (forMember) {
            userStep = 0;
        } else {
            CmsSite site = bean.getSite();
            userStep = user.getCheckStep(site.getId());
        }
        AfterCheckEnum after = bean.getChannel().getAfterCheckEnum();
        if (after == AfterCheckEnum.BACK_UPDATE && bean.getCheckStep() > userStep) {
            bean.getContentCheck().setCheckStep(userStep);
            if (bean.getCheckStep() >= bean.getChannel().getFinalStepExtends()) {
                bean.setStatus(ContentCheck.CHECKED);
            } else {
                bean.setStatus(ContentCheck.CHECKING);
            }
        }
        if (draft != null) {
            if (draft) {
                bean.setStatus(DRAFT);
            } else {
                if (bean.getStatus() == DRAFT) {
                    if (bean.getCheckStep() >= bean.getChannel().getFinalStepExtends()) {
                        bean.setStatus(ContentCheck.CHECKED);
                    } else {
                        bean.setStatus(ContentCheck.CHECKING);
                    }
                }
            }
        }
        bean.setHasTitleImg(!StringUtils.isBlank(ext.getTitleImg()));
        if (channelId != null) {
            bean.setChannel(channelMng.findById(channelId));
        }
        if (typeId != null) {
            bean.setType(contentTypeMng.findById(typeId));
        }
        contentExtMng.update(ext);
        contentTxtMng.update(txt, bean);
        if (attr != null) {
            Map<String, String> attrOrig = bean.getAttr();
            attrOrig.clear();
            attrOrig.putAll(attr);
        }
        Set<Channel> channels = bean.getChannels();
        channels.clear();
        if (channelIds != null && channelIds.length > 0) {
            for (Integer cid : channelIds) {
                channels.add(channelMng.findById(cid));
            }
        }
        channels.add(bean.getChannel());
        Set<CmsTopic> topics = bean.getTopics();
        topics.clear();
        if (topicIds != null && topicIds.length > 0) {
            for (Integer tid : topicIds) {
                topics.add(cmsTopicMng.findById(tid));
            }
        }
        Set<CmsGroup> groups = bean.getViewGroups();
        groups.clear();
        if (viewGroupIds != null && viewGroupIds.length > 0) {
            for (Integer gid : viewGroupIds) {
                groups.add(cmsGroupMng.findById(gid));
            }
        }
        contentTagMng.updateTags(bean.getTags(), tagArr);
        bean.getAttachments().clear();
        if (attachmentPaths != null && attachmentPaths.length > 0) {
            for (int i = 0, len = attachmentPaths.length; i < len; i++) {
                if (!StringUtils.isBlank(attachmentPaths[i])) {
                    bean.addToAttachmemts(attachmentPaths[i], attachmentNames[i], attachmentFilenames[i]);
                }
            }
        }
        bean.getPictures().clear();
        if (picPaths != null && picPaths.length > 0) {
            for (int i = 0, len = picPaths.length; i < len; i++) {
                if (!StringUtils.isBlank(picPaths[i])) {
                    bean.addToPictures(picPaths[i], picDescs[i]);
                }
            }
        }
        afterChange(bean, mapList);
        return bean;
    }
}
