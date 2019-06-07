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

    @SqlQuery("SELECT * FROM " + DBTable.USERS + " WHERE login = :login")
    public User getByLogin(@Bind("login") String login);

    @SqlQuery("SELECT * FROM " + DBTable.USERS + " WHERE email = :email")
    public User getByEmail(@Bind("email") String email);

    @SqlQuery("SELECT * FROM " + DBTable.USERS + " WHERE login = :login AND password = :password")
    public User getByLoginAndPassword(@Bind("login") String login, @Bind ("password") byte[] password);

    @SqlUpdate("INSERT INTO " + DBTable.USERS + " (login, password, email, name, timezone) VALUES (:login, :password, :email, :name, :timezone)")
    public void insert(@BindBean User user);

    @SqlUpdate("UPDATE " + DBTable.USERS + " SET name = :name WHERE id = :id")
    public void changeName(@Bind("id") int id, @Bind("name") String name);

    @SqlUpdate("UPDATE " + DBTable.USERS + " SET email = :email WHERE id = :id")
    public void changeEmail(@Bind("id") int id, @Bind("email") String email);

    @SqlUpdate("UPDATE " + DBTable.USERS + " SET password = :password WHERE id = :id")
    public void changePassword(@Bind("id") int id, @Bind("password") byte[] password);

    @SqlUpdate("UPDATE " + DBTable.USERS + " SET timezone = :timezone WHERE id = :id")
    public void changeTimezone(@Bind("id") int id, @Bind("timezone") Short timezone);

    @SqlUpdate("DELETE FROM " + DBTable.USERS + " WHERE id = :id")
    public void delete(@Bind("id") int id);

    @SqlQuery("SELECT COUNT(*) FROM " + DBTable.USERS)
    public int getCount();
}
