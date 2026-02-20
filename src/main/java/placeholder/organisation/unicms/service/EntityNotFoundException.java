package placeholder.organisation.unicms.service;

import lombok.Getter;

@Getter
public class EntityNotFoundException extends ServiceException {
    private final Class<?> entity;
    private final String identifier;

    public EntityNotFoundException(Class<?> entity, String identifier) {
        super(entity + " not found with identifier: " + identifier);
        this.entity = entity;
        this.identifier = identifier;
    }
}