package placeholder.organisation.unicms.service.datagenerator;

import placeholder.organisation.unicms.entity.Address;
import placeholder.organisation.unicms.service.AddressService;

import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

public class AddressGenerator implements DataGenerator {


    private static final Map<String, Map<String, List<String>>> REGIONAL_DATA = Map.of(
            "Ukraine", Map.of(
                    "Kyiv", List.of("Khreshchatyk St", "Peremohy Ave", "Volodymyrska St", "Lesi Ukrainky Blvd", "Velyka Vasylkivska St"),
                    "Kharkiv", List.of("Sumska St", "Nauky Ave", "Poltavskyi Shliakh St", "Pushkinska St", "Gagarina Ave")
            ),
            "Lithuania", Map.of(
                    "Vilnius", List.of("Gedimino pr.", "Vilniaus g.", "Konstitucijos pr.", "Pilies g.", "Jogailos g."),
                    "Kaunas", List.of("Laisvės al.", "Savanorių pr.", "Karaliaus Mindaugo pr.", "Vytauto pr.", "Vilniaus g.")
            ),
            "Poland", Map.of(
                    "Warsaw", List.of("Marszałkowska St", "Jerozolimskie Ave", "Nowy Świat St", "Puławska St", "Krakowskie Przedmieście St"),
                    "Krakow", List.of("Floriańska St", "Grodzka St", "Karmelicka St", "Pokoju Ave", "Kalwaryjska St")
            ),
            "Moldova", Map.of(
                    "Chisinau", List.of("Stefan cel Mare si Sfant Blvd", "Ciocana St", "Botanica Blvd", "Alba Iulia St", "Mihai Eminescu St"),
                    "Balti", List.of("Stefan cel Mare St", "Decebal St", "Independentei St", "Kiev St", "Bulgara St")
            ),
            "Belarus", Map.of(
                    "Minsk", List.of("Nezavisimosti Ave", "Pobediteley Ave", "Lenina St", "Nemiga St", "Surganova St"),
                    "Gomel", List.of("Sovetskaya St", "Lenina Ave", "Pobedy Ave", "Mazurova St", "Barykina St")
            )
    );

    AddressService addressService;

    public AddressGenerator(AddressService addressService) {
        this.addressService = addressService;
    }

    @Override
    public void generate(int amount) {
        int totalCities = 10;
        int addressesPerCity = Math.max(1, amount / totalCities);

        REGIONAL_DATA.forEach((country, cities) -> {
            cities.forEach((city, streets) -> {
                for (int i = 0; i < addressesPerCity; i++) {
                    String street = streets.get(ThreadLocalRandom.current().nextInt(streets.size()));
                    String houseNumber = generateHouseNumber();
                    String postalCode = generatePostalCode();

                    Address result = new Address(city, street, country, houseNumber, postalCode);
                    addressService.createAddress(result);
                }
            });
        });
    }

    private String generateHouseNumber() {
        Random random = new Random();
        int number = random.nextInt(150);
        return String.valueOf(number);
    }

    private String generatePostalCode() {
        Random random = new Random();
        final int MAX_NATIONAL_AREA = 9;
        final int MAX_SECURITY_CENTER = 99;
        final int MAX_DELIVERY_AREA = 99;
        int nationalArea = random.nextInt(MAX_NATIONAL_AREA);
        int securityCenter = random.nextInt(MAX_SECURITY_CENTER);
        int deliveryArea = random.nextInt(MAX_DELIVERY_AREA);
        return String.valueOf(nationalArea + "" + securityCenter + deliveryArea);
    }

}
