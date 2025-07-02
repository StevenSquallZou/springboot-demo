package demo.model.world;


import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;


@Converter(autoApply = true)
public class ContinentConverter implements AttributeConverter<Continent, String> {

    @Override
    public String convertToDatabaseColumn(Continent attribute) {
        return attribute == null ? null : attribute.toString();
    }


    @Override
    public Continent convertToEntityAttribute(String dbData) {
        if (dbData == null) {
            return null;
        }

        for (Continent c : Continent.values()) {
            if (c.toString().equals(dbData)) {
                return c;
            }
        }

        throw new IllegalArgumentException("Unknown continent: " + dbData);
    }
}
