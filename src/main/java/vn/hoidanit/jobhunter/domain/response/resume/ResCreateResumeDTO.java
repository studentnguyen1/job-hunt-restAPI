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
public class ResCreateResumeDTO {
    private long id;
    private Instant createdAt;
    private String createBy;
}
