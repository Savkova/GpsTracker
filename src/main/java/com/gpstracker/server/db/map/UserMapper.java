package com.gpstracker.server.db.map;

import com.gpstracker.server.db.entities.User;
import org.skife.jdbi.v2.StatementContext;
import org.skife.jdbi.v2.tweak.ResultSetMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class UserMapper implements ResultSetMapper<User>{
    @Override
    public User map(int index, ResultSet rs, StatementContext ctx) throws SQLException {
        return new User(rs.getInt("id"),
                rs.getString("login"),
                rs.getBytes("password"),
                rs.getString("email"),
                rs.getString("name"),
                rs.getShort("timezone"),
                rs.getBytes("token"));
    }

}
