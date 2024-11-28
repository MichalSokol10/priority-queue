package gui;

import obsluha.AgendaKraj;
import data.Obec;
import enumy.eTypProhl;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URL;
import java.util.Comparator;
import java.util.Iterator;
import java.util.ResourceBundle;
import java.util.Scanner;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ListView;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;
import javafx.scene.control.TextField;
import prioritni_fronta.AbstrHeap;

/**
 * FXML Controller class
 *
 * @author sokol
 */
public class FXMLController implements Initializable {

    @FXML
    private ListView<Obec> lv_seznam; // Seznam obcí zobrazený v UI
    @FXML
    private Button btn_pridej; // Tlačítko pro přidání nové obce
    @FXML
    private Spinner<Integer> spn_pocetGenerovani; // Spinner pro výběr počtu generovaných obcí
    @FXML
    private Button btn_generuj; // Tlačítko pro generování nových obcí
    @FXML
    private Button btn_uloz; // Tlačítko pro uložení obcí do souboru
    @FXML
    private Button btn_nacti; // Tlačítko pro načtení obcí ze souboru
    @FXML
    private Button btn_odeber; // Tlačítko pro odebrání obce
    private TextField tf_idHledani; // Textové pole pro zadání ID při hledání obce
    @FXML
    private TextField tf_dialog; // Textové pole pro zobrazení dialogových zpráv
    @FXML
    private Button btn_zrus; // Tlačítko pro zrušení prioritní fronty

    private AgendaKraj agenda = new AgendaKraj();
    @FXML
    private Spinner<Integer> spn_cisloKraje; // Spinner pro výběr čísla kraj
    @FXML
    private TextField tf_nazevKraje; // Textové pole pro název kraje
    @FXML
    private TextField tf_psc; // Textové pole pro zadání PSČ
    @FXML
    private TextField tf_nazevObce; // Textové pole pro zadání názvu obce
    @FXML
    private TextField tf_pocetMuzu; // Textové pole pro zadání počtu mužů
    @FXML
    private TextField tf_pocetZen; // Textové pole pro zadání počtu žen
    @FXML
    private TextField tf_celkemOsob; // Textové pole pro zobrazení celkového počtu osob
    @FXML
    private Button btn_najdi; // Tlačítko pro hledání obce

    private ObservableList<Obec> observableList = FXCollections.observableArrayList(); // Seznam obcí pro zobrazení v UI
    @FXML
    private CheckBox cb_sirka; // Checkbox pro výběr prohledávání do šířky
    @FXML
    private CheckBox cb_hloubka; // Checkbox pro výběr prohledávání do hloubky

    private eTypProhl prohlidka = eTypProhl.HLOUBKA; // Výchozí typ prohledávání (hloubka nebo šířka)

    private AbstrHeap<Obec> prioritniFronta = new AbstrHeap<>();
    @FXML
    private ComboBox<String> cb_comparator; // ComboBox pro výběr komparátoru pro řazení

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        ObservableList<String> items = FXCollections.observableArrayList(
                "POČET OSOB",
                "NÁZEV OBCE"
        );

        cb_comparator.setItems(items);
        cb_comparator.getSelectionModel().selectFirst();

        SpinnerValueFactory<Integer> valueFactory = new SpinnerValueFactory.IntegerSpinnerValueFactory(1, 14);
        spn_cisloKraje.setValueFactory(valueFactory);

        SpinnerValueFactory<Integer> valueFactoryPocetGenerovani = new SpinnerValueFactory.IntegerSpinnerValueFactory(1, 100);
        spn_pocetGenerovani.setValueFactory(valueFactoryPocetGenerovani);
        
        // Listener pro změnu výběru prohledávání do hloubky
        cb_hloubka.selectedProperty().addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
                if (newValue) {
                    cb_sirka.setSelected(false);
                    prohlidka = eTypProhl.HLOUBKA;
                    aktualizovatListView();
                }
            }
        });

        // Listener pro změnu výběru prohledávání do šířky
        cb_sirka.selectedProperty().addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
                if (newValue) {
                    cb_hloubka.setSelected(false);
                    prohlidka = eTypProhl.SIRKA;
                    aktualizovatListView();
                }
            }
        });

        tf_nazevKraje.setText("Hlavni mesto Praha");
        tf_nazevKraje.setEditable(false);
        tf_celkemOsob.setEditable(false);

        // Listener pro změnu počtu mužů
        tf_pocetMuzu.textProperty().addListener((observable, oldValue, newValue) -> {
            updateCelkemOsob();
        });

        // Listener pro změnu počtu žen
        tf_pocetZen.textProperty().addListener((observable, oldValue, newValue) -> {
            updateCelkemOsob();
        });
        
        // Listener pro změnu čísla kraje
        spn_cisloKraje.valueProperty().addListener((obs, oldValue, newValue) -> {
            int selectedNumber = spn_cisloKraje.getValue();
            String nazevKraje = getNazevKraje(selectedNumber);
            tf_nazevKraje.setText(nazevKraje);
        });
    }

    /**
    * Uloží seznam obcí do textového souboru.
    * Obce jsou seřazeny v prioritní frontě a následně zapsány do souboru.
    *
    * @param event Akce tlačítka pro uložení do souboru.
    */
    @FXML
private void ulozDoSouboru(ActionEvent event) {
    int pocetZaznamu = 0;

    Obec[] serazeneObce = new Obec[prioritniFronta.getVelikost()];

    for (int i = 0; i < serazeneObce.length; i++) {
        serazeneObce[i] = prioritniFronta.odeberMax();
    }

    try {
        FileWriter fw = new FileWriter("obce.txt");

        for (Obec obec : serazeneObce) {
            fw.write(obec.getCisloKraje() + ";");
            fw.write(obec.getNazevKraje() + ";");
            fw.write(obec.getPSC() + ";");
            fw.write(obec.getObec() + ";");
            fw.write(obec.getPocetMuzu() + ";");
            fw.write(obec.getPocetZen() + ";");
            fw.write(obec.getPocetOsob() + "\n");
            pocetZaznamu++;
        }

        fw.close();

        if (pocetZaznamu == 0) {
            tf_dialog.setText("Váš seznam je prázdný, do textového souboru nebyl uložen žádný záznam měření.");
        } else {
            tf_dialog.setText("Do textového souboru bylo úspěšně uloženo " + pocetZaznamu + ".");
        }
        
        aktualizovatListView();

    } catch (IOException e) {
        e.printStackTrace(); // Pokud dojde k chybě při zápisu do souboru, vypište chybu do konzole
    }
}

//    @FXML
//    private void nactiZeSouboru(ActionEvent event) {
//        try {
//            agenda.importDat("test.txt");
//        } catch (Exception e) {
//            tf_dialog.setText(e.getMessage());
//        }
//
//        aktualizovatListView();
//    }
//    

    /**
    * Načte data o obcích ze souboru a přidá je do prioritní fronty.
    * 
    * @param event Akce tlačítka pro načítání dat ze souboru.
    */
    @FXML
    private void nactiZeSouboru(ActionEvent event) {
        try {
            Obec[] obce = nactiDataZeSouboru("kraje.csv");
            prioritniFronta.vybuduj(obce, Comparator.comparingInt(Obec::getPocetOsob));
        } catch (Exception e) {
            tf_dialog.setText(e.getMessage());
        }

        aktualizovatListView();
    }

    /**
    * Načte data o obcích ze souboru a vrátí je jako pole objektů třídy Obec.
    * 
    * @param cestaKsouboru Cesta k souboru, který obsahuje data o obcích.
    * @return Pole objektů třídy Obec, které obsahuje všechny obce načtené ze souboru.
    * @throws FileNotFoundException Pokud soubor na zadané cestě neexistuje nebo není dostupný.
    */
    private static Obec[] nactiDataZeSouboru(String cestaKsouboru) throws FileNotFoundException {
        int pocetRadku = spocitejPocetRadku(cestaKsouboru);
        Obec[] seznamObci = new Obec[pocetRadku];

        try (Scanner scanner = new Scanner(new File(cestaKsouboru))) {
            int index = 0;
            while (scanner.hasNextLine()) {
                String radek = scanner.nextLine();
                String[] hodnoty = radek.split(";");

                // Zpracování hodnot ze souboru a vytvoření instance třídy Obec
                int cisloKraje = Integer.parseInt(hodnoty[0]);
                String nazevKraje = hodnoty[1];
                int PSC = Integer.parseInt(hodnoty[2]);
                String obec = hodnoty[3];
                int pocetMuzu = Integer.parseInt(hodnoty[4]);
                int pocetZen = Integer.parseInt(hodnoty[5]);
                int pocetOsob = Integer.parseInt(hodnoty[6]);

                Obec novaObec = new Obec(cisloKraje, nazevKraje, PSC, obec, pocetMuzu, pocetZen, pocetOsob);
                seznamObci[index] = novaObec;
                index++;
            }

            return seznamObci;
        }
    }

    /**
    * Spočítá počet řádků v souboru na základě zadané cesty k souboru.
    * 
    * @param cestaKsouboru Cesta k souboru, jehož řádky mají být spočítány.
    * @return Počet řádků v souboru.
    */
    private static int spocitejPocetRadku(String cestaKsouboru) {
        int pocetRadku = 0;

        try (Scanner scanner = new Scanner(new File(cestaKsouboru))) {
            while (scanner.hasNextLine()) {
                scanner.nextLine();
                pocetRadku++;
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        return pocetRadku;
    }

    /**
    * Aktualizuje zobrazení seznamu obcí ve ListView.
    */
    private void aktualizovatListView() {

        observableList.clear();

        Iterator<Obec> it = prioritniFronta.vypis(prohlidka);
        while (it.hasNext()) {
            observableList.add(it.next());
        }

        lv_seznam.setItems(observableList);
    }

    /**
    * Získá název kraje podle zadaného čísla kraje.
    * 
    * @param selectedNumber Číslo kraje, pro který má být vrácen název.
    * @return Název kraje odpovídající zadanému číslu kraje.
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

    /**
    * Aktualizuje celkový počet osob na základě zadaného počtu mužů a žen.
    */
    private void updateCelkemOsob() {
        int pocetMuzu = 0;
        int pocetZen = 0;

        try {
            pocetMuzu = Integer.parseInt(tf_pocetMuzu.getText());
        } catch (NumberFormatException e) {

        }

        try {
            pocetZen = Integer.parseInt(tf_pocetZen.getText());
        } catch (NumberFormatException e) {

        }

        int celkemOsob = pocetMuzu + pocetZen;
        tf_celkemOsob.setText(Integer.toString(celkemOsob));
    }

    /**
    * Přidá novou obec do prioritní fronty a aktualizuje zobrazení v UI.
    *
    * @param event Akce tlačítka pro přidání nové obce.
    */
    @FXML
    private void pridejObec(ActionEvent event) {
        int cisloKraje;
        String nazevKraje;
        int psc;
        String nazevObce;
        int pocetMuzu;
        int pocetZen;
        int celkemOsob;

        cisloKraje = spn_cisloKraje.getValue();
        nazevKraje = tf_nazevKraje.getText();
        String upravenyNazevObce;

        try {
            psc = Integer.parseInt(tf_psc.getText());
            if (tf_psc.getText().length() != 4) {
                throw new Exception();
            }
        } catch (Exception e) {
            tf_dialog.setText("PSČ musí být čtyřmístné číslo!");
            return;
        }

        try {
            nazevObce = tf_nazevObce.getText();
            if (!(nazevObce.length() > 0 && Character.isUpperCase(nazevObce.charAt(0)))) {
                throw new Exception("Musíte zadat název obce s velkým počátečním písmenem!");
            }
            String zbytekNazvuObce = nazevObce.substring(1).toLowerCase();
            upravenyNazevObce = nazevObce.substring(0, 1) + zbytekNazvuObce;
        } catch (Exception e) {
            tf_dialog.setText(e.getMessage());
            return;
        }

        try {
            pocetMuzu = Integer.parseInt(tf_pocetMuzu.getText());
            if (pocetMuzu < 0) {
                throw new Exception();
            }
        } catch (Exception e) {
            tf_dialog.setText("Počet mužů musí být kladné celé číslo!");
            return;
        }

        try {
            pocetZen = Integer.parseInt(tf_pocetZen.getText());
            if (pocetZen < 0) {
                throw new Exception();
            }
        } catch (Exception e) {
            tf_dialog.setText("Počet žen musí být kladné celé číslo!");
            return;
        }

        celkemOsob = Integer.parseInt(tf_celkemOsob.getText());

        Obec obec = new Obec(cisloKraje, nazevKraje, psc, upravenyNazevObce, pocetMuzu, pocetZen, celkemOsob);

//        try {
//            agenda.vloz(upravenyNazevObce, obec);
//        } catch (Exception e) {
//            tf_dialog.setText(e.getMessage());
//            return;
//        }

        if (prioritniFronta.getVelikost() == 0) {
            Obec[] obce = new Obec[1];
            obce[0] = obec;
            prioritniFronta.vybuduj(obce, Comparator.comparingInt(Obec::getPocetOsob));
        } else {
            prioritniFronta.vloz(obec);
        }

        tf_dialog.setText("Byla přidána následující obec: " + obec.toString());
        aktualizovatListView();
    }

    /**
    * Generuje daný počet obcí a přidává je do prioritní fronty.
    * 
    * @param event Akce, která vyvolala tuto metodu.
    */
    @FXML
    private void generujObce(ActionEvent event) {
        int pocetVygenerovanychObci = 0;

        while (pocetVygenerovanychObci < spn_pocetGenerovani.getValue()) {
            Obec novaObec = agenda.generuj();

            if (prioritniFronta.getVelikost() == 0) {
                Obec[] obce = new Obec[1];
                obce[0] = novaObec;
                prioritniFronta.vybuduj(obce, Comparator.comparingInt(Obec::getPocetOsob));
            } else {
                prioritniFronta.vloz(novaObec);
            }

            pocetVygenerovanychObci++;
        }

        aktualizovatListView();
        tf_dialog.setText("Bylo vygenerováno následujících " + spn_pocetGenerovani.getValue() + " obcí.");
    }

    /**
    * Odebere obec z prioritní fronty a aktualizuje zobrazení.
    * 
    * @param event Akce, která vyvolala tuto metodu.
    */
    @FXML
    private void odeberObec(ActionEvent event) {
        try {
            tf_dialog.setText("Byla odebrána následující obec: " + prioritniFronta.odeberMax().toString());
            aktualizovatListView();
        } catch (Exception e) {
            return;
        }
    }

//    @FXML
//    private void odeberObec(ActionEvent event) {
//        try {
//            tf_dialog.setText("Byla odebrána následující obec: " + agenda.odeber(tf_idHledani.getText()).toString());
//            aktualizovatListView();
//        } catch (Exception e) {
//            tf_dialog.setText(e.getMessage());
//        }
//    }
//    @FXML
//    private void najdiObec(ActionEvent event) {
//        try {
//            tf_dialog.setText(agenda.najdi(tf_idHledani.getText()).toString());
//        } catch (Exception e) {
//            tf_dialog.setText(e.getMessage());
//        }
//    }
    
    /**
    * Vyhledá obec s nejvyšší prioritou v prioritní frontě a zobrazí její údaje.
    * 
    * @param event Akce, která vyvolala tuto metodu.
    */
    @FXML
    private void najdiObec(ActionEvent event) {
        try {
            tf_dialog.setText(prioritniFronta.zpristupniMax().toString());
        } catch (Exception e) {
            return;
        }
    }

//    @FXML
//    private void zrusTabulku(ActionEvent event) {
//        agenda.zrus();
//        aktualizovatListView();
//        tf_dialog.setText("Tabulka měření byla vymazána.");
//    }
    
    /**
    * Vymaže všechna data v prioritní frontě a aktualizuje zobrazení.
    * 
    * @param event Akce, která tuto metodu vyvolala.
    */
    @FXML
    private void zrusTabulku(ActionEvent event) {
        prioritniFronta.zrus();
        aktualizovatListView();
        tf_dialog.setText("Prioritní fronta byla vymazána.");
    }

    /**
    * Reorganizuje prioritní frontu na základě vybraného kritéria a aktualizuje zobrazení.
    * 
    * @param event Akce, která tuto metodu vyvolala.
    */
    @FXML
    private void reorganizuj(ActionEvent event) {
        if (cb_comparator.getSelectionModel().getSelectedItem().equals("POČET OSOB")) {
            prioritniFronta.reorganizace(Comparator.comparingInt(Obec::getPocetOsob));
        } else {
            prioritniFronta.reorganizace(Comparator.comparing(Obec::getObec).reversed());
        }

        aktualizovatListView();
    }
}
