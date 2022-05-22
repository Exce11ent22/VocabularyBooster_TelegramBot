package vocabulary.bot.botapi.handlers;

import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import vocabulary.bot.botapi.BotState;
import vocabulary.bot.botapi.InputMessageHandler;
import vocabulary.bot.dao.entity.UserAndWord;
import vocabulary.bot.service.DictionaryAPI;
import vocabulary.bot.service.Translator;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class LearnHandler implements InputMessageHandler {

  private final DictionaryAPI dictionaryAPI = new DictionaryAPI();

  @Override
  public SendMessage handle(Message message) {
    SendMessage replyMessage = new SendMessage();
    replyMessage.setChatId(message.getChatId().toString());
    List<UserAndWord> userWordList = usersAndWordsRepository.getUserWordList(message.getFrom().getId());
    userRepository.setBotStateForUser(message.getFrom().getId(), BotState.WAITING);
    long currentId = userWordList.size() == 0 ? 1 : userWordList.get(userWordList.size() - 1).getWordId() + 1;

    String word = mostFrequencyWordsRepository.get(currentId).getWord();
    usersAndWordsRepository.add(message.getFrom().getId(), currentId);

    String replyInfo = getInfo(word);

    replyMessage.setText(replyInfo);

    return replyMessage;
  }

  private String getInfo(String word) {
    String replyInfo = dictionaryAPI.getWordInfo(word);
    if (replyInfo != null) return replyInfo;

    try {
      String translate = Translator.translate("en", "ru", word);
      replyInfo = word.toUpperCase(Locale.ROOT) + " - " + translate.toUpperCase(Locale.ROOT);
      return replyInfo;
    } catch (IOException e) {
      e.printStackTrace();
      return "Что-то пошло не так...";
    }
  }

}
