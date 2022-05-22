package vocabulary.bot.dao.repository_in_db;

import vocabulary.bot.dao.MostFrequencyWordsRepository;
import vocabulary.bot.dao.entity.MostFrequencyWord;
import vocabulary.bot.dao.persistence.ConnectionManager;
import vocabulary.bot.dao.persistence.Extractor;

import java.sql.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class MostFrequencyWordsRepositoryInDB implements MostFrequencyWordsRepository {

  private final ConnectionManager connectionManager;

  public MostFrequencyWordsRepositoryInDB(ConnectionManager connectionManager) {
    this.connectionManager = connectionManager;
  }

  @Override
  public void add(MostFrequencyWord word) {
    try {
      Connection connection = connectionManager.getConnection();
      PreparedStatement ps = connection.prepareStatement(
        "insert into most_frequency_word(word) values (?)"
      );
      ps.setObject(1, word.getWord(), Types.VARCHAR);
      ps.execute();
    } catch (SQLException e) {
      e.printStackTrace();
    }
  }

  @Override
  public List<MostFrequencyWord> getAll() {
    try {
      Connection connection = connectionManager.getConnection();
      Statement statement = connection.createStatement();
      ResultSet resultSet = statement.executeQuery("select * from most_frequency_word;");
      return extractor.extract(resultSet);
    } catch (SQLException e) {
      e.printStackTrace();
    }
    return Collections.emptyList();
  }

  @Override
  public MostFrequencyWord get(long id) {
    try {
      Connection connection = connectionManager.getConnection();
      Statement statement = connection.createStatement();
      ResultSet resultSet = statement.executeQuery("select * from most_frequency_word" +
        " where id = " + id + ";");
      if (resultSet.next()) {
        return new MostFrequencyWord(resultSet.getLong("id"), resultSet.getString("word"));
      }
    } catch (SQLException e) {
      e.printStackTrace();
    }
    return null;
  }

  @Override
  public Long getId(String word) {
    try {
      Connection connection = connectionManager.getConnection();
      Statement statement = connection.createStatement();
      ResultSet resultSet = statement.executeQuery("select * from most_frequency_word" +
        " where word = '" + word + "';");
      if (resultSet.next()) {
        return resultSet.getLong("id");
      }
    } catch (SQLException e) {
      e.printStackTrace();
    }
    return null;
  }

  private final Extractor<MostFrequencyWord> extractor = rs -> {
    List<MostFrequencyWord> mostFrequencyWordList = new ArrayList<>();
    while (rs.next()) {
      long wordId = rs.getLong("id");
      String word = rs.getString("word");
      mostFrequencyWordList.add(new MostFrequencyWord(wordId, word));
    }
    return mostFrequencyWordList;
  };
}
