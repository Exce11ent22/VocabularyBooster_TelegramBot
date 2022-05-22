package vocabulary.bot.dao.repository_in_memory;

import vocabulary.bot.dao.UsersAndWordsRepository;
import vocabulary.bot.dao.entity.UserAndWord;

import java.util.ArrayList;
import java.util.List;

public class UsersAndWordsRepositoryInMemory implements UsersAndWordsRepository {

  private static UsersAndWordsRepositoryInMemory INSTANCE;
  private final List<UserAndWord> repository = new ArrayList<>();
  private long id = 1;

  private UsersAndWordsRepositoryInMemory() {
  }

  public static UsersAndWordsRepositoryInMemory getInstance() {
    if (INSTANCE != null) return INSTANCE;
    INSTANCE = new UsersAndWordsRepositoryInMemory();
    return INSTANCE;
  }

  @Override
  public void add(long userId, long wordId) {
    repository.add(new UserAndWord(id++, userId, wordId));
  }

  @Override
  public List<UserAndWord> getUserWordList(long userId) {
    List<UserAndWord> result = new ArrayList<>();
    for (UserAndWord userAndWord : repository) {
      if (userAndWord.getUserId() == userId) result.add(userAndWord);
    }
    return result;
  }

  @Override
  public void increaseCorrect(long userId, long wordId) {
    for (UserAndWord userAndWord : repository) {
      if (userAndWord.getWordId() == wordId && userAndWord.getUserId() == userId) {
        userAndWord.setCorrect(userAndWord.getCorrect() + 1);
      }
    }
  }

  @Override
  public void increaseWrong(long userId, long wordId) {
    for (UserAndWord userAndWord : repository) {
      if (userAndWord.getWordId() == wordId && userAndWord.getUserId() == userId) {
        userAndWord.setWrong(userAndWord.getWrong() + 1);
      }
    }
  }


}
