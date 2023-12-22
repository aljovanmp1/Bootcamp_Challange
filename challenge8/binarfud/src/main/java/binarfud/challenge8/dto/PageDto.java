package binarfud.challenge8.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@EqualsAndHashCode
public class PageDto {
    @NotNull
    @Min(1)
    private int selectedPage;
    @NotNull
    @Min(1)
    private int dataPerPage;
}
