package com.google.drive.api.entity;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    private Long id;

    @Column(name = "user_name", unique = true)
    private String userName;

    @ManyToMany(cascade = {CascadeType.ALL})
    @JoinTable(
            name = "USER_Document",
            joinColumns = {@JoinColumn(name = "user_id")},
            inverseJoinColumns = {@JoinColumn(name = "document_id")}
    )
    private Set<Document> documents = new HashSet<>();

    public void addDocument(Document document) {
        documents.add(document);
        document.getUsers().add(this);
    }

    public Long getId() {
        return id;
    }


    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }


    public void setId(Long id) {
        this.id = id;
    }

    public Set<Document> getDocuments() {
        return documents;
    }

    public void setDocuments(Set<Document> documents) {
        this.documents = documents;
    }
}
