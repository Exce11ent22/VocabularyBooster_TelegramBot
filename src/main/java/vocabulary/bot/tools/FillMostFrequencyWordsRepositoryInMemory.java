package vocabulary.bot.tools;

import vocabulary.bot.dao.entity.MostFrequencyWord;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class FillMostFrequencyWordsRepositoryInMemory {

  public static List<MostFrequencyWord> fill() throws IOException {
    List<MostFrequencyWord> repository = new ArrayList<>();
    // Open the file
    FileInputStream fstream = new FileInputStream("src/main/resources/top1000.txt");
    BufferedReader br = new BufferedReader(new InputStreamReader(fstream));

    String strLine;

    //Read File Line By Line
    long i = 0;
    while ((strLine = br.readLine()) != null) {
      repository.add(new MostFrequencyWord(++i, strLine.toLowerCase(Locale.ROOT)));
    }

    //Close the input stream
    fstream.close();
    return repository;
  }


}
