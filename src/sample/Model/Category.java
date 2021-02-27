package sample.Model;

import java.util.HashMap;
import java.util.Map;

public enum Category {


    archeo("Archeologia"),
    philosophy("Filozofia"),
    historia("Historia"),
    linguistics("JÄ™zykoznawstwo"),
    literaryStudies("Literaturoznawstwo"),
    cultureAndReligion("Nauki o kulturze i religii"),
    arts("Nauki o sztuce"),
    architectureAndUrbanPlanning("Architektura i Urbanistyka"),
    automatics("Automatyka, Elektronika i Elektrotechnika"),
    informatics("Informatyka techniczna i Telekomunikacja"),
    biomedicEngineering("InĹĽynieria Biomedyczna"),
    chemicalEngineering("InĹĽynieria Chemiczna"),
    transportation("InĹĽynieria lÄ…dowa i Transport"),
    materialScience("Inzynieria MateriaĹ‚owa"),
    mechanics("inĹĽynieria mechaniczna"),
    environmentalMiningAndEnergetics("InĹĽynieria Ĺšrodowiska, GĂłrnictwo i Energetyka"),
    pharmaceuticalStudies("Nauki farmaceutyczne"),
    medicalScience("Nauki Medyczne"),
    physicalCultureStudies("Nauki o kulturze fizycznej"),
    healthScience("Nauki o zdrowiu"),
    forestStudies("Nauki leĹ›ne"),
    agricultureAndGardening("Rolnictwo i Ogrodnictwo"),
    foodTechnology("Technologia ĹĽywnoĹ›ci i ĹĽywienia"),
    veterinary("Weterynaria"),
    zootechnics("Zootechnika i Rybactwo"),
    economics("Ekonomia i Finanse"),
    geographyEconomicsSocial("Geografia spoĹ‚eczno-ekonomiczna"),
    securityStudies("nauki o bezpieczeĹ„stwi"),
    mediaAndSocialCommunicationStudies("Nauki o komunikacji spoĹ‚ecznej i mediach"),
    politicsAndAdministration("Nauki o polityce i administracji"),
    managementAndQualityStudies("Nauki o ZarzÄ…dzaniu i JakoĹ›ci"),
    legalStudies("Nauki Prawne"),
    sociologicalStudies("Nauki Socjologiczne"),
    pedagogics("Pedagogika"),
    canonLaw("Prawo Kanoniczne"),
    psychology("Psychologia"),
    astronomy("Astronomia"),
    computerScience("Informatyka"),
    mathematics("Matematyka"),
    biologicalScience("Nauki Biologiczne"),
    chemicalScience("Nauki Chemiczne"),
    physicalStudies("Nauki Fizyczne"),
    earthAndEnvironmentStudies("Nauki o Ziemi i Ĺšrodowisku"),
    theologicalStudies("Nauki Teologiczne");

    public final String name;

    Category(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    private static final Map<String, Category> lookup = new HashMap<>();

    static {
        for(Category p : Category.values()){
            lookup.put(p.getName(), p);
        }
    }
    public static Category get(String str){
        return lookup.get(str);
    }

}
