package placeholder.organisation.unicms.service.datagenerator;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import placeholder.organisation.unicms.entity.Address;
import placeholder.organisation.unicms.service.AddressService;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class AddressGeneratorTest {

    @Mock
    private AddressService addressService;

    @InjectMocks
    private AddressGenerator addressGenerator;

    @Test
    void generate_invokesCreateAddressExactlyFiftyTimes_whenAmountIsFifty() {
        int amount = 50;
        addressGenerator.generate(amount);

        verify(addressService, times(50)).createAddress(any(Address.class));
    }

    @Test
    void generate_createsMinimumTenAddresses_whenAmountIsTooLow() {
        int amount = 5;
        addressGenerator.generate(amount);
        verify(addressService, times(10)).createAddress(any(Address.class));
    }

    @Test
    void generate_populatesAllRequiredFields_whenExecutingBatch() {
        int amount = 10;
        ArgumentCaptor<Address> addressCaptor = ArgumentCaptor.forClass(Address.class);

        addressGenerator.generate(amount);

        verify(addressService, atLeastOnce()).createAddress(addressCaptor.capture());
        Address captured = addressCaptor.getValue();

        assertNotNull(captured.getCity());
        assertNotNull(captured.getStreet());
        assertNotNull(captured.getCountry());
        assertNotNull(captured.getHouseNumber());
        assertNotNull(captured.getPostalCode());
    }

    @Test
    void generate_distributesAddressesAcrossAllCountries_whenAmountIsSufficient() {
        int amount = 10;
        ArgumentCaptor<Address> addressCaptor = ArgumentCaptor.forClass(Address.class);

        addressGenerator.generate(amount);

        verify(addressService, times(10)).createAddress(addressCaptor.capture());
        long distinctCountries = addressCaptor.getAllValues().stream()
                .map(Address::getCountry)
                .distinct()
                .count();

        assertEquals(5, distinctCountries);
    }
}