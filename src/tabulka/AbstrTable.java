package tabulka;


import zasobnik_fronta.AbstrLifo;
import zasobnik_fronta.AbstrFifo;
import enumy.eTypProhl;
import rozhrani.IAbstrTable;
import rozhrani.IAbstrFifo;
import rozhrani.IAbstrLifo;
import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * Třída reprezentuje abstraktní implementaci tabulky, která používá binární vyhledávací strom.
 * 
 * Tato třída implementuje rozhraní IAbstrTable, které umožňuje vložení, nalezení a odebrání prvků
 * podle klíče, stejně jako procházení tabulkou pomocí iterátorů podle různých metod prohledávání (do hloubky nebo do šířky).
 * 
 * <p>Tabulka je implementována jako binární vyhledávací strom, kde každý prvek (uzel) obsahuje klíč, hodnotu a odkazy na levého a pravého potomka.
 * 
 * @param <K> Typ klíče, který musí implementovat rozhraní {@link Comparable}.
 * @param <V> Typ hodnoty, která je asociována s klíčem.
 */
public class AbstrTable<K extends Comparable<K>, V> implements IAbstrTable<K, V> {

    private Prvek koren; // Kořenový prvek binárního stromu

    /**
    * Vnitřní třída představující uzel (prvek) binárního stromu.
    */
    private class Prvek {

        K key; // Klíč prvku
        V value; // Hodnota prvku
        Prvek leva; // Levá větev prvku
        Prvek prava; // Pravá větev prvku

        public Prvek(K key, V value) {
            this.key = key;
            this.value = value;
            this.leva = null;
            this.prava = null;
        }
    }

    /**
    * Zruší obsah tabulky, odstraní všechny prvky.
    */
    @Override
    public void zrus() {
        koren = null;
    }

    /**
    * Zkontroluje, zda je tabulka prázdná.
    * 
    * @return True pokud je tabulka prázdná, jinak false.
    */
    @Override
    public boolean jePrazdny() {
        return koren == null;
    }

    /**
    * Najde hodnotu podle zadaného klíče.
    * 
    * @param key Klíč, podle kterého hledáme.
    * @return Hodnota spojená s daným klíčem.
    * @throws NullPointerException pokud je tabulka prázdná nebo klíč není nalezen.
    */
    @Override
    public V najdi(K key) {
        if (jePrazdny()) {
            throw new NullPointerException("Strom je prázdný!");
        }

        Prvek prvek = koren;

        while (prvek != null) {
            int porovnani = key.compareTo(prvek.key);
            if (porovnani < 0) {
                prvek = prvek.leva;
            } else if (porovnani > 0) {
                prvek = prvek.prava;
            } else {
                return prvek.value;
            }
        }

        throw new NullPointerException("Nebyla nalezena žádná obec s názvem: " + key);
    }

    /**
    * Vloží nový prvek do tabulky podle zadaného klíče a hodnoty.
    * Pokud klíč již existuje, vyvolá výjimku.
    * 
    * @param key Klíč pro nový prvek.
    * @param value Hodnota spojená s tímto klíčem.
    * @throws NullPointerException pokud je klíč již v tabulce.
    */
    @Override
    public void vloz(K key, V value) {
        Prvek novyPrvek = new Prvek(key, value);
        
        if (jePrazdny()) {
            koren = novyPrvek;
            return;
        }
        
        try {
            najdi(key);
            throw new Exception("Klíč byl nalezen.");
        } catch (NullPointerException e) {
            
        } catch (Exception e) {
            throw new NullPointerException("Nemůžete vložit obec se stejným názvem!");
        }

        Prvek prvek = koren;
        Prvek rodic = null;

        while (prvek != null) {
            rodic = prvek;
            int porovnani = key.compareTo(prvek.key);
            if (porovnani < 0) {
                prvek = prvek.leva;
            } else {
                prvek = prvek.prava;
            }
        }

        int porovnani = key.compareTo(rodic.key);

        if (porovnani < 0) {
            rodic.leva = novyPrvek;
        } else {
            rodic.prava = novyPrvek;
        }
    }

    /**
    * Odebere prvek podle zadaného klíče a vrátí jeho hodnotu.
    * 
    * @param key Klíč prvku, který má být odebrán.
    * @return Hodnota odebraného prvku.
    * @throws IllegalArgumentException pokud klíč není v tabulce.
    */
    @Override
    public V odeber(K key) {
        if (jePrazdny()) {
            throw new NullPointerException("Tabulka je prázdná, nelze odebrat žádnou obec!");
        }

        Prvek prvek = koren;
        Prvek rodic = null;

        while (prvek != null) {
            int porovnani = key.compareTo(prvek.key);

            if (porovnani == 0) {
                if (prvek.leva == null && prvek.prava == null) {
                    if (rodic != null) {
                        if (rodic.leva == prvek) {
                            rodic.leva = null;
                        } else {
                            rodic.prava = null;
                        }
                    } else {
                        koren = null;
                    }
                } else if (prvek.leva == null) {
                    if (rodic != null) {
                        if (rodic.leva == prvek) {
                            rodic.leva = prvek.prava;
                        } else {
                            rodic.prava = prvek.prava;
                        }
                    } else {
                        koren = prvek.prava;
                    }
                } else if (prvek.prava == null) {
                    if (rodic != null) {
                        if (rodic.leva == prvek) {
                            rodic.leva = prvek.leva;
                        } else {
                            rodic.prava = prvek.leva;
                        }
                    } else {
                        koren = prvek.leva;
                    }
                } else {
                    Prvek naslednik = prvek.prava;
                    Prvek naslednikRodic = prvek;
                    while (naslednik.leva != null) {
                        naslednikRodic = naslednik;
                        naslednik = naslednik.leva;
                    }
                    if (naslednikRodic != prvek) {
                        naslednikRodic.leva = naslednik.prava;
                        naslednik.prava = prvek.prava;
                    }
                    if (rodic != null) {
                        if (rodic.leva == prvek) {
                            rodic.leva = naslednik;
                        } else {
                            rodic.prava = naslednik;
                        }
                    } else {
                        koren = naslednik;
                    }
                    naslednik.leva = prvek.leva;
                }
                return prvek.value;
            } else if (porovnani < 0) {
                rodic = prvek;
                prvek = prvek.leva;
            } else {
                rodic = prvek;
                prvek = prvek.prava;
            }
        }

        throw new IllegalArgumentException("Nebyla nalezena žádná obec s názvem: " + key);

    }

    /**
    * Vytvoří iterátor pro průchod tabulkou podle zadaného typu prohledávání (HLOUBKA nebo SIRKA).
    * 
    * @param typ Typ prohledávání (HLOUBKA nebo SIRKA).
    * @return Iterátor pro procházení tabulky.
    */
    @Override
    public Iterator vytvorIterator(eTypProhl typ) {
        if (typ == eTypProhl.HLOUBKA) {
            return new IteratorHloubka(koren);
        } else if (typ == eTypProhl.SIRKA) {
            return new IteratorSirka(koren);
        }

        return null;
    }

    /**
    * Iterátor pro procházení tabulkou metodou prohledávání do hloubky (DFS).
    */
    private class IteratorHloubka implements Iterator<V> {

        private final IAbstrLifo<Prvek> zasobnik = new AbstrLifo<>(); // Zásobník pro DFS

        public IteratorHloubka(Prvek koren) {
            vlozDoZasobniku(koren);
        }
        
        /**
        * Vloží všechny levé podstromy do zásobníku.
        * 
        * @param prvek Aktuální prvek.
        */
        private void vlozDoZasobniku(Prvek prvek) {
            while (prvek != null) {
                zasobnik.vloz(prvek);
                prvek = prvek.leva;
            }
        }

        /**
        * Zkontroluje, zda existuje další prvek pro iteraci.
        * 
        * @return True pokud existuje další prvek, jinak false.
        */
        @Override
        public boolean hasNext() {
            return !zasobnik.jePrazdny();
        }

        /**
        * Vrátí další prvek podle principu prohledávání do hloubky.
        * 
        * @return Hodnota následujícího prvku.
        * @throws NoSuchElementException pokud již není žádný další prvek.
        */
        @Override
        public V next() {
            if (!hasNext()) {
                throw new NoSuchElementException();
            }

            Prvek aktualni = zasobnik.odeber();

            if (aktualni.prava != null) {
                vlozDoZasobniku(aktualni.prava);
            }

            return aktualni.value;
        }
    }

    /**
    * Iterátor pro procházení tabulkou metodou prohledávání do šířky (BFS).
    */
    private class IteratorSirka implements Iterator<V> {

        private final IAbstrFifo<Prvek> fronta = new AbstrFifo<>(); // Fronta pro BFS

        public IteratorSirka(Prvek koren) {
            if (koren != null) {
                fronta.vloz(koren);
            }
        }

        /**
        * Zkontroluje, zda existuje další prvek pro iteraci.
        * 
        * @return True pokud existuje další prvek, jinak false.
        */
        @Override
        public boolean hasNext() {
            return !fronta.jePrazdny();
        }

        /**
        * Vrátí další prvek podle principu prohledávání do šířky.
        * 
        * @return Hodnota následujícího prvku.
        * @throws NoSuchElementException pokud již není žádný další prvek.
        */
        @Override
        public V next() {
            if (!hasNext()) {
                throw new NoSuchElementException();
            }

            Prvek aktualni = fronta.odeber();
            if (aktualni.leva != null) {
                fronta.vloz(aktualni.leva);
            }
            if (aktualni.prava != null) {
                fronta.vloz(aktualni.prava);
            }

            return aktualni.value;
        }
    }

}
