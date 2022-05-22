package vocabulary.bot.botapi.handlers;

import org.telegram.telegrambots.meta.api.methods.ParseMode;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardRemove;
import vocabulary.bot.botapi.BotState;
import vocabulary.bot.botapi.InputMessageHandler;
import vocabulary.bot.service.Translator;

import java.io.IOException;
import java.util.Objects;

public class TranslateHandler implements InputMessageHandler {

  @Override
  public SendMessage handle(Message message) {
    SendMessage replyMessage = new SendMessage();
    replyMessage.setReplyMarkup(new ReplyKeyboardRemove(true, true));
    replyMessage.setParseMode(ParseMode.HTML);
    replyMessage.setChatId(message.getChatId().toString());
    if (Objects.equals(message.getText(), BotState.TRANSLATE.getActivationKey())) {
      replyMessage.setText("Напиши что ты хочешь перевести. Перевод работает с английского на русский.");
      return replyMessage;
    }
    try {
      replyMessage.setText(Translator.translate("en", "ru", message.getText()));
    } catch (IOException e) {
      e.printStackTrace();
      replyMessage.setText("Что-то не так :(");
    }
    userRepository.setBotStateForUser(message.getFrom().getId(), BotState.WAITING);
    return replyMessage;
  }
}
