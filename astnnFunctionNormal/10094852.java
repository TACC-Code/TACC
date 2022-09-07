class BackupThread extends Thread {
    public boolean equals(Object other) {
        if ((this == other)) return true;
        if ((other == null)) return false;
        if (!(other instanceof AbstractAssayBiomaterial)) return false;
        AbstractAssayBiomaterial castOther = (AbstractAssayBiomaterial) other;
        return ((this.getAssay() == castOther.getAssay()) || (this.getAssay() != null && castOther.getAssay() != null && this.getAssay().equals(castOther.getAssay()))) && ((this.getChannel() == castOther.getChannel()) || (this.getChannel() != null && castOther.getChannel() != null && this.getChannel().equals(castOther.getChannel()))) && ((this.getBiomaterial() == castOther.getBiomaterial()) || (this.getBiomaterial() != null && castOther.getBiomaterial() != null && this.getBiomaterial().equals(castOther.getBiomaterial()))) && (this.getRank() == castOther.getRank());
    }
}
