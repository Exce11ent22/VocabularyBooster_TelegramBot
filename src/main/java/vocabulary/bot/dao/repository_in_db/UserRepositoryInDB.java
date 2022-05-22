package vocabulary.bot.dao.repository_in_db;

import vocabulary.bot.botapi.BotState;
import vocabulary.bot.dao.UserRepository;
import vocabulary.bot.dao.entity.User;
import vocabulary.bot.dao.persistence.ConnectionManager;

import java.sql.*;

public class UserRepositoryInDB implements UserRepository {

  private final ConnectionManager connectionManager;

  public UserRepositoryInDB(ConnectionManager connectionManager) {
    this.connectionManager = connectionManager;
  }

  @Override
  public void setBotStateForUser(long userId, BotState botState) {
    try {
      Connection connection = connectionManager.getConnection();
      User user = getUser(userId);
      PreparedStatement ps;
      if (user == null) {
        ps = connection.prepareStatement(
          "insert into \"user\"(id, bot_state) values (?, ?);"
        );
        ps.setObject(1, userId, Types.INTEGER);
        ps.setObject(2, botState.getBotStateId(), Types.INTEGER);
      } else {
        ps = connection.prepareStatement(
          "update \"user\" set bot_state = ? where id = " + userId + ";"
        );
        ps.setObject(1, botState.getBotStateId(), Types.INTEGER);
      }
      ps.executeUpdate();
    } catch (SQLException e) {
      e.printStackTrace();
    }
  }

  @Override
  public BotState getBotStateOfUser(long userId) {
    try {
      Connection connection = connectionManager.getConnection();
      User user = getUser(userId);
      if (user == null) {
        setBotStateForUser(userId, BotState.WAITING);
        return BotState.WAITING;
      }
      Statement statement = connection.createStatement();
      ResultSet resultSet = statement.executeQuery(
        "select * from \"user\" where id = " + userId + ";"
      );
      if (resultSet.next()) {
        return BotState.getBotStateById(resultSet.getInt("bot_state"));
      }
    } catch (SQLException e) {
      e.printStackTrace();
    }
    return null;
  }

  @Override
  public User getUser(long userId) {
    try {
      Connection connection = connectionManager.getConnection();
      Statement statement = connection.createStatement();
      ResultSet resultSet = statement.executeQuery("select * from \"user\" " +
        "where id = " + userId + ";");
      if (resultSet.next()) {
        return new User(resultSet.getLong("id"), resultSet.getInt("bot_state"));
      }
    } catch (SQLException e) {
      e.printStackTrace();
    }
    return null;
  }

}
