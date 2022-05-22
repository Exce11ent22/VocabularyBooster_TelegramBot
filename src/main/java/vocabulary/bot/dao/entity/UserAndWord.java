package vocabulary.bot.dao.entity;

import lombok.Getter;
import lombok.Setter;

@Getter
public class UserAndWord {

  private final long id;
  private final long userId;
  private final long wordId;
  @Setter
  private int correct = 0;
  @Setter
  private int wrong = 0;

  public UserAndWord(long id, long userId, long wordId) {
    this.id = id;
    this.userId = userId;
    this.wordId = wordId;
  }
}
