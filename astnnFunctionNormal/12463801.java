class BackupThread extends Thread {
    private Channel getChannelInstance(String path) throws Exception {
        Channel result = null;
        DBOperation dbo = null;
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        try {
            dbo = createDBOperation();
            StringBuffer sql = new StringBuffer();
            sql.append("select ").append("tree.id tree_id,").append("tree.parent_id tree_parent_id,").append("tree.path tree_path,").append("tree.ref_path tree_ref_path,").append("tree.tree_level tree_tree_level,").append("tree.type tree_type,").append("tree.title tree_title,").append("tree.order_no tree_order_no,").append("tree.description tree_description,").append("tree.style tree_style,").append("tree.icon tree_icon,").append("channel.id chan_id,").append("channel.name chan_name,").append("channel.description chan_description,").append("channel.ascii_name chan_ascii_name,").append("channel.site_id chan_site_id,").append("channel.type chan_type,").append("channel.data_url chan_data_url,").append("channel.template_id chan_template_id,").append("channel.use_status chan_use_status,").append("channel.order_no chan_order_no,").append("channel.style chan_style,").append("channel.creator chan_creator,").append("channel.create_date chan_create_date,").append("channel.refresh_flag chan_refresh_flag, ").append("channel.page_num page_num ").append("from t_ip_tree_frame tree,t_ip_channel channel ").append("where path = ? and tree.path=channel.channel_path order by path");
            connection = dbo.getConnection();
            preparedStatement = connection.prepareStatement(sql.toString());
            preparedStatement.setString(1, path);
            resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                result = new Channel();
                result.setAsciiName(resultSet.getString("chan_ascii_name"));
                result.setChannelID(resultSet.getInt("chan_id"));
                result.setChannelType(resultSet.getString("chan_type"));
                result.setCompletePath("");
                result.setCreateDate(resultSet.getTimestamp("chan_create_date"));
                result.setCreator(resultSet.getInt("chan_creator"));
                result.setDataUrl(resultSet.getString("chan_data_url"));
                result.setDesc(resultSet.getString("chan_description"));
                result.setName(resultSet.getString("chan_name"));
                result.setOrderNo(resultSet.getInt("chan_order_no"));
                result.setRefresh(resultSet.getString("chan_refresh_flag"));
                result.setSiteId(resultSet.getInt("chan_site_id"));
                result.setStyle(resultSet.getString("chan_style"));
                result.setTemplateId(resultSet.getString("chan_template_id"));
                result.setUseStatus(resultSet.getString("chan_use_status"));
                result.setDescription(resultSet.getString("tree_description"));
                result.setIcon(resultSet.getString("tree_icon"));
                result.setId(resultSet.getString("tree_id"));
                result.setLevel(resultSet.getInt("tree_tree_level"));
                result.setParentId(resultSet.getString("tree_parent_id"));
                result.setPath(resultSet.getString("tree_path"));
                result.setRefPath(resultSet.getString("tree_ref_path"));
                result.setTitle(resultSet.getString("tree_title"));
                result.setType(resultSet.getString("tree_type"));
                result.setPageNum(resultSet.getInt("page_num"));
            }
        } catch (SQLException ex) {
            log.error("�õ�Ƶ��ʵ�����!path=" + path, ex);
            throw ex;
        } finally {
            close(resultSet, null, preparedStatement, connection, dbo);
        }
        return result;
    }
}
