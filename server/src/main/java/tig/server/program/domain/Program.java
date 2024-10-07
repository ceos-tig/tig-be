package tig.server.program.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import tig.server.base.BaseTimeEntity;
import tig.server.club.domain.Club;
import tig.server.enums.ProgramEnum;

@Entity
@Getter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Program extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long programId;

    @ManyToOne
    @JoinColumn(name = "club_id", nullable = false)
    private Club club;

    @Column(name = "program_name", nullable = false)
    @Enumerated(EnumType.STRING)
    private ProgramEnum programName;  // ENUM 타입으로 정의된 프로그램 이름
}
