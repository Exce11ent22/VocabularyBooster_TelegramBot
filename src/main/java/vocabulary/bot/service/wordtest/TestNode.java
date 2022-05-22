package vocabulary.bot.service.wordtest;

import lombok.Getter;
import vocabulary.bot.dao.entity.MostFrequencyWord;

import java.util.Collections;
import java.util.List;
import java.util.Objects;

@Getter
public class TestNode {

  private final MostFrequencyWord targetWord;
  private final List<String> otherWords;
  private final List<String> wordsList;
  private boolean passed;
  private boolean correctlyPassed;

  public TestNode(MostFrequencyWord targetWord, List<String> otherWords) {
    this.targetWord = targetWord;
    this.otherWords = otherWords;
    this.wordsList = otherWords;
    this.wordsList.add(targetWord.getWord());
    Collections.shuffle(wordsList);
  }

  public boolean check(int answer) {
    answer--;
    correctlyPassed = wordsList.get(answer).equals(targetWord.getWord());
    passed = true;
    return correctlyPassed;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    TestNode testNode = (TestNode) o;
    return passed == testNode.passed && correctlyPassed == testNode.correctlyPassed && targetWord.equals(testNode.targetWord) && otherWords.equals(testNode.otherWords) && wordsList.equals(testNode.wordsList);
  }

  @Override
  public int hashCode() {
    return Objects.hash(targetWord, otherWords, wordsList, passed, correctlyPassed);
  }
}
