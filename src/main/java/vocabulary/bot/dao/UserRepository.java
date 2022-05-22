package vocabulary.bot.dao;

import vocabulary.bot.botapi.BotState;
import vocabulary.bot.dao.entity.User;

public interface UserRepository {

  void setBotStateForUser(long userId, BotState botState);

  BotState getBotStateOfUser(long userId);

  User getUser(long userId);

}
