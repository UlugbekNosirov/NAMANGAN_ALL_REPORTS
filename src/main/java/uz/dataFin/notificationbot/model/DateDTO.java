package uz.dataFin.notificationbot.model;

import lombok.*;
import lombok.experimental.FieldDefaults;

import javax.persistence.Entity;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
@FieldDefaults(level = AccessLevel.PRIVATE)
@Builder
public class DateDTO extends BaseModel {
    String name;
    String clientId;
    Integer reportId;
    String methodType;
    String startDate;
    String endDate;
    String code;
    String typeFile;
    public DateDTO(String firstname, String chatId, Integer reportId, String methodType, String startDate, String endDate, String code) {
        this.name = firstname;
        this.clientId = chatId;
        this.reportId = reportId;
        this.methodType = methodType;
        this.startDate = startDate;
        this.endDate = endDate;
        this.code = code;
    }
}
