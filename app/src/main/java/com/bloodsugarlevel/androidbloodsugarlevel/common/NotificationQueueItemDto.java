package com.bloodsugarlevel.androidbloodsugarlevel.common;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class NotificationQueueItemDto {
        
    public static enum NotificationStatus {
        NEW,
        SENT,
        REJECTED,
        ERROR,
        ONHOLD;
    }
    
    public static interface TemplateEnum {
        public String getId();
    }
    
    public static interface TemplateCodeEnum {
        public String getCode();
    }
    
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Recipient {
        
        private String emailAddress;
        
        private String phone;
        
        private String recipientId;
        
        public String getEmailAddress() {
            return emailAddress;
        }

        public void setEmailAddress(String emailAddress) {
            this.emailAddress = emailAddress;
        }

        public String getPhone() {
            return phone;
        }

        public void setPhone(String phone) {
            this.phone = phone;
        }

        public String getRecipientId() {
            return recipientId;
        }

        public void setRecipientId(String recipientId) {
            this.recipientId = recipientId;
        }
        
    }
    
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class NotificationTemplate {
        
        private String templateId;
           
        private Map<String, Object> data;

        public String getTemplateId() {
            return templateId;
        }

        public void setTemplateId(String templateId) {
            this.templateId = templateId;
        }

        public Map<String, Object> getData() {
            return data;
        }

        public void setData(Map<String, Object> data) {
            this.data = data;
        }
        
        @Override
        public boolean equals(Object obj) {
            if (obj instanceof TemplateEnum) {
                return ((TemplateEnum)obj).getId().equals(templateId);
            }
            else {
                return super.equals(obj);
            }
        }
    }
    
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Notification {
        
        private String content;
        
        private Recipient recipient;
        
        private String objectId;
        
        private NotificationTemplate template;
        
        public String getContent() {
            return content;
        }

        public void setContent(String content) {
            this.content = content;
        }

        public Recipient getRecipient() {
            return recipient;
        }

        public void setRecipient(Recipient recipient) {
            this.recipient = recipient;
        }

        public String getObjectId() {
            return objectId;
        }

        public void setObjectId(String objectId) {
            this.objectId = objectId;
        }

        public NotificationTemplate getTemplate() {
            return template;
        }

        public void setTemplate(NotificationTemplate template) {
            this.template = template;
        }
        
    }

    private Long queueId;
    
    private Long senderId;
    
    private String messageId;
    
    private Date created;
    
    private Date processDate;
    
    private NotificationStatus status;
    
    private String errorDetails;
    
    private Notification notification;

    public Long getQueueId() {
        return queueId;
    }

    public void setQueueId(Long queueId) {
        this.queueId = queueId;
    }

    public Long getSenderId() {
        return senderId;
    }

    public void setSenderId(Long senderId) {
        this.senderId = senderId;
    }

    public String getMessageId() {
        return messageId;
    }

    public void setMessageId(String messageId) {
        this.messageId = messageId;
    }

    public Date getCreated() {
        return created;
    }

    public void setCreated(Date created) {
        this.created = created;
    }

    public Date getProcessDate() {
        return processDate;
    }

    public void setProcessDate(Date processDate) {
        this.processDate = processDate;
    }

    public NotificationStatus getStatus() {
        return status;
    }

    public void setStatus(NotificationStatus status) {
        this.status = status;
    }

    public String getErrorDetails() {
        return errorDetails;
    }

    public void setErrorDetails(String errorDetails) {
        this.errorDetails = errorDetails;
    }

    public Notification getNotification() {
        return notification;
    }

    public void setNotification(Notification notification) {
        this.notification = notification;
    }
    
    public boolean isTemplateMatch(TemplateEnum template) {
        return getNotification().getTemplate().equals(template);
    }
    
    public Map<String, Object> getDataAsMap() {
        Map<String, Object> data = this.getNotification().getTemplate().getData();
        return data;
    }

    @SuppressWarnings("unchecked")
    public <T> T getData(TemplateCodeEnum templateCode){
        Map<String, Object> data = getDataAsMap();
        Object object = data.get(templateCode.getCode());
        return (T)object;
    }

    public String getHash(TemplateCodeEnum templateCode) {
        String link = getData(templateCode);
        String hash = link.substring(link.lastIndexOf("/") + 1);
        return hash;
    }
    
    public static List<NotificationQueueItemDto> filterByTemplate(List<NotificationQueueItemDto> list, TemplateEnum template) {
        List<NotificationQueueItemDto> result = new ArrayList<>();
        for(NotificationQueueItemDto item : list) {
            if( item.isTemplateMatch(template)) {
                result.add(item);
            }
        }
        return result;
    }
}
