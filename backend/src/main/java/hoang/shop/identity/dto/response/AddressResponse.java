package hoang.shop.identity.dto.response;

public record AddressResponse(
        Long id,
        String lastName,
        String firstName,
        String phone,
        String postalCode,
        String fullAddress,
        String prefecture,
        String municipality,
        String streetNumber,
        String building,
        Boolean isDefault

) {
}
