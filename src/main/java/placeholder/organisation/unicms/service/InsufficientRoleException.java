package placeholder.organisation.unicms.service;

public class InsufficientRoleException extends ServiceException {
    private final Class<?> entity;
    private final String identifier;

    public InsufficientRoleException(Class<?> entity, String identifier) {
        super(entity.getSimpleName() + " does not have the required role: " + identifier);
        this.entity = entity;
        this.identifier = identifier;
    }
}