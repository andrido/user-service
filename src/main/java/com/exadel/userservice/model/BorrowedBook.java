package com.exadel.userservice.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Table
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class BorrowedBook {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long bookId;
    private String bookTitle;

    @Enumerated(EnumType.STRING)
    private BorrowedStatus status;

    private LocalDateTime eventAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id") // FK

    @JsonIgnore
    private User user;
}
