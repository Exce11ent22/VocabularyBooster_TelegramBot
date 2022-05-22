package vocabulary.bot.dao.entity;

import java.util.Comparator;

public class UserAndWordComparatorByCorrectness implements Comparator<UserAndWord> {

  @Override
  public int compare(UserAndWord o1, UserAndWord o2) {
    if (o1.getWrong() == 0 && o2.getWrong() == 0) {
      return o1.getCorrect() - o2.getCorrect();
    }
    if (o1.getWrong() == 0) return 1;
    if (o2.getWrong() == 0) return -1;

    if (o1.getCorrect() == 0 && o2.getCorrect() == 0) {
      return o2.getWrong() - o1.getWrong();
    }
    if (o1.getCorrect() == 0) return -1;
    if (o2.getCorrect() == 0) return 1;

    return o1.getCorrect() * o2.getWrong() - o2.getCorrect() * o1.getWrong();
  }

}
