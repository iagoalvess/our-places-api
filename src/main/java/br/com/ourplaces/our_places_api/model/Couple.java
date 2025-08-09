package br.com.ourplaces.our_places_api.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "couples")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Couple {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name = "user1_id", referencedColumnName = "id")
    private User user1;

    @OneToOne
    @JoinColumn(name = "user2_id", referencedColumnName = "id")
    private User user2;

    @Column(unique = true)
    private String inviteCode;

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "couple_important_dates", joinColumns = @JoinColumn(name = "couple_id"))
    @OrderColumn(name = "date_order")
    private List<ImportantDate> importantDates = new ArrayList<>();

    @Lob
    @Basic(fetch = FetchType.LAZY)
    private byte[] couplePicture;

    private String pictureMediaType;
}
