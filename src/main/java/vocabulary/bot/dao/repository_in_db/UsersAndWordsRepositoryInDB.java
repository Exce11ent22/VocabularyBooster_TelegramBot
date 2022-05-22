package vocabulary.bot.dao.repository_in_db;

import vocabulary.bot.dao.UsersAndWordsRepository;
import vocabulary.bot.dao.entity.UserAndWord;
import vocabulary.bot.dao.persistence.ConnectionManager;
import vocabulary.bot.dao.persistence.Extractor;

import java.sql.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class UsersAndWordsRepositoryInDB implements UsersAndWordsRepository {

  private final ConnectionManager connectionManager;

  public UsersAndWordsRepositoryInDB(ConnectionManager connectionManager) {
    this.connectionManager = connectionManager;
  }

  @Override
  public void add(long userId, long wordId) {
    try {
      Connection connection = connectionManager.getConnection();
      PreparedStatement ps = connection.prepareStatement(
        "insert into user_and_word(user_id, word_id) values (?, ?);"
      );
      ps.setObject(1, userId, Types.INTEGER);
      ps.setObject(2, wordId, Types.INTEGER);
      ps.executeUpdate();
    } catch (SQLException e) {
      e.printStackTrace();
    }
  }

  @Override
  public List<UserAndWord> getUserWordList(long userId) {
    try {
      Connection connection = connectionManager.getConnection();
      Statement statement = connection.createStatement();
      ResultSet resultSet = statement.executeQuery(
        "select * from user_and_word where user_id = " + userId + ";"
      );
      return extractor.extract(resultSet);
    } catch (SQLException e) {
      e.printStackTrace();
    }
    return Collections.emptyList();
  }

  @Override
  public void increaseCorrect(long userId, long wordId) {
    try {
      Connection connection = connectionManager.getConnection();
      Statement statement = connection.createStatement();
      ResultSet resultSet = statement.executeQuery(
        "select * from user_and_word where (user_id = " + userId + " and word_id = " + wordId + ");"
      );
      int correct = 0;
      if (resultSet.next()) {
        correct = resultSet.getInt("correct");
      }
      PreparedStatement ps = connection.prepareStatement(
        "update user_and_word set correct = ? where (user_id = " + userId + " and word_id = " + wordId + ");"
      );
      ps.setObject(1, correct + 1, Types.INTEGER);
      ps.executeUpdate();
    } catch (SQLException e) {
      e.printStackTrace();
    }
  }

  @Override
  public void increaseWrong(long userId, long wordId) {
    try {
      Connection connection = connectionManager.getConnection();
      Statement statement = connection.createStatement();
      ResultSet resultSet = statement.executeQuery(
        "select * from user_and_word where (user_id = " + userId + " and word_id = " + wordId + ");"
      );
      int wrong = 0;
      if (resultSet.next()) {
        wrong = resultSet.getInt("wrong");
      }
      PreparedStatement ps = connection.prepareStatement(
        "update user_and_word set wrong = ? where (user_id = " + userId + " and word_id = " + wordId + ");"
      );
      ps.setObject(1, wrong + 1, Types.INTEGER);
      ps.executeUpdate();
    } catch (SQLException e) {
      e.printStackTrace();
    }
  }

  private final Extractor<UserAndWord> extractor = rs -> {
    List<UserAndWord> userList = new ArrayList<>();
    while (rs.next()) {
      long id = rs.getLong("id");
      long userId = rs.getLong("user_id");
      int wordId = rs.getInt("word_id");
      int correct = rs.getInt("correct");
      int wrong = rs.getInt("wrong");

      UserAndWord userAndWord = new UserAndWord(id, userId, wordId);
      userAndWord.setCorrect(correct);
      userAndWord.setWrong(wrong);

      userList.add(userAndWord);
    }
    return userList;
  };

}
