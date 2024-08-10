package softuni.bg.finalPJ.models.DTOs;

public class MessageDTO {

    private String senderName;
    private String senderEmail;
    private String senderPhone;
    private String content;

    public MessageDTO() {
    }

    public String getSenderName() {
        return senderName;
    }

    public MessageDTO setSenderName(String senderName) {
        this.senderName = senderName;
        return this;
    }

    public String getSenderEmail() {
        return senderEmail;
    }

    public MessageDTO setSenderEmail(String senderEmail) {
        this.senderEmail = senderEmail;
        return this;
    }

    public String getSenderPhone() {
        return senderPhone;
    }

    public MessageDTO setSenderPhone(String senderPhone) {
        this.senderPhone = senderPhone;
        return this;
    }

    public String getContent() {
        return content;
    }

    public MessageDTO setContent(String content) {
        this.content = content;
        return this;
    }
}
