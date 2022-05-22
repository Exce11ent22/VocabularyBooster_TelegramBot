package vocabulary.bot.botapi;

import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import vocabulary.bot.dao.MostFrequencyWordsRepository;
import vocabulary.bot.dao.UserRepository;
import vocabulary.bot.dao.UsersAndWordsRepository;
import vocabulary.bot.dao.persistence.ConnectionManager;
import vocabulary.bot.dao.repository_in_db.MostFrequencyWordsRepositoryInDB;
import vocabulary.bot.dao.repository_in_db.UserRepositoryInDB;
import vocabulary.bot.dao.repository_in_db.UsersAndWordsRepositoryInDB;

public interface InputMessageHandler {

  UserRepository userRepository = new UserRepositoryInDB(ConnectionManager.getInstance());
  MostFrequencyWordsRepository mostFrequencyWordsRepository = new MostFrequencyWordsRepositoryInDB(ConnectionManager.getInstance());
  UsersAndWordsRepository usersAndWordsRepository = new UsersAndWordsRepositoryInDB(ConnectionManager.getInstance());

  SendMessage handle(Message message);

}
