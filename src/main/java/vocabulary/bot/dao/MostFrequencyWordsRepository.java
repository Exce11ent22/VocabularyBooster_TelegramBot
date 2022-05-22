package vocabulary.bot.dao;

import vocabulary.bot.dao.entity.MostFrequencyWord;

import java.util.List;

public interface MostFrequencyWordsRepository {

  void add(MostFrequencyWord word);

  List<MostFrequencyWord> getAll();

  MostFrequencyWord get(long id);

  Long getId(String word);

}
