class BackupThread extends Thread {
    private TreeNode[] getChannelTree(String siteChannelPath) throws Exception {
        List list = new ArrayList();
        DBOperation dbo = null;
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        try {
            dbo = createDBOperation();
            StringBuffer sql = new StringBuffer();
            sql.append("select ").append("tree.id tree_id,").append("tree.parent_id tree_parent_id,").append("tree.path tree_path,").append("tree.ref_path tree_ref_path,").append("tree.tree_level tree_tree_level,").append("tree.type tree_type,").append("tree.title tree_title,").append("tree.order_no tree_order_no,").append("tree.description tree_description,").append("tree.style tree_style,").append("tree.icon tree_icon,").append("channel.id chan_id,").append("channel.name chan_name,").append("channel.description chan_description,").append("channel.ascii_name chan_ascii_name,").append("channel.site_id chan_site_id,").append("channel.type chan_type,").append("channel.data_url chan_data_url,").append("channel.template_id chan_template_id,").append("channel.use_status chan_use_status,").append("channel.order_no chan_order_no,").append("channel.style chan_style,").append("channel.creator chan_creator,").append("channel.create_date chan_create_date,").append("channel.refresh_flag chan_refresh_flag ").append("from t_ip_tree_frame tree,t_ip_channel channel ").append("where path like ? and path<>? and tree.path=channel.channel_path order by tree_path, chan_order_no");
            connection = dbo.getConnection();
            preparedStatement = connection.prepareStatement(sql.toString());
            preparedStatement.setString(1, siteChannelPath + "%");
            preparedStatement.setString(2, siteChannelPath);
            resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                Channel channel = new Channel();
                channel.setAsciiName(resultSet.getString("chan_ascii_name"));
                channel.setChannelID(resultSet.getInt("chan_id"));
                channel.setChannelType(resultSet.getString("chan_type"));
                channel.setCreateDate(resultSet.getTimestamp("chan_create_date"));
                channel.setCreator(resultSet.getInt("chan_creator"));
                channel.setDataUrl(resultSet.getString("chan_data_url"));
                channel.setDesc(resultSet.getString("chan_description"));
                channel.setName(resultSet.getString("chan_name"));
                channel.setOrderNo(resultSet.getInt("chan_order_no"));
                channel.setRefresh(resultSet.getString("chan_refresh_flag"));
                channel.setSiteId(resultSet.getInt("chan_site_id"));
                channel.setStyle(resultSet.getString("chan_style"));
                channel.setTemplateId(resultSet.getString("chan_template_id"));
                channel.setUseStatus(resultSet.getString("chan_use_status"));
                channel.setDescription(resultSet.getString("tree_description"));
                channel.setIcon(resultSet.getString("tree_icon"));
                channel.setId(resultSet.getString("tree_id"));
                channel.setLevel(resultSet.getInt("tree_tree_level"));
                channel.setParentId(resultSet.getString("tree_parent_id"));
                channel.setPath(resultSet.getString("tree_path"));
                channel.setRefPath(resultSet.getString("tree_ref_path"));
                channel.setTitle(resultSet.getString("tree_title"));
                channel.setType(resultSet.getString("tree_type"));
                list.add(channel);
            }
        } catch (SQLException ex) {
            log.error("�õ�������!", ex);
            throw ex;
        } finally {
            close(resultSet, null, preparedStatement, connection, dbo);
        }
        TreeNode[] result = null;
        if (list.size() > 0) {
            result = new TreeNode[list.size()];
        }
        for (int i = 0; i < list.size(); i++) {
            result[i] = (TreeNode) list.get(i);
        }
        return result;
    }
}
