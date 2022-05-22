package vocabulary.bot.botapi.handlers;

import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardRemove;
import vocabulary.bot.botapi.BotState;
import vocabulary.bot.botapi.InputMessageHandler;
import vocabulary.bot.dao.entity.MostFrequencyWord;
import vocabulary.bot.dao.entity.UserAndWord;
import vocabulary.bot.dao.entity.UserAndWordComparatorByCorrectness;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class StatisticHandler implements InputMessageHandler {

  @Override
  public SendMessage handle(Message message) {
    SendMessage replyMessage = new SendMessage();
    replyMessage.setChatId(message.getChatId().toString());
    replyMessage.setReplyMarkup(new ReplyKeyboardRemove(true, true));

    replyMessage.setText(getStatistic(message));
    userRepository.setBotStateForUser(message.getFrom().getId(), BotState.WAITING);
    return replyMessage;
  }

  private String getStatistic(Message message) {
    StringBuilder stringBuilder = new StringBuilder();

    List<UserAndWord> userWordList = usersAndWordsRepository.getUserWordList(message.getFrom().getId());
    List<MostFrequencyWord> mostFrequencyWordList = mostFrequencyWordsRepository.getAll();
    double userWordsSize = userWordList.size();
    double allWordsSize = mostFrequencyWordList.size();
    stringBuilder.append("Курс пройден на ")
      .append(Math.round(userWordsSize / allWordsSize * 100))
      .append("%\n\n");

    List<UserAndWord> withAnswers = new ArrayList<>();
    for (UserAndWord userAndWord : userWordList) {
      if (userAndWord.getCorrect() + userAndWord.getWrong() > 0) withAnswers.add(userAndWord);
    }

    if (withAnswers.size() < 5) return stringBuilder.toString();
    stringBuilder.append("Топ 5 самых сложных слов для тебя:\n");
    withAnswers.sort(new UserAndWordComparatorByCorrectness());

    for (int i = 0; i < 5; i++) {
      stringBuilder.append(i + 1)
        .append(". ")
        .append(mostFrequencyWordsRepository.get(withAnswers.get(i).getWordId()).getWord().toUpperCase(Locale.ROOT));
      stringBuilder.append(" (")
        .append(withAnswers.get(i).getCorrect())
        .append("/")
        .append(withAnswers.get(i).getCorrect() + withAnswers.get(i).getWrong())
        .append(" правильных ответов)\n");
    }

    return stringBuilder.toString();
  }
}
