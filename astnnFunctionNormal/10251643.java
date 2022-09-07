class BackupThread extends Thread {
    @PostConstruct
    public void init() {
        ExternalContext ec = FacesContext.getCurrentInstance().getExternalContext();
        HttpServletRequest request = (HttpServletRequest) ec.getRequest();
        int chNu = new Integer(request.getParameter("chNu"));
        channel = this.getCache().getChannel(chNu);
        lEPG = this.getCache().getEpgForChNu(chNu, new Date());
        logger.debug(lEPG.size() + " EPGs fetched for " + channel.getChannelNumber());
    }
}
