package cl.cizquierdonov.metrics.dal.model;

/**
 * This enum class defines the kind of date metric averages to calculate by the project.
 */
public enum AverageType {

  MINUTE("Minute"), HOUR("Hour"), DAY("Day");

  private final String value;

  private AverageType(String value) {
    this.value = value;
  }

  public String getValue() {
    return value;
  }

  public static AverageType getInstance(String avgName) {
    for (AverageType dat : values()) {
      if (dat.getValue().equals(avgName)) {
        return dat;
      }
    }

    return null;
  }

  public String toString() {
    return value;
  }

}
