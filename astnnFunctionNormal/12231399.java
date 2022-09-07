class BackupThread extends Thread {
    public static void main(String[] args) {
        try {
            URL url = new URL("http://localhost:8080/ConsultaServer/ConsultaServlet");
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setDoOutput(true);
            conn.setRequestMethod("POST");
            DataOutputStream dos = new DataOutputStream(conn.getOutputStream());
            dos.writeUTF("2007/10/16");
            dos.flush();
            dos.close();
            InputStream is = conn.getInputStream();
            DataInputStream dis = new DataInputStream(is);
            int tamanhoLista = dis.readInt();
            ConsultaMedica consulta = null;
            Vector vetor = new Vector();
            SimpleDateFormat format = new SimpleDateFormat(PADRAO);
            for (int i = 0; i < tamanhoLista; i++) {
                consulta = new ConsultaMedica();
                consulta.setData(format.parse(dis.readUTF()));
                consulta.setLocal(dis.readUTF());
                consulta.setNomePaciente(dis.readUTF());
                vetor.add(consulta);
            }
            dis.close();
            is.close();
            conn.disconnect();
            System.out.println("Numero de consultas: " + vetor.size());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
