package vocabulary.bot.botapi.handlers;

import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardRemove;
import vocabulary.bot.botapi.BotState;
import vocabulary.bot.botapi.InputMessageHandler;

public class UndefinedHandler implements InputMessageHandler {
  @Override
  public SendMessage handle(Message message) {
    SendMessage replyMessage = new SendMessage();
    replyMessage.setChatId(message.getChatId().toString());
    replyMessage.setReplyMarkup(new ReplyKeyboardRemove(true, true));

    replyMessage.setText("Такой команды нет :(");
    userRepository.setBotStateForUser(message.getFrom().getId(), BotState.WAITING);
    return replyMessage;
  }
}
