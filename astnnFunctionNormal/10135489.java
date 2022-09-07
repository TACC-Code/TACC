class BackupThread extends Thread {
    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
        String siteChannelPath = request.getParameter("siteChannelPath") == null ? TreeNode.SITECHANNEL_TREE_ROOT_PATH : request.getParameter("siteChannelPath");
        try {
            TreeNode tn = TreeNode.getInstance(siteChannelPath);
            if (tn == null) {
                throw new Exception("û���ҵ�path=" + siteChannelPath + "��ʵ��!");
            }
            TreeNode[] channels = tn.getList();
            if (channels == null) {
                channels = new Channel[0];
            }
            request.setAttribute("parentPath", siteChannelPath);
            LoginUser loginUser = new LoginUser();
            loginUser.setRequest(request);
            int userId = Integer.parseInt(loginUser.getDefaultValue());
            Hashtable extrAuths = (Hashtable) request.getSession().getAttribute("extrauthority");
            Pepodom pepodom = new Pepodom();
            int parentResId = -1;
            if (tn.getPath().length() > 10) {
                parentResId = Const.CHANNEL_TYPE_RES + ((Channel) tn).getChannelID();
            } else {
                parentResId = Const.SITE_TYPE_RES + ((Site) tn).getSiteID();
            }
            pepodom.setResID(Integer.toString(parentResId));
            pepodom.setResTypeID(Const.OPERATE_TYPE_ID);
            pepodom.setLoginProvider(userId);
            pepodom.setOperateID(Const.OPERATE_ID_NEW);
            boolean hasNewAuth = pepodom.isDisplay(extrAuths);
            String[] authority = Function.newStringArray(channels.length, "");
            String[] operates = { Const.OPERATE_ID_COLLECT, Const.OPERATE_ID_NEW, Const.OPERATE_ID_DEL, Const.OPERATE_ID_MODIFY, Const.OPERATE_ID_PREVIEW, Const.OPERATE_ID_INCRE, Const.OPERATE_ID_COMPLETE, Const.OPERATE_ID_RELEASE };
            for (int i = 0; i < channels.length; i++) {
                int resId = Const.CHANNEL_TYPE_RES + ((Channel) channels[i]).getChannelID();
                pepodom.setResID(Integer.toString(resId));
                pepodom.setResTypeID(Const.OPERATE_TYPE_ID);
                pepodom.setLoginProvider(userId);
                for (int j = 0; j < operates.length; j++) {
                    pepodom.setOperateID(operates[j]);
                    boolean hasAuth = pepodom.isDisplay(extrAuths);
                    authority[i] += hasAuth ? "1" : "0";
                }
            }
            Channel[] chan = new Channel[channels.length];
            for (int k = 0; k < channels.length; k++) {
                chan[k] = (Channel) channels[k];
                String id = chan[k].getTemplateId();
                if (id != null) {
                    Template td = new TemplateDAO().getInstance(Integer.parseInt(id));
                    chan[k].setTemplateName(td.getName());
                }
            }
            request.setAttribute("result", chan);
            request.setAttribute("authority", authority);
            request.setAttribute("hasNewAuth", hasNewAuth ? "1" : "0");
        } catch (Exception ex) {
            log.error("��ȡƵ���б����!", ex);
            request.setAttribute(Const.ERROR_MESSAGE_NAME, ex.toString());
            mapping.findForward("error");
        }
        return mapping.findForward("list");
    }
}
