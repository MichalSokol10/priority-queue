package obsluha;

import tabulka.AbstrTable;
import data.Obec;
import enumy.eTypProhl;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import rozhrani.IAbstrTable;
import rozhrani.IAgendaKraj;
import java.util.Iterator;
import java.util.Random;

public class AgendaKraj implements IAgendaKraj {

    // Datová struktura pro ukládání obcí, implementována pomocí abstraktní tabulky
    private IAbstrTable<String, Obec> obce = new AbstrTable<>();

    /**
    * Načte data ze souboru a vloží je do tabulky.
    * @param nazevSouboru cesta k souboru s daty
    */
    @Override
    public void importDat(String nazevSouboru) {
        obce.zrus();
        int pocetZaznamu = 0;

        try {
            BufferedReader br = new BufferedReader(new FileReader(nazevSouboru));
            String radek;

            while ((radek = br.readLine()) != null) {
                String[] cast = radek.split(";");

                try {
                    obce.vloz(cast[3], new Obec(Integer.parseInt(cast[0]), cast[1], Integer.parseInt(cast[2]), cast[3], Integer.parseInt(cast[4]), Integer.parseInt(cast[5]), Integer.parseInt(cast[6])));
                } catch (Exception e) {
                    throw new RuntimeException("Textový soubor nelze načíst, protože v něm jsou obce se stejným názvem!");
                }
                pocetZaznamu++;
            }

            br.close();

            if (pocetZaznamu == 0) {
                throw new RuntimeException("Textový soubor je pravděpodobně prázdný, nebyl načten žádný záznam měření.");
            } else {
                throw new RuntimeException("Do seznamu bylo úspěšně načteno následujících " + pocetZaznamu + " záznamů.");
            }

        } catch (IOException e) {
            throw new RuntimeException("Chyba při čtení souboru." + e.getMessage());
        }
    }

    /**
    * Vyhledá obec podle názvu.
    * @param nazevObce název obce
    * @return nalezená obec nebo null, pokud není nalezena
    */
    @Override
    public Obec najdi(String nazevObce) {
        return obce.najdi(nazevObce);
    }

    /**
    * Vloží novou obec do tabulky.
    * @param nazevObce název obce
    * @param obec instance třídy Obec
    */
    @Override
    public void vloz(String nazevObce, Obec obec) {
        obce.vloz(nazevObce, obec);
    }
    
    /**
    * Odebere obec z tabulky.
    * @param nazevObce název obce
    * @return odebraná obec nebo null, pokud obec neexistuje
    */
    @Override
    public Obec odeber(String nazevObce) {
        return obce.odeber(nazevObce);
    }

    /**
    * Vytvoří iterátor pro průchod obcemi podle zvoleného typu procházení.
    * @return iterátor obcí
    */
    @Override
    public Iterator<Obec> vytvorIterator() {
        return obce.vytvorIterator(eTypProhl.SIRKA);
    }

    /**
    * Generuje náhodnou obec se simulovanými daty.
    * @return nově vygenerovaná obec
    */
    @Override
    public Obec generuj() {
        int cisloKraje;
        String nazevKraje;
        int psc;
        String nazevObce;
        int pocetMuzu;
        int pocetZen;
        int celkemOsob;
        Random random = new Random();

        cisloKraje = random.nextInt(14) + 1;
        nazevKraje = getNazevKraje(cisloKraje);
        psc = random.nextInt(9998) + 1;

        int delkaNazvu = 8;
        StringBuilder nahodnyNazev = new StringBuilder();
        nahodnyNazev.append((char) (random.nextInt(26) + 'A'));

        for (int j = 1; j < delkaNazvu; j++) {
            char nahodnePismeno = (char) (random.nextInt(26) + 'a');
            nahodnyNazev.append(nahodnePismeno);
        }

        pocetMuzu = random.nextInt(10001);
        pocetZen = random.nextInt(10001);
        celkemOsob = pocetMuzu + pocetZen;

        return new Obec(cisloKraje, nazevKraje, psc, nahodnyNazev.toString(), pocetMuzu, pocetZen, celkemOsob);
    }

    // Gettery a settery
    public IAbstrTable<String, Obec> getObce() {
        return obce;
    }

    public void setObce(IAbstrTable<String, Obec> obce) {
        this.obce = obce;
    }

    /**
    * Vyprázdní tabulku obcí.
    */
    public void zrus() {
        obce.zrus();
    }

    /**
    * Vrátí název kraje podle čísla kraje.
    * @param selectedNumber číslo kraje
    * @return název kraje
    */
    private String getNazevKraje(int selectedNumber) {
        switch (selectedNumber) {
            case 1:
                return "Hlavni mesto Praha";
            case 2:
                return "Jihocesky";
            case 3:
                return "Jihomoravsky";
            case 4:
                return "Karlovarsky";
            case 5:
                return "Kraj Vysocina";
            case 6:
                return "Kralovehradecky";
            case 7:
                return "Liberecky";
            case 8:
                return "Moravskoslezsky";
            case 9:
                return "Olomoucky";
            case 10:
                return "Pardubicky";
            case 11:
                return "Plzensky";
            case 12:
                return "Stredocesky";
            case 13:
                return "Ustecky";
            default:
                return "Zlinsky";
        }
    }

}
