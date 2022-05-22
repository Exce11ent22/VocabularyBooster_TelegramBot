package vocabulary.bot.botapi.handlers;

import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardRemove;
import vocabulary.bot.botapi.BotState;
import vocabulary.bot.botapi.InputMessageHandler;
import vocabulary.bot.service.DictionaryAPI;
import vocabulary.bot.service.Translator;
import vocabulary.bot.service.wordtest.Test;
import vocabulary.bot.service.wordtest.TestNode;
import vocabulary.bot.service.wordtest.TestType;
import vocabulary.bot.tools.ReplyKeyboardBuilder;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

public class TestHandler implements InputMessageHandler {

  private final DictionaryAPI dictionaryAPI = new DictionaryAPI();
  private final List<Test> tests = new ArrayList<>();
  private final int wordsPerTest = 5;
  private final int minWordsForTest = 5;

  @Override
  public SendMessage handle(Message message) {
    SendMessage replyMessage = new SendMessage();
    replyMessage.setChatId(message.getChatId().toString());
    replyMessage.setReplyMarkup(new ReplyKeyboardRemove(true, true));
    Test test = getTest(message.getFrom().getId());

    if (checkExitCommand(message, replyMessage, test)) return replyMessage;
    if (checkUserWordListLimit(message, replyMessage, minWordsForTest)) return replyMessage;

    //проверить существование теста
    if (test == null) {
      tests.add(new Test(message.getFrom().getId(), wordsPerTest, usersAndWordsRepository, mostFrequencyWordsRepository));
      replyMessage.setText("Какой тип теста ты хочешь пройти?");
      ReplyKeyboardBuilder.build(replyMessage, TestType.getNames());
      return replyMessage;
    }

    //выбор типа теста
    if (test.getTestType() == null) {
      TestType testType = TestType.getByName(message.getText());
      if (testType == null) {
        replyMessage.setText("Некорректный ввод, попробуй еще раз");
        ReplyKeyboardBuilder.build(replyMessage, TestType.getNames());
        return replyMessage;
      }
      test.setTestType(testType);
      replyMessage.setText("Выберите количество вопросов");
      ReplyKeyboardBuilder.build(replyMessage, "5", "10", "20");
      return replyMessage;
    }

    //выбор количества вопросов в тесте
    if (test.getTestAmount() == null) {
      int amount;
      try {
        amount = Integer.parseInt(message.getText());
        if (amount < 1 || amount > 20) {
          replyMessage.setText("Вне диапазона!");
          return replyMessage;
        }
      } catch (NumberFormatException e) {
        e.printStackTrace();
        replyMessage.setText("Некорректный ввод, попробуй еще раз");
        ReplyKeyboardBuilder.build(replyMessage, "5", "10", "20");
        return replyMessage;
      }
      test.setTestAmount(amount);
      test.createTest();
    }

    TestNode testNode = test.getCurrentNode();

    //исключительный случай для первого вопроса
    if (test.getTestNodes().get(0).equals(testNode)) {
      createReplyQuestion(replyMessage, testNode);
      test.shift();
      return replyMessage;
    }

    //отметить ответ на предыдущий вопрос
    if (!getAnswer(message, replyMessage, test)) return replyMessage;

    //результат
    if (testNode == null) {
      if (!getAnswer(message, replyMessage, test)) return replyMessage;
      try {
        test.saveResults();
      } catch (Exception e) {
        e.printStackTrace();
      }
      printResult(replyMessage, test);
      userRepository.setBotStateForUser(message.getFrom().getId(), BotState.WAITING);
      tests.remove(test);
      return replyMessage;
    }

    //Вывести следующий вопрос
    createReplyQuestion(replyMessage, testNode);
    test.shift();
    return replyMessage;
  }

  private boolean checkExitCommand(Message message, SendMessage replyMessage, Test test) {
    if (message.getText().equals("/exit")) {
      userRepository.setBotStateForUser(message.getFrom().getId(), BotState.WAITING);
      tests.remove(test);
      replyMessage.setText("Тест досрочно завершен!");
      return true;
    }
    return false;
  }

  private boolean checkUserWordListLimit(Message message, SendMessage replyMessage, int limit) {
    if (usersAndWordsRepository.getUserWordList(message.getFrom().getId()).size() < limit) {
      replyMessage.setText("Чтобы проходить тесты, необходимо сначала выучить несколько слов :)");
      userRepository.setBotStateForUser(message.getFrom().getId(), BotState.WAITING);
      return true;
    }
    return false;
  }

  private Integer readInt(Message message) {
    int n;
    try {
      n = Integer.parseInt(message.getText());
    } catch (NumberFormatException e) {
      e.printStackTrace();
      return null;
    }
    return n;
  }

  private boolean getAnswer(Message message, SendMessage replyMessage, Test test) {
    Integer answer = readInt(message);
    if (answer == null) {
      replyMessage.setText("Некорректный ввод");
      return false;
    }
    if (answer > wordsPerTest || answer < 1) {
      replyMessage.setText("Вне диапазона!");
      return false;
    }
    test.inputAnswer(answer);
    return true;
  }

  private void printResult(SendMessage replyMessage, Test test) {
    StringBuilder stringBuilder = new StringBuilder();
    int correct = 0;
    for (TestNode node : test.getTestNodes()) {
      if (node.isCorrectlyPassed()) {
        correct++;
        stringBuilder.append("+ ");
      } else {
        stringBuilder.append("- ");
      }
      stringBuilder.append(node.getTargetWord().getWord().toUpperCase(Locale.ROOT))
        .append("\n");
    }
    replyMessage.setText("Тест завершен! Результат: " + correct + "/" + test.getTestNodes().size() + "\n" + stringBuilder);
  }

  private Test getTest(long userId) {
    for (Test test : tests) {
      if (test.getUserId() == userId) return test;
    }
    return null;
  }

  private String getTranslate(String word){
    String translate = dictionaryAPI.getTranslate(word);
    if (translate != null) return translate.toUpperCase(Locale.ROOT);

    try {
      translate = Translator.translate("en", "ru", word);
      return translate.toUpperCase(Locale.ROOT);
    } catch (IOException e) {
      e.printStackTrace();
      return "Для слова " + word.toUpperCase(Locale.ROOT) + " не найден перевод...";
    }
  }

  private void createReplyQuestion(SendMessage replyMessage, TestNode testNode) {
    StringBuilder stringBuilder = new StringBuilder(getTranslate(testNode.getTargetWord().getWord()));
    stringBuilder.append("\n\n");
    for (int i = 0; i < testNode.getWordsList().size(); i++) {
      stringBuilder.append(i + 1)
        .append(". ")
        .append(testNode.getOtherWords().get(i).toUpperCase(Locale.ROOT))
        .append("\n");
    }
    replyMessage.setText(stringBuilder.toString());
    String[] indexes = new String[wordsPerTest];
    Arrays.setAll(indexes, i -> Integer.toString(i + 1));
    ReplyKeyboardBuilder.build(replyMessage, indexes);
  }

}
