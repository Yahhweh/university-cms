package placeholder.organisation.unicms.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import placeholder.organisation.unicms.entity.Address;

@Repository
public interface AddressJpa extends JpaRepository<Address, Long> {
}

