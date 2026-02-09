package placeholder.organisation.unicms.excpetion;

import lombok.Getter;

@Getter
public class EntityValidationException extends RuntimeException {
    private final String entity;
    private final String identifier;

    public EntityValidationException(String message, String entity, String identifier) {
        super(message);
        this.entity = entity;
        this.identifier = identifier;
    }
}