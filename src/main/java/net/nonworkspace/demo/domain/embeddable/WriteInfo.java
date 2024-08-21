package net.nonworkspace.demo.domain.embeddable;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.Embeddable;
import jakarta.persistence.PrePersist;
import java.time.LocalDateTime;
import lombok.Getter;
import org.springframework.format.annotation.DateTimeFormat;

@Embeddable
@Getter
public class WriteInfo {

    private Long writerId;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Schema(description = "등록일시")
    private LocalDateTime createDate;

    @PrePersist
    protected void onCreate() {
        if (this.createDate == null) {
            this.createDate = LocalDateTime.now();
        }
    }

    public static WriteInfo create(final Long writerId) {
        final WriteInfo writeInfo = new WriteInfo();
        writeInfo.writerId = writerId;
        return writeInfo;
    }
}
