package vocabulary.bot.tools;

import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboard;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;

import java.util.ArrayList;
import java.util.List;

public class ReplyKeyboardBuilder {

  public static void build(SendMessage message, String... args) {
    List<KeyboardRow> keyboard = new ArrayList<>();
    for (String arg : args) {
      KeyboardRow row = new KeyboardRow();
      row.add(arg);
      keyboard.add(row);
    }
    ReplyKeyboard replyKeyboard = new ReplyKeyboardMarkup(keyboard);
    message.setReplyMarkup(replyKeyboard);
  }

}
