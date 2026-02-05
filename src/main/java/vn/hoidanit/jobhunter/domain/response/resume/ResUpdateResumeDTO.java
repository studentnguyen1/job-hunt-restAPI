package vn.hoidanit.jobhunter.domain.response.resume;

import java.time.Instant;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ResUpdateResumeDTO {
    private Instant updatedAt;
    private String updatedBy;
}
