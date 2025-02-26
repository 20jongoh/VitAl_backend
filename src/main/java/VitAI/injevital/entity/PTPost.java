package VitAI.injevital.entity;

import VitAI.injevital.enumSet.PTStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "pt_posts")
public class PTPost extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    private String title;

    @Column(columnDefinition = "TEXT")
    private String content;

    private String location;

    private Integer price;

    @ElementCollection
    @CollectionTable(name = "pt_post_images")
    private List<String> images;

    private String specialty;

    @Enumerated(EnumType.STRING)
    private PTStatus status;

    public void updatePost(String title, String content, String location,
                           Integer price, String specialty) {
        this.title = title;
        this.content = content;
        this.location = location;
        this.price = price;
        this.specialty = specialty;
    }
}