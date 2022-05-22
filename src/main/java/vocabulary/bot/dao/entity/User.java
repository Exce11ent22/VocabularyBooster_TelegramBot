package vocabulary.bot.dao.entity;

import lombok.Getter;
import lombok.Setter;

@Getter
public class User {

  private final long userId;
  @Setter
  private int botState;

  public User(long userId, int botState) {
    this.userId = userId;
    this.botState = botState;
  }
}
