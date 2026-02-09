package placeholder.organisation.unicms.excpetion;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;


@Getter
public class EntityNotFoundException extends RuntimeException{
    private final Class<?> Entity;
    private final String Identifier;

    public EntityNotFoundException(Class<?> entity, String identifier) {
        super(entity + " not found with name identifier: " + identifier);
        this.Entity = entity;
        this.Identifier = identifier;
    }
}
