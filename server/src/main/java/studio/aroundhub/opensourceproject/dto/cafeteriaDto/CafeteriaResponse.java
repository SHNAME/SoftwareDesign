package studio.aroundhub.opensourceproject.dto.cafeteriaDto;

import lombok.Getter;

@Getter
public class CafeteriaResponse {

    String natural;
    String student;

    public CafeteriaResponse(String natural, String student) {
        this.natural = natural;
        this.student = student;
    }
}
