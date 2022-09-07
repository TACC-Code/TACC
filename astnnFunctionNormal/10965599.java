class BackupThread extends Thread {
    public String doUpdate(String username, String password, String domainName, String ip) {
        String retorno = "";
        try {
            String endereco = "http://members.dyndns.org/nic/update?hostname=" + domainName + "&myip=" + ip + "&wildcard=NOCHG&mx=NOCHG&backmx=NOCHG";
            String userPass = username + ":" + password;
            String codificado = Base64.encodeBytes(userPass.getBytes());
            URL url = new URL(endereco);
            URLConnection connection = url.openConnection();
            connection.setRequestProperty("Authorization", "Basic " + codificado);
            BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            String linha;
            String rtn = "";
            while ((linha = in.readLine()) != null) {
                rtn += linha;
            }
            in.close();
            retorno = filtraRetorno(rtn);
        } catch (IOException ex) {
            Logger.getLogger(ExternalIp.class.getName()).log(Level.SEVERE, null, ex);
            return "ERROR.";
        }
        return retorno;
    }
}
