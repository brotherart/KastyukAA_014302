package com.autodocservice.model;

import com.autodocservice.model.enums.DocumentStatus;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIdentityReference;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@Entity
public class Documents {
    @Setter(AccessLevel.NONE)
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(nullable = false, updatable = false)
    private Long id;

    private String name;
    private int number;
    private String date;
    private String documentScan;
    private String document;
    private String documentReceiver;

    @Enumerated(EnumType.STRING)
    private DocumentStatus status;
    @Column(length = 5000)
    private String description;

    @JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
    @JsonIdentityReference(alwaysAsId = true)
    @ManyToOne(fetch = FetchType.LAZY)
    private Users receiver;
    @JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
    @JsonIdentityReference(alwaysAsId = true)
    @ManyToOne(fetch = FetchType.LAZY)
    private Users owner;
    @JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
    @JsonIdentityReference(alwaysAsId = true)
    @OneToMany(mappedBy = "document", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<DocumentComments> comments;

    public Documents(String name, int number, String date, String documentScan, String document, String description, Users owner) {
        this.name = name;
        this.number = number;
        this.date = date;
        this.documentScan = documentScan;
        this.document = document;
        this.description = description;
        this.status = DocumentStatus.NOT_SIGNED;
        this.receiver = null;
        this.documentReceiver = "";
        this.owner = owner;
    }

    public Documents(String name, int number, String date, String documentScan, String document, String description) {
        this.name = name;
        this.number = number;
        this.date = date;
        this.documentScan = documentScan;
        this.document = document;
        this.description = description;
        this.status = DocumentStatus.NOT_SIGNED;
        this.receiver = null;
        this.documentReceiver = "";
    }

    public void set(String name, int number, String date, String documentScan, String document, String description) {
        this.name = name;
        this.number = number;
        this.date = date;
        this.documentScan = documentScan;
        this.document = document;
        this.description = description;
    }

    public boolean check() {
        receiver = null;
        status = DocumentStatus.NOT_SIGNED;
        documentReceiver = "";
        comments = new ArrayList<>();
        return name.isEmpty() || number == 0 || date.isEmpty() || documentScan.isEmpty() || document.isEmpty() || description.isEmpty();
    }
}
