package placeholder.organisation.unicms.service;

import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import placeholder.organisation.unicms.dao.AddressDao;
import placeholder.organisation.unicms.entity.Address;

import java.util.List;
import java.util.Optional;

@Service
@Log4j2
@Transactional(readOnly = true)
public class AddressService {

    AddressDao addressDao;

    public AddressService(AddressDao addressDao) {
        this.addressDao = addressDao;
    }

    public List<Address> findAll(){
        List<Address> addresses =  addressDao.findAll();
        log.debug("Found {} addresses ", addresses.size());
        return addresses;
    }

    @Transactional
    public void createAddress(Address address){
        addressDao.save(address);
        log.debug("Address saved successfully. City: {}}", address.getCity());
    }

    public Optional<Address> findAddress(long id){
        Optional<Address> address = addressDao.findById(id);
        address.ifPresent(value -> log.debug("Found address {}",value));
        return address;
    }
}
