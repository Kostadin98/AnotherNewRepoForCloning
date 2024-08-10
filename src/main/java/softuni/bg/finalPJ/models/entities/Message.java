package softuni.bg.finalPJ.models.entities;

import jakarta.persistence.*;

@Entity
public class Message {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String senderName;
    private String senderEmail;
    private String senderPhone;
    private String content;
    private String formattedDate;

    @ManyToOne
    @JoinColumn(name = "receiver_id")
    private UserEntity receiver;



    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getSenderName() {
        return senderName;
    }

    public void setSenderName(String senderName) {
        this.senderName = senderName;
    }

    public String getSenderEmail() {
        return senderEmail;
    }

    public void setSenderEmail(String senderEmail) {
        this.senderEmail = senderEmail;
    }

    public String getSenderPhone() {
        return senderPhone;
    }

    public Message setSenderPhone(String senderPhone) {
        this.senderPhone = senderPhone;
        return this;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getFormattedDate() {
        return formattedDate;
    }

    public Message setFormattedDate(String formattedDate) {
        this.formattedDate = formattedDate;
        return this;
    }

    public UserEntity getReceiver() {
        return receiver;
    }

    public void setReceiver(UserEntity receiver) {
        this.receiver = receiver;
    }
}
