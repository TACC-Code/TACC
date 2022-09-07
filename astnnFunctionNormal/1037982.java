class BackupThread extends Thread {
    private void SetModes() {
        if (getIsOperator()) {
            if (btnRequireKey.getSelection()) Connection.getIrcclient().setChannelKey(getChannel(), edtKey.getText()); else Connection.getIrcclient().removeChannelKey(getChannel(), edtKey.getText());
            if (btnInvitei.getSelection()) Connection.getIrcclient().setInviteOnly(getChannel()); else Connection.getIrcclient().removeInviteOnly(getChannel());
            if (btnPrivatep.getSelection()) Connection.getIrcclient().setSecret(getChannel()); else Connection.getIrcclient().removeSecret(getChannel());
            if (btnOnlySetOp.getSelection()) Connection.getIrcclient().setTopicProtection(getChannel()); else Connection.getIrcclient().removeTopicProtection(getChannel());
            if (btnUserLimitTo.getSelection()) Connection.getIrcclient().setChannelLimit(getChannel(), sLimit.getSelection()); else Connection.getIrcclient().removeChannelLimit(getChannel());
            if (isIstopicChange()) {
                Connection.getIrcclient().setTopic(getChannel(), TemplateManager.replace(stTopic.getText()));
            }
        }
    }
}
