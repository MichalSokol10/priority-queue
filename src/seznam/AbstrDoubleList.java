package seznam;



import rozhrani.IAbstrDoubleList;
import java.io.Serializable;
import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * Implementace abstraktního obousměrného spojového seznamu.
 * Třída poskytuje operace pro manipulaci s prvky seznamu jako jsou přidávání, odebírání, přístup k prvkům a iterace.
 * 
 * @param <T> Typ dat uložených v seznamu
 */
public class AbstrDoubleList<T> implements IAbstrDoubleList<T>, Serializable {
    
    private Prvek<T> prvni; // První prvek seznamu
    private Prvek<T> posledni; // Poslední prvek seznamu
    private Prvek<T> aktualni; // Aktuální prvek
    private int velikost; // Počet prvků v seznamu
    
    /**
    * Vnitřní třída reprezentující prvek seznamu.
    * Každý prvek obsahuje odkaz na předchůdce, následovníka a data.
    */
    private class Prvek<T> {
        private Prvek<T> predchudce; // Odkaz na předchůdce
        private Prvek<T> naslednik; // Odkaz na následovníka
        private final T data; // Data uložená v prvku

        public Prvek(T data) {
            this.predchudce = null;
            this.naslednik = null;
            this.data = data;
        }
    }

    /**
    * Vyprázdní seznam, vynuluje odkazy na jednotlivé prvky, vynuluje velikost.
    */
    @Override
    public void zrus() {
        prvni = null;
        posledni = null;
        aktualni = null;
        velikost = 0;
    }

    /**
    * Zkontroluje, zda je seznam prázdný.
    * 
    * @return True, pokud je seznam prázdný, jinak false.
    */
    @Override
    public boolean jePrazdny() {
        return velikost == 0;
    }

    /**
    * Vloží nový prvek na začátek seznamu.
    * 
    * @param data Data, která mají být uložena v novém prvku.
    */
    @Override
    public void vlozPrvni(T data) {
        Prvek prvek = new Prvek(data);
        
        if (jePrazdny()) {
            prvni = prvek;
            posledni = prvek;
        } else {
            prvni.predchudce = prvek;
            prvek.naslednik = prvni;
            prvni = prvek;
        }
        
        this.velikost++;
    }

    /**
    * Vloží nový prvek na konec seznamu.
    * 
    * @param data Data, která mají být uložena v novém prvku.
    */
    @Override
    public void vlozPosledni(T data) {
        Prvek prvek = new Prvek(data);
        
        if (jePrazdny()) {
            prvni = prvek;
        } else {
            posledni.naslednik = prvek;
            prvek.predchudce = posledni;
        }
        
        posledni = prvek;
        
        this.velikost++;
    }
    
    /**
    * Vloží nový prvek za aktuální prvek.
    * 
    * @param data Data, která mají být uložena v novém prvku.
    * @throws NullPointerException pokud je seznam prázdný, nebo nebyl zvolen aktuální prvek.
    */
    @Override
    public void vlozNaslednika(T data) {
        Prvek prvek = new Prvek(data);
        
        if (jePrazdny()) {
            throw new NullPointerException("Seznam je prázdný.");
        }
        
        if (aktualni == null) {
            throw new NullPointerException("Nebyl zvolen aktuální prvek.");
        }
        
        if (aktualni == posledni) {
            posledni.naslednik = prvek;
            prvek.predchudce = posledni;
            posledni = prvek;
        } else {
            prvek.naslednik = aktualni.naslednik;
            prvek.predchudce = aktualni;
            aktualni.naslednik = prvek;
        }
        
        this.velikost++;
    }

    /**
    * Vloží nový prvek před aktuální prvek.
    * 
    * @param data Data, která mají být uložena v novém prvku.
    * @throws NullPointerException pokud je seznam prázdný, nebo nebyl zvolen aktuální prvek.
    */
    @Override
    public void vlozPredchudce(T data) {
        Prvek prvek = new Prvek(data);
        
        if (jePrazdny()) {
            throw new NullPointerException("Seznam je prázdný.");
        }
        
        if (aktualni == null) {
            throw new NullPointerException("Nebyl zvolen aktuální prvek.");
        }
        
        if (aktualni == prvni) {
            prvni.predchudce = prvek;
            prvek.naslednik = prvni;
            prvni = prvek;
        } else {
            aktualni.predchudce.naslednik = prvek;
            prvek.predchudce = aktualni.predchudce;
            prvek.naslednik = aktualni;
            aktualni.predchudce = prvek;
        }
        
        this.velikost++;
    }

    /**
    * Vrátí data aktuálního prvku.
    * 
    * @return Data aktuálního prvku.
    * @throws NullPointerException pokud je seznam prázdný, nebo pokud není zvolen aktuální prvek.
    */
    @Override
    public T zpristupniAktualni() {
        if (jePrazdny()) {
            throw new NullPointerException("Seznam je prázdný");
        } else if (aktualni == null) {
            throw new NullPointerException("Žádný prvek není aktuální.");
        } else {
            return aktualni.data;
        }
    }

    /**
    * Nastaví první prvek seznamu jako aktuální a vrátí jeho data.
    * 
    * @return Data prvního prvku.
    * @throws NullPointerException pokud je seznam prázdný.
    */
    @Override
    public T zpristupniPrvni() {
        if (jePrazdny()) {
            throw new NullPointerException("Seznam je prázdný");
        } 
        
        aktualni = prvni;
        
        return aktualni.data;
    }

    /**
    * Nastaví poslední prvek seznamu jako aktuální a vrátí jeho data.
    * 
    * @return Data posledního prvku.
    * @throws NullPointerException pokud je seznam prázdný.
    */
    @Override
    public T zpristupniPosledni() {
        if (jePrazdny()) {
            throw new NullPointerException("Seznam je prázdný");
        } 
        
        aktualni = posledni;
        
        return aktualni.data;
    }

    /**
    * Vrátí data následujícího prvku a nastaví ho jako aktuální.
    * 
    * @return Data následujícího prvku.
    * @throws NullPointerException pokud je seznam prázdný, pokud není zvolen aktuální prvek, nebo pokud je aktuální prvek poslední.
    */
    @Override
    public T zpristupniNaslednika() {
        if (jePrazdny()) {
            throw new NullPointerException("Seznam je prázdný");
        } else if (aktualni == posledni) {
            throw new NullPointerException("Tento prvek je poslední ve vašem seznamu.");
        } else if (aktualni == null) {
            throw new NullPointerException("Žádný prvek není aktuální.");
        } else {
            aktualni = aktualni.naslednik;
            return aktualni.data;
        }
    }

    /**
    * Vrátí data předchozího prvku a nastaví ho jako aktuální.
    * 
    * @return Data předchozího prvku.
    * @throws NullPointerException pokud je seznam prázdný, pokud není zvolen aktuální prvek, nebo pokud je aktuální prvek první.
    */
    @Override
    public T zpristupniPredchudce() {
        if (jePrazdny()) {
            throw new NullPointerException("Seznam je prázdný");
        } else if (aktualni == prvni) {
            throw new NullPointerException("Tento prvek je první ve vašem seznamu.");
        } else if (aktualni == null) {
            throw new NullPointerException("Žádný prvek není aktuální.");
        } else {
            aktualni = aktualni.predchudce;
            return aktualni.data;
        }
    }

    /**
    * Odebere aktuální prvek a vrátí jeho data.
    * 
    * @return Data odebraného prvku.
    * @throws NullPointerException pokud je seznam prázdný, nebo pokud není zvolen aktuální prvek.
    */
    @Override
    public T odeberAktualni() {
        if (jePrazdny()) {
            throw new NullPointerException("Seznam je prázdný.");
        } else if (aktualni == null) {
            throw new NullPointerException("Žádný prvek není aktuální");
        } else if (aktualni == prvni) {
            return odeberPrvni();
        } else if (aktualni == posledni) {
            return odeberPosledni();
        } else {
            Prvek<T> temp = aktualni;
            aktualni.predchudce.naslednik = aktualni.naslednik;
            aktualni.naslednik.predchudce = aktualni.predchudce;
            aktualni = prvni;
            
            return temp.data;
        }
    }
    
    /**
    * Odebere první prvek seznamu a vrátí jeho data.
    * 
    * @return Data odebraného prvku.
    * @throws NullPointerException pokud je seznam prázdný.
    */
    @Override
    public T odeberPrvni() {
        if (jePrazdny()) {
            throw new NullPointerException("Seznam je prázdný.");
        } else if (prvni == posledni) {
            Prvek<T> temp = prvni;
            prvni = null;
            posledni = null;
            aktualni = null;
            velikost = 0;
            
            return temp.data;
        } else {
            Prvek<T> temp = prvni;
            prvni = prvni.naslednik;
            prvni.predchudce = null;
            
            if (aktualni == temp) {
                aktualni = prvni;
            }
            
            velikost--;
            return temp.data;
        }
    }

    /**
    * Odebere poslední prvek seznamu a vrátí jeho data.
    * 
    * @return Data odebraného prvku.
    * @throws NullPointerException pokud je seznam prázdný.
    */
    @Override
    public T odeberPosledni() {
        if (jePrazdny()) {
            throw new NullPointerException("Seznam je prázdný.");
        } else if (prvni == posledni) {
            return odeberPrvni();
        } else {
            Prvek<T> temp = posledni;
            posledni.predchudce.naslednik = null;
            posledni = posledni.predchudce;
           
            if (aktualni == temp) {
                aktualni = prvni;
            }
            
            this.velikost--;
            return temp.data;
        }
    }

    /**
    * Odebere následující prvek k aktuálnímu a vrátí jeho data.
    * 
    * @return Data odebraného následujícího prvku.
    * @throws NullPointerException pokud je seznam prázdný, není zvolen aktuální prvek, nebo pokud je aktuální prvek poslední v seznamu.
    */
    @Override
    public T odeberNaslednika() {
        if (jePrazdny()) {
            throw new NullPointerException("Seznam je prázdný.");
        } else if (aktualni == null) {
            throw new NullPointerException("Žádný prvek není aktuální.");
        } else if (aktualni.naslednik == posledni) {
            return odeberPosledni();
        } else if (aktualni == posledni) {
            throw new NullPointerException("Tento prvek je poslední v seznamu.");
        } else {
            Prvek<T> temp = aktualni.naslednik;
            
            aktualni.naslednik.naslednik.predchudce = aktualni;
            aktualni.naslednik = aktualni.naslednik.naslednik;
            
            this.velikost--;
            return temp.data;
        }
    }

    /**
    * Odebere předchozí prvek k aktuálnímu a vrátí jeho data.
    * 
    * @return Data odebraného předchozího prvku.
    * @throws NullPointerException pokud je seznam prázdný, není zvolen aktuální prvek, nebo pokud je aktuální prvek první v seznamu.
    */
    @Override
    public T odeberPredchudce() {
        if (jePrazdny()) {
            throw new NullPointerException("Seznam je prázdný.");
        } else if (aktualni == null) {
            throw new NullPointerException("Žádný prvek není aktuální.");
        } else if (aktualni == prvni) {
            throw new NullPointerException("Tento prvek je první v seznamu.");
        } else if (aktualni.predchudce == prvni) {
            return odeberPrvni();
        } else {
            Prvek<T> temp = aktualni.predchudce;
            
            aktualni.predchudce.predchudce.naslednik = aktualni;
            aktualni.predchudce = aktualni.predchudce.predchudce;
            
            this.velikost--;
            return temp.data;
        }
    }
    
    /**
    * Vnitřní třída implementující iterátor pro tento seznam.
    * Umožňuje procházet seznam v pořadí od prvního do posledního prvku.
    */
    private class AbstrDoubleListIterator implements Iterator<T> {
        private Prvek<T> aktualni = prvni; // Ukazatel na aktuální prvek seznamu

        /**
        * Kontroluje, zda existuje další prvek v seznamu.
        * 
        * @return True pokud existuje další prvek, jinak false.
        */
        @Override
        public boolean hasNext() {
            return aktualni != null;
        }
        
        /**
        * Vrací data následujícího prvku v seznamu a posune ukazatel na další prvek.
        * 
        * @return Data následujícího prvku.
        * @throws NoSuchElementException pokud není další prvek v seznamu.
        */
        @Override
        public T next() {
            if (!hasNext()) {
                throw new NoSuchElementException();
            } else {
                T data = aktualni.data;
                aktualni = aktualni.naslednik;
                
                return data;
            }
        }
        
    }
    
    /**
    * Vrací nový iterátor pro tento seznam, který umožňuje iteraci přes prvky v pořadí od prvního do posledního.
    * 
    * @return Nový iterátor pro seznam.
    */
    @Override
    public Iterator<T> iterator() {
        return new AbstrDoubleListIterator();
    }   
}
