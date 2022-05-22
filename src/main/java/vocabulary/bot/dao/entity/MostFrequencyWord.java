package vocabulary.bot.dao.entity;

import lombok.Getter;

@Getter
public class MostFrequencyWord {

  private final long id;
  private final String word;

  public MostFrequencyWord(long id, String word) {
    this.id = id;
    this.word = word;
  }

}
