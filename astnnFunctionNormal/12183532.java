class BackupThread extends Thread {
    protected synchronized void getCurrentUserDisableViewChannels(AbstractSearchForm form, HttpServletRequest request) throws ApplicationException {
        PersonalHomePreferenceDAObject personalPreferenceDAO = new PersonalHomePreferenceDAObject(this.getSessionContainer(request), this.getConnection(request));
        PersonalHomePreference personalPreference = (PersonalHomePreference) personalPreferenceDAO.getByObjectByUserRecordID(this.getSessionContainer(request).getUserRecordID());
        PreferenceManager preferenceMg = new PreferenceManager(this.getSessionContainer(request), this.getConnection(request));
        ListPersonalHomeForm maintForm = (ListPersonalHomeForm) form;
        if (!Utility.isEmpty(personalPreference)) {
            if (!Utility.isEmpty(personalPreference.getDisableViewChannel())) {
                String channeldisableViewAndViewStr = preferenceMg.fitTheOldChannelConfig(personalPreference.getDisableViewChannel());
                String[] preferences = preferenceMg.getChannelPreference(channeldisableViewAndViewStr);
                maintForm.setDisableViewChannels(preferences[0]);
                maintForm.setAbleViewChannels(preferences[1]);
            } else {
                String[] preferences = preferenceMg.getChannelPreference(UserHomePreferenceConstant.SYSTEMPREFERENCESTR);
                maintForm.setDisableViewChannels(preferences[0]);
                maintForm.setAbleViewChannels(preferences[1]);
            }
        } else {
            String[] preferences = preferenceMg.getChannelPreference(UserHomePreferenceConstant.SYSTEMPREFERENCESTR);
            maintForm.setDisableViewChannels(preferences[0]);
            maintForm.setAbleViewChannels(preferences[1]);
        }
        List channelList = preferenceMg.getChannelSequence(maintForm.getAbleViewChannels());
        request.setAttribute("channelList", channelList);
    }
}
