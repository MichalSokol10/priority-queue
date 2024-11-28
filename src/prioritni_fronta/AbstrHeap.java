package prioritni_fronta;

import enumy.eTypProhl;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Iterator;
import rozhrani.IAbstrFifo;
import zasobnik_fronta.AbstrLifo;
import rozhrani.IAbstrLifo;
import zasobnik_fronta.AbstrFifo;

/**
* Abstraktní třída pro implementaci haldy (heap), která je generická a může
* pracovat s jakýmkoliv typem dat, pokud je zajištěn komparátor pro porovnávání
* těchto dat.
* 
* @param <T> Typ prvků v haldě
*/
public class AbstrHeap<T> {

    private T[] polePrvku;
    private int velikost;
    private Comparator<T> komparator;

    public AbstrHeap() {
    }
    
    /**
    * Metoda pro vybudování haldy z daného pole prvků a komparátoru.
    * 
    * @param prvky Pole prvků, které budou tvořit haldu
    * @param novyKomparator Komparátor pro porovnávání prvků
    */
    public void vybuduj(T[] prvky, Comparator<T> novyKomparator) {
        this.polePrvku = Arrays.copyOfRange(prvky, 0, prvky.length);
        this.velikost = prvky.length;
        this.komparator = novyKomparator;

        for (int i = velikost / 2 - 1; i >= 0; i--) {
            traversujDolu(i);
        }
    }

    /**
    * Získání velikosti haldy (počet prvků).
    * 
    * @return Velikost haldy
    */
    public int getVelikost() {
        return this.velikost;
    }

    /**
    * Reorganizace haldy s novým komparátorem.
    * 
    * @param novyKomparator Komparátor pro porovnávání prvků
    */
    public void reorganizace(Comparator<T> novyKomparator) {
        if (novyKomparator != null) {
            this.komparator = novyKomparator;

            for (int i = velikost / 2 - 1; i >= 0; i--) {
                traversujDolu(i);
            }
        }
    }

    /**
    * Metoda pro zrušení haldy. Vyprázdní ji.
    */
    public void zrus() {
        polePrvku = null;
        velikost = 0;
    }

    /**
    * Kontrola, zda je halda prázdná.
    * 
    * @return True, pokud je halda prázdná, jinak false
    */
    public boolean jePrazdny() {
        return velikost == 0;
    }
    
    /**
    * Vloží prvek do haldy a provede její reorganizaci.
    * 
    * @param prvek Prvek, který má být vložen
    */
    public void vloz(T prvek) {
        if (velikost == polePrvku.length) {
            zvetsiKapacitu();
        }

        polePrvku[velikost] = prvek;
        velikost++;

        traversujNahoru(velikost - 1);
    }

    /**
    * Odebere prvek s nejvyšší prioritou (maximální hodnotu) a vrátí ji, provede reorganizaci.
    * 
    * @return Prvek s nejvyšší prioritou nebo null, pokud je halda prázdná
    */
    public T odeberMax() {
        if (velikost == 0) {
            return null;
        }

        T maxPrvek = polePrvku[0];

        polePrvku[0] = polePrvku[velikost - 1];
        velikost--;

        traversujDolu(0);

        return maxPrvek;
    }
    
    /**
    * Zpřístupní prvek s nejvyšší prioritou.
    * 
    * @return Prvek s nejvyšší prioritou nebo null, pokud je halda prázdná
    */
    public T zpristupniMax() {
        return velikost > 0 ? polePrvku[0] : null;
    }
    
    /**
    * Vytvoří a vrátí iterator pro procházení haldy podle zvoleného typu prohlížení.
    * 
    * @param typProhl Typ prohlížení, zda do hloubky nebo do šířky
    * @return Iterator pro procházení haldy
    */
    public Iterator<T> vypis(eTypProhl typProhl) {
        switch (typProhl) {
            case HLOUBKA:
                return new IteratorHloubka();
            case SIRKA:
                return new IteratorSirka();
            default:
                System.out.println("Neznámý typ prohlížení.");
                return null;
        }
    }

    /**
    * Provádí operaci „up-heapify“ (procházení nahoru) pro prvek na daném indexu.
    * 
    * @param index Index prvku, který má být zpracován
    */
    private void traversujNahoru(int index) {
        while (index > 0) {
            int indexRodice = (index - 1) / 2;
            if (komparator.compare(polePrvku[index], polePrvku[indexRodice]) > 0) {
                prohod(index, indexRodice);
                index = indexRodice;
            } else {
                break;
            }
        }
    }

    /**
    * Provádí operaci „down-heapify“ (procházení dolů) pro prvek na daném indexu.
    * 
    * @param index Index prvku, který má být zpracován
    */
    private void traversujDolu(int index) {
        int nejvyssiPriorita = index;
        int indexLevyPotomek = 2 * index + 1;
        int indexPravyPotomek = 2 * index + 2;

        if (indexLevyPotomek < velikost && komparator.compare(polePrvku[indexLevyPotomek], polePrvku[nejvyssiPriorita]) > 0) {
            nejvyssiPriorita = indexLevyPotomek;
        }

        if (indexPravyPotomek < velikost && komparator.compare(polePrvku[indexPravyPotomek], polePrvku[nejvyssiPriorita]) > 0) {
            nejvyssiPriorita = indexPravyPotomek;
        }

        if (nejvyssiPriorita != index) {
            prohod(index, nejvyssiPriorita);
            traversujDolu(nejvyssiPriorita);
        }
    }
    
    /**
    * Prohodí dva prvky v poli.
    * 
    * @param i Index prvního prvku
    * @param j Index druhého prvku
    */
    private void prohod(int i, int j) {
        T tempPrvek = polePrvku[i];
        polePrvku[i] = polePrvku[j];
        polePrvku[j] = tempPrvek;
    }

    /**
    * Zdvojnásobí kapacitu pole, aby mohlo obsahovat více prvků.
    */
    private void zvetsiKapacitu() {
        int novaKapacita = polePrvku.length * 2;
        polePrvku = Arrays.copyOf(polePrvku, novaKapacita);
    }

    /**
    * Iterator pro procházení haldy do hloubky (preorder traversal).
    */
    private class IteratorHloubka implements Iterator<T> {

        private final IAbstrLifo<Integer> zasobnik = new AbstrLifo<>();

        public IteratorHloubka() {
            if (velikost != 0) {
                // Vloží kořenový prvek do zásobníku.
                zasobnik.vloz(0);
            }
        }

        /**
        * Metoda zjišťuje, zda existuje další prvek k procházení.
        * 
        * @return True, pokud existuje další prvek, jinak False
        */
        @Override
        public boolean hasNext() {
            return !zasobnik.jePrazdny();
        }

        /**
        * Metoda vrátí následující prvek v iteraci. Tento prvek je vybrán podle 
        * pořadí procházení do hloubky (preorder traversal).
        * 
        * @return Další prvek v iteraci haldy, nebo null, pokud již není žádný
        */
        @Override
        public T next() {
            if (!hasNext()) {
                return null;
            }

            int aktualniIndex = zasobnik.odeber();
            T vysledek = polePrvku[aktualniIndex];

            int levyPotomekIndex = 2 * aktualniIndex + 1;
            int pravyPotomekIndex = 2 * aktualniIndex + 2;

            if (pravyPotomekIndex < velikost) {
                zasobnik.vloz(pravyPotomekIndex);
            }

            if (levyPotomekIndex < velikost) {
                zasobnik.vloz(levyPotomekIndex);
            }
            return vysledek;
        }
    }
    
    /**
    * Iterator pro procházení haldy do šířky (level-order traversal).
    */
    private class IteratorSirka implements Iterator<T> {

        private final IAbstrFifo<Integer> fronta = new AbstrFifo<>();

        public IteratorSirka() {
            if (velikost != 0) {
                for (int i = 0; i < velikost; i++) {
                    // Přidá všechny indexy prvků haldy do fronty pro procházení do šířky
                    fronta.vloz(i);
                }
            }
        }

        /**
        * Metoda zjišťuje, zda existuje další prvek k procházení.
        * 
        * @return True, pokud existuje další prvek, jinak False
        */
        @Override
        public boolean hasNext() {
            return !fronta.jePrazdny();
        }

        /**
        * Metoda vrátí následující prvek v iteraci. Tento prvek je vrácen podle
        * pořadí procházení do šířky (level-order traversal).
        * 
        * @return Další prvek v iteraci haldy, nebo null, pokud již není žádný
        */
        @Override
        public T next() {
            if (!hasNext()) {
                return null;
            }

            int aktualniIndex = fronta.odeber();
            T vysledek = polePrvku[aktualniIndex];

            return vysledek;
        }
    }
}
