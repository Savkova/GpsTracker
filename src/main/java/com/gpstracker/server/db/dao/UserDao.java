package com.gpstracker.server.db.dao;

import com.gpstracker.server.db.entities.User;
import com.gpstracker.server.db.map.UserMapper;
import com.gpstracker.server.util.Constants.DBTable;
import org.skife.jdbi.v2.sqlobject.Bind;
import org.skife.jdbi.v2.sqlobject.BindBean;
import org.skife.jdbi.v2.sqlobject.SqlQuery;
import org.skife.jdbi.v2.sqlobject.SqlUpdate;
import org.skife.jdbi.v2.sqlobject.customizers.RegisterMapper;

@RegisterMapper(UserMapper.class)
public interface UserDao {
    @SqlQuery("SELECT * FROM " + DBTable.USERS + " WHERE id = :id")
    public User getById(@Bind("id") int id);

    @SqlQuery("SELECT * FROM " + DBTable.USERS + " WHERE token = :token")
    public User getByToken(@Bind("token") byte[] token);

    @SqlUpdate("INSERT INTO " + DBTable.USERS + " (login, password, email, name, timezone, token) VALUES (:login, :password, :email, :name, :timezone, :token)")
    public void insert(@BindBean User user);

    @SqlUpdate("UPDATE " + DBTable.USERS + " SET name = :name WHERE id = :id")
    public void changeName(@Bind("id") int id, @Bind("name") String name);

    @SqlUpdate("UPDATE " + DBTable.USERS + " SET email = :email WHERE id = :id")
    public void changeEmail(@Bind("id") int id, @Bind("email") String email);

    @SqlUpdate("UPDATE " + DBTable.USERS + " SET token = :token WHERE id = :id")
    public void changeToken(@Bind("id") int id, @Bind("token") byte[] token);

    @SqlUpdate("UPDATE " + DBTable.USERS + " SET password = :password WHERE id = :id")
    public void changePassword(@Bind("id") int id, @Bind("password") byte[] password);

    @SqlUpdate("UPDATE " + DBTable.USERS + " SET timezone = :timezone WHERE id = :id")
    public void changeTimezone(@Bind("id") int id, @Bind("timezone") Short timezone);

    @SqlUpdate("DELETE FROM " + DBTable.USERS + " WHERE id = :id")
    public void delete(@Bind("id") int id);

    @SqlQuery("SELECT COUNT(*) FROM " + DBTable.USERS)
    public int getCount();
}
