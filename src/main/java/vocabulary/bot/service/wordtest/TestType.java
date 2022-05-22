package vocabulary.bot.service.wordtest;

import lombok.Getter;

public enum TestType {

  LAST("Последние слова"),
  RANDOM("Случайные слова"),
  BAD("Плохо выученные слова");

  @Getter
  private final String name;

  TestType(String name) {
    this.name = name;
  }

  public static TestType getByName(String name) {
    for (TestType testType : TestType.values()) {
      if (testType.getName().equals(name)) return testType;
    }
    return null;
  }

  public static String[] getNames() {
    String[] names = new String[TestType.values().length];
    for (int i = 0; i < names.length; i++) {
      names[i] = TestType.values()[i].getName();
    }
    return names;
  }
}
