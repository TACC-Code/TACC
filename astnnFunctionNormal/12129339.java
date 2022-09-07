class BackupThread extends Thread {
    public void Setup() {
        quad_ch[0] = new ChannelAgent(ChannelFactory.defaultFactory().getChannel("Ring_Mag:PS_QV03a05a07:B_Set"));
        quad_ch[1] = new ChannelAgent(ChannelFactory.defaultFactory().getChannel("Ring_Mag:PS_QH02a08:B_Set"));
        quad_ch[2] = new ChannelAgent(ChannelFactory.defaultFactory().getChannel("Ring_Mag:PS_QH04a06:B_Set"));
        quad_ch[3] = new ChannelAgent(ChannelFactory.defaultFactory().getChannel("Ring_Mag:PS_QV01a09:B_Set"));
        quad_ch[4] = new ChannelAgent(ChannelFactory.defaultFactory().getChannel("Ring_Mag:PS_QV11a12:B_Set"));
        quad_ch[5] = new ChannelAgent(ChannelFactory.defaultFactory().getChannel("Ring_Mag:PS_QH10a13:B_Set"));
        sext_ch[0] = new ChannelAgent(ChannelFactory.defaultFactory().getChannel("Ring_Mag:PS_SV03a07:B_Set"));
        sext_ch[1] = new ChannelAgent(ChannelFactory.defaultFactory().getChannel("Ring_Mag:PS_SH04:B_Set"));
        sext_ch[2] = new ChannelAgent(ChannelFactory.defaultFactory().getChannel("Ring_Mag:PS_SV05:B_Set"));
        sext_ch[3] = new ChannelAgent(ChannelFactory.defaultFactory().getChannel("Ring_Mag:PS_SH06:B_Set"));
        brho_nom = 5.65737;
        mass = 0.938272310;
        c = 2.99792458e8;
    }
}
