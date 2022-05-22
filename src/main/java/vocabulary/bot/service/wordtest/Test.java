package vocabulary.bot.service.wordtest;

import lombok.Getter;
import lombok.Setter;
import vocabulary.bot.dao.MostFrequencyWordsRepository;
import vocabulary.bot.dao.UsersAndWordsRepository;
import vocabulary.bot.dao.entity.MostFrequencyWord;
import vocabulary.bot.dao.entity.UserAndWord;
import vocabulary.bot.dao.entity.UserAndWordComparatorByCorrectness;

import java.util.*;

public class Test {

  @Getter
  private final List<TestNode> testNodes = new ArrayList<>();
  @Getter
  private final long userId;
  @Getter
  @Setter
  private TestType testType = null;
  @Getter
  @Setter
  private Integer testAmount = null;
  private final int wordsPerTest;
  private final UsersAndWordsRepository usersAndWordsRepository;
  private final MostFrequencyWordsRepository mostFrequencyWordsRepository;
  private int currentNode = 0;

  public Test(long userId, int wordsPerTest, UsersAndWordsRepository usersAndWordsRepository, MostFrequencyWordsRepository mostFrequencyWordsRepository) {
    this.userId = userId;
    this.wordsPerTest = wordsPerTest;
    this.usersAndWordsRepository = usersAndWordsRepository;
    this.mostFrequencyWordsRepository = mostFrequencyWordsRepository;
  }

  public void saveResults() throws Exception {
    for (TestNode testNode : testNodes) {
      if (!testNode.isPassed()) throw new Exception("Test not completed!");
    }
    for (TestNode testNode : testNodes) {
      if (testNode.isCorrectlyPassed()) {
        usersAndWordsRepository.increaseCorrect(userId, testNode.getTargetWord().getId());
      } else {
        usersAndWordsRepository.increaseWrong(userId, testNode.getTargetWord().getId());
      }
    }
  }

  public TestNode getCurrentNode() {
    if (currentNode >= testNodes.size()) return null;
    return testNodes.get(currentNode);
  }

  public void shift() {
    currentNode++;
  }

  public void inputAnswer(int answer) {
    testNodes.get(currentNode - 1).check(answer);
  }

  public void createTest() {
    Random rand = new Random();
    List<MostFrequencyWord> allWords = mostFrequencyWordsRepository.getAll();
    List<MostFrequencyWord> targetWords = new ArrayList<>();
    if (testType == TestType.LAST) targetWords = getLastTargetWords();
    if (testType == TestType.RANDOM) targetWords = getRandomTargetWords();
    if (testType == TestType.BAD) targetWords = getBadTargetWords();
    for (MostFrequencyWord targetWord : targetWords) {
      List<String> otherWords = new ArrayList<>();
      for (int i = 0; i < wordsPerTest - 1; i++) {
        String otherWord = allWords.get(rand.nextInt(allWords.size())).getWord();
        if (otherWord.equals(targetWord.getWord())) {
          i--;
        } else {
          otherWords.add(otherWord);
        }
      }
      testNodes.add(new TestNode(targetWord, otherWords));
    }
  }

  private List<MostFrequencyWord> getLastTargetWords() {
    List<UserAndWord> userWordList = usersAndWordsRepository.getUserWordList(userId);
    Collections.reverse(userWordList);
    return getFirstN(userWordList, testAmount);
  }

  private List<MostFrequencyWord> getRandomTargetWords() {
    List<UserAndWord> userWordList = usersAndWordsRepository.getUserWordList(userId);
    Collections.shuffle(userWordList);
    return getFirstN(userWordList, testAmount);
  }

  private List<MostFrequencyWord> getBadTargetWords() {
    List<UserAndWord> userWordList = usersAndWordsRepository.getUserWordList(userId);
    userWordList.sort(new UserAndWordComparatorByCorrectness());
    return getFirstN(userWordList, testAmount);
  }

  private List<MostFrequencyWord> getFirstN(List<UserAndWord> list, int n) {
    List<MostFrequencyWord> result = new ArrayList<>();
    int amount = Math.min(list.size(), n);
    for (int i = 0; i < amount; i++) result.add(mostFrequencyWordsRepository.get(list.get(i).getWordId()));
    return result;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    Test test = (Test) o;
    return userId == test.userId && wordsPerTest == test.wordsPerTest && testNodes.equals(test.testNodes) && testType == test.testType && testAmount.equals(test.testAmount) && usersAndWordsRepository.equals(test.usersAndWordsRepository) && mostFrequencyWordsRepository.equals(test.mostFrequencyWordsRepository);
  }

  @Override
  public int hashCode() {
    return Objects.hash(testNodes, userId, testType, testAmount, wordsPerTest, usersAndWordsRepository, mostFrequencyWordsRepository);
  }
}
