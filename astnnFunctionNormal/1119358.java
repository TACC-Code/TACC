class BackupThread extends Thread {
    public void getDefaultImage(FacesContext context, HttpServletResponse response) throws IOException {
        InputStream defaultImage = context.getExternalContext().getResourceAsStream(DEFAULT_IMAGE_URL);
        response.setContentType("image/jpeg");
        OutputStream out = response.getOutputStream();
        ImageIO.write(ImageIO.read(defaultImage), "jpeg", out);
        out.close();
        return;
    }
}
