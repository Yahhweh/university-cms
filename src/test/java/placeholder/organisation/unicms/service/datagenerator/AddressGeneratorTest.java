package placeholder.organisation.unicms.service.datagenerator;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import placeholder.organisation.unicms.entity.Address;
import placeholder.organisation.unicms.service.AddressService;

import java.util.HashSet;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AddressGeneratorTest {

    @Mock
    private AddressService addressService;

    @InjectMocks
    private AddressGenerator addressGenerator;

    @Test
    void generate_shouldEnforceMinimumLimitAndPopulateFields() {
        addressGenerator.generate(5);

        verify(addressService, times(10)).createAddress(argThat(address -> {
            assertThat(address).extracting("city", "street", "country", "houseNumber", "postalCode")
                    .allSatisfy(field -> assertThat(field).isNotNull());
            return true;
        }));
    }

    @Test
    void generate_shouldDistributeAcrossFiveCountries_whenAmountIsSufficient() {
        int amount = 10;
        Set<String> distinctCountries = new HashSet<>();

        doAnswer(invocation -> {
            Address address = invocation.getArgument(0);
            distinctCountries.add(address.getCountry());
            return null;
        }).when(addressService).createAddress(any(Address.class));

        addressGenerator.generate(amount);

        verify(addressService, times(amount)).createAddress(any(Address.class));
        assertThat(distinctCountries).hasSize(5);
    }
}