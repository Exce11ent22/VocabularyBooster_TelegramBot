package vocabulary.bot.dao.repository_in_memory;

import vocabulary.bot.dao.MostFrequencyWordsRepository;
import vocabulary.bot.dao.entity.MostFrequencyWord;
import vocabulary.bot.tools.FillMostFrequencyWordsRepositoryInMemory;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class MostFrequencyWordsRepositoryInMemory implements MostFrequencyWordsRepository {

  private static MostFrequencyWordsRepositoryInMemory INSTANCE;
  private List<MostFrequencyWord> repository;

  private MostFrequencyWordsRepositoryInMemory() {
    try {
      repository = FillMostFrequencyWordsRepositoryInMemory.fill();
    } catch (IOException e) {
      e.printStackTrace();
      repository = new ArrayList<>();
    }
  }

  public static MostFrequencyWordsRepositoryInMemory getInstance() {
    if (INSTANCE != null) return INSTANCE;
    INSTANCE = new MostFrequencyWordsRepositoryInMemory();
    return INSTANCE;
  }

  @Override
  public void add(MostFrequencyWord word) {
    repository.add(word);
  }

  @Override
  public List<MostFrequencyWord> getAll() {
    return repository;
  }

  @Override
  public MostFrequencyWord get(long id) {
    for (MostFrequencyWord word : repository) {
      if (word.getId() == id) return word;
    }
    return null;
  }

  @Override
  public Long getId(String word) {
    for (MostFrequencyWord mostFrequencyWord : repository) {
      if (mostFrequencyWord.getWord().equals(word)) return mostFrequencyWord.getId();
    }
    return null;
  }
}
