package vocabulary.bot.botapi.handlers;

import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardRemove;
import vocabulary.bot.botapi.BotState;
import vocabulary.bot.botapi.InputMessageHandler;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;

public class InfoHandler implements InputMessageHandler {
  @Override
  public SendMessage handle(Message message) {
    SendMessage replyMessage = new SendMessage();
    replyMessage.setChatId(message.getChatId().toString());
    replyMessage.setReplyMarkup(new ReplyKeyboardRemove(true, true));
    try {
      String content = Files.readString(Paths.get("src/main/resources/info.txt"), StandardCharsets.UTF_8);
      replyMessage.setText(content);
    } catch (IOException e) {
      e.printStackTrace();
    }

    userRepository.setBotStateForUser(message.getFrom().getId(), BotState.WAITING);
    return replyMessage;
  }
}
