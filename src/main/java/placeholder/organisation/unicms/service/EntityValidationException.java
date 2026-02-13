package placeholder.organisation.unicms.service;

import lombok.Getter;

@Getter
public class EntityValidationException extends ServiceException {
    private final String entity;
    private final String identifier;

    public EntityValidationException(String message, String entity, String identifier) {
        super(message);
        this.entity = entity;
        this.identifier = identifier;
    }
}