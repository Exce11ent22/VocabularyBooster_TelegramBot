package vocabulary.bot;

import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;
import vocabulary.bot.botapi.TelegramFacade;
import vocabulary.bot.dao.UserRepository;
import vocabulary.bot.dao.persistence.ConnectionManager;
import vocabulary.bot.dao.repository_in_db.UserRepositoryInDB;

public class Main {

  public static void main(String[] args) {
    UserRepository userRepository = new UserRepositoryInDB(ConnectionManager.getInstance());
    TelegramFacade telegramFacade = new TelegramFacade(userRepository);
    try {
      TelegramBotsApi telegramBotsApi = new TelegramBotsApi(DefaultBotSession.class);
      telegramBotsApi.registerBot(new VocabularyBoosterBot(telegramFacade));
    } catch (TelegramApiException e) {
      e.printStackTrace();
    }
  }

}
