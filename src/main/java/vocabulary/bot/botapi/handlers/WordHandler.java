package vocabulary.bot.botapi.handlers;

import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardRemove;
import vocabulary.bot.botapi.BotState;
import vocabulary.bot.botapi.InputMessageHandler;
import vocabulary.bot.service.DictionaryAPI;
import vocabulary.bot.service.Translator;

import java.io.IOException;
import java.util.Locale;

public class WordHandler implements InputMessageHandler {

  private final DictionaryAPI dictionaryAPI = new DictionaryAPI();

  @Override
  public SendMessage handle(Message message) {
    SendMessage replyMessage = new SendMessage();
    replyMessage.setChatId(message.getChatId().toString());
    replyMessage.setReplyMarkup(new ReplyKeyboardRemove(true, true));

    replyMessage.setText(getInfo(message.getText()));

    userRepository.setBotStateForUser(message.getFrom().getId(), BotState.WAITING);
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
      return "Для слова " + word.toUpperCase(Locale.ROOT) + " не найдено информации.";
    }
  }

}
