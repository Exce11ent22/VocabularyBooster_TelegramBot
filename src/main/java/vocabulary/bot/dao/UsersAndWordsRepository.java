package vocabulary.bot.dao;

import vocabulary.bot.dao.entity.UserAndWord;

import java.util.List;

public interface UsersAndWordsRepository {

  void add(long userId, long wordId);

  List<UserAndWord> getUserWordList(long userId);

  void increaseCorrect(long userId, long wordId);

  void increaseWrong(long userId, long wordId);

}
