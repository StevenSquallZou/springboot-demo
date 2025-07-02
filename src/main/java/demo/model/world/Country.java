package demo.model.world;


import jakarta.persistence.*;
import java.math.BigDecimal;


@Entity
@Table(name = "country")
public class Country {
    @Id
    @Column(name = "Code", length = 3, nullable = false)
    private String code;

    @Column(name = "Name", length = 52, nullable = false)
    private String name;

    @Column(name = "Continent", nullable = false, columnDefinition = "ENUM('Asia','Europe','North America','Africa','Oceania','Antarctica','South America')")
    @Convert(converter = ContinentConverter.class)
    private Continent continent = Continent.ASIA;

    @Column(name = "Region", length = 26, nullable = false)
    private String region;

    @Column(name = "SurfaceArea", precision = 10, scale = 2, nullable = false)
    private BigDecimal surfaceArea = new BigDecimal("0.00");

    @Column(name = "IndepYear")
    private Short indepYear;

    @Column(name = "Population", nullable = false)
    private Integer population = 0;

    @Column(name = "LifeExpectancy", precision = 3, scale = 1)
    private BigDecimal lifeExpectancy;

    @Column(name = "GNP", precision = 10, scale = 2)
    private BigDecimal gnp;

    @Column(name = "GNPOld", precision = 10, scale = 2)
    private BigDecimal gnpOld;

    @Column(name = "LocalName", length = 45, nullable = false)
    private String localName;

    @Column(name = "GovernmentForm", length = 45, nullable = false)
    private String governmentForm;

    @Column(name = "HeadOfState", length = 60)
    private String headOfState;

    @Column(name = "Capital")
    private Integer capital;

    @Column(name = "Code2", length = 2, nullable = false)
    private String code2;


    public String getCode() {
        return code;
    }


    public void setCode(String code) {
        this.code = code;
    }


    public String getName() {
        return name;
    }


    public void setName(String name) {
        this.name = name;
    }


    public Continent getContinent() {
        return continent;
    }


    public void setContinent(Continent continent) {
        this.continent = continent;
    }


    public String getRegion() {
        return region;
    }


    public void setRegion(String region) {
        this.region = region;
    }


    public BigDecimal getSurfaceArea() {
        return surfaceArea;
    }


    public void setSurfaceArea(BigDecimal surfaceArea) {
        this.surfaceArea = surfaceArea;
    }


    public Short getIndepYear() {
        return indepYear;
    }


    public void setIndepYear(Short indepYear) {
        this.indepYear = indepYear;
    }


    public Integer getPopulation() {
        return population;
    }


    public void setPopulation(Integer population) {
        this.population = population;
    }


    public BigDecimal getLifeExpectancy() {
        return lifeExpectancy;
    }


    public void setLifeExpectancy(BigDecimal lifeExpectancy) {
        this.lifeExpectancy = lifeExpectancy;
    }


    public BigDecimal getGnp() {
        return gnp;
    }


    public void setGnp(BigDecimal gnp) {
        this.gnp = gnp;
    }


    public BigDecimal getGnpOld() {
        return gnpOld;
    }


    public void setGnpOld(BigDecimal gnpOld) {
        this.gnpOld = gnpOld;
    }


    public String getLocalName() {
        return localName;
    }


    public void setLocalName(String localName) {
        this.localName = localName;
    }


    public String getGovernmentForm() {
        return governmentForm;
    }


    public void setGovernmentForm(String governmentForm) {
        this.governmentForm = governmentForm;
    }


    public String getHeadOfState() {
        return headOfState;
    }


    public void setHeadOfState(String headOfState) {
        this.headOfState = headOfState;
    }


    public Integer getCapital() {
        return capital;
    }


    public void setCapital(Integer capital) {
        this.capital = capital;
    }


    public String getCode2() {
        return code2;
    }


    public void setCode2(String code2) {
        this.code2 = code2;
    }


    @Override
    public String toString() {
        return "Country{" +
                "code='" + code + '\'' +
                ", name='" + name + '\'' +
                ", continent=" + continent +
                ", region='" + region + '\'' +
                ", surfaceArea=" + surfaceArea +
                ", indepYear=" + indepYear +
                ", population=" + population +
                ", lifeExpectancy=" + lifeExpectancy +
                ", gnp=" + gnp +
                ", gnpOld=" + gnpOld +
                ", localName='" + localName + '\'' +
                ", governmentForm='" + governmentForm + '\'' +
                ", headOfState='" + headOfState + '\'' +
                ", capital=" + capital +
                ", code2='" + code2 + '\'' +
                '}';
    }
}
