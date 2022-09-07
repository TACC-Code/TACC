class BackupThread extends Thread {
    public String enviaDadosIndiq(String agendamento_id) {
        String sql = "";
        String resp = "";
        String email, nome, nascimento, sexo, cpf, profissional, crm;
        try {
            stmt = con.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_READ_ONLY);
            ResultSet rs = null;
            sql = "SELECT paciente.email, paciente.nome, paciente.data_nascimento, ";
            sql += "paciente.cod_sexo, paciente.cic, profissional.nome, profissional.reg_prof ";
            sql += "FROM (agendamento INNER JOIN paciente ON agendamento.codcli = ";
            sql += "paciente.codcli) INNER JOIN profissional ON ";
            sql += "agendamento.prof_reg = profissional.prof_reg ";
            sql += "WHERE agendamento.agendamento_id=" + agendamento_id;
            rs = stmt.executeQuery(sql);
            if (rs.next()) {
                email = Util.trataNulo(rs.getString("email"), "");
                nome = Util.trataNulo(rs.getString("paciente.nome"), "");
                nascimento = Util.trataNulo(rs.getString("data_nascimento"), "");
                sexo = Util.trataNulo(rs.getString("cod_sexo"), "");
                cpf = Util.trataNulo(rs.getString("cic"), "");
                cpf = cpf.replace(".", "");
                cpf = cpf.replace("-", "");
                profissional = Util.trataNulo(rs.getString("profissional.nome"), "");
                crm = Util.trataNulo(rs.getString("reg_prof"), "");
                resp = "http://www.indiq.com.br/cad_katu.php?email=" + email + "&nome=" + nome + "&dnasc=" + nascimento;
                resp += "&sexo=" + sexo + "&doc=" + cpf + "&nmedico=" + profissional + "&docm=" + crm;
                resp = resp.replace(" ", "%20");
                if (!Util.isNull(cpf)) {
                    URL url = new URL(resp);
                    HttpURLConnection con = (HttpURLConnection) url.openConnection();
                    BufferedReader br = new BufferedReader(new InputStreamReader(con.getInputStream()));
                    con.disconnect();
                }
                return "OK";
            } else return "Usu�rio n�o encontrado";
        } catch (SQLException e) {
            return "ERRO no banco de dados: " + e.toString() + " SQL: " + sql;
        } catch (MalformedURLException ex) {
            return "Erro no URL: " + ex.toString();
        } catch (IOException ex) {
            return "Erro de entrada e sa�da: " + ex.toString();
        }
    }
}
