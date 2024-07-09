package net.nonworkspace.demo.domain.embeddable;

import java.time.LocalDateTime;

import org.springframework.format.annotation.DateTimeFormat;

import com.fasterxml.jackson.annotation.JsonFormat;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.Embeddable;
import jakarta.persistence.PrePersist;
import lombok.Getter;

@Embeddable
@Getter
public class CreateInfo {

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
}
