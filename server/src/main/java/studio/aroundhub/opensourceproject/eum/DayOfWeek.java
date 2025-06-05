package studio.aroundhub.opensourceproject.eum;

import lombok.Getter;

@Getter
public enum DayOfWeek {
    MON("Monday"),
    TUE("Tuesday"),
    WED("Wednesday"),
    THU("Thursday"),
    FRI("Friday"),
    SAT("Saturday"),
    SUN("Sunday");
    private final String fullName;

    DayOfWeek(String fullName) {
        this.fullName = fullName;
    }
}
