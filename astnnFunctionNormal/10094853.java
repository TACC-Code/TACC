class BackupThread extends Thread {
    public int hashCode() {
        int result = 17;
        result = 37 * result + (getAssay() == null ? 0 : this.getAssay().hashCode());
        result = 37 * result + (getChannel() == null ? 0 : this.getChannel().hashCode());
        result = 37 * result + (getBiomaterial() == null ? 0 : this.getBiomaterial().hashCode());
        result = 37 * result + this.getRank();
        return result;
    }
}
