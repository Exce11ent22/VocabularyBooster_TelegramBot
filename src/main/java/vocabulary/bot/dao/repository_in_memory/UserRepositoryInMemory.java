package vocabulary.bot.dao.repository_in_memory;

import vocabulary.bot.botapi.BotState;
import vocabulary.bot.dao.UserRepository;
import vocabulary.bot.dao.entity.User;

import java.util.ArrayList;
import java.util.List;

public class UserRepositoryInMemory implements UserRepository {

  private static UserRepositoryInMemory INSTANCE = null;
  private final List<User> users = new ArrayList<>();

  private UserRepositoryInMemory() {
  }

  public static UserRepositoryInMemory getInstance() {
    if (INSTANCE != null) return INSTANCE;
    INSTANCE = new UserRepositoryInMemory();
    return INSTANCE;
  }

  @Override
  public void setBotStateForUser(long userId, BotState botState) {
    for (User user : users) {
      if (user.getUserId() == userId) {
        user.setBotState(botState.getBotStateId());
        return;
      }
    }
    users.add(new User(userId, botState.getBotStateId()));
  }

  @Override
  public BotState getBotStateOfUser(long userId) {
    for (User user : users) {
      if (user.getUserId() == userId) return BotState.getBotStateById(user.getBotState());
    }
    BotState currentState = BotState.WAITING;
    users.add(new User(userId, currentState.getBotStateId()));
    return currentState;
  }

  @Override
  public User getUser(long userId) {
    for (User user : users) {
      if (user.getUserId() == userId) return user;
    }
    return null;
  }
}
