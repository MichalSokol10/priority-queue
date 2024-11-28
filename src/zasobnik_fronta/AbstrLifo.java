package zasobnik_fronta;


import seznam.AbstrDoubleList;
import rozhrani.IAbstrDoubleList;
import rozhrani.IAbstrLifo;
import java.util.Iterator;

/**
 * Třída implementující zásobník (LIFO - Last In, First Out) pomocí abstraktního obousměrného spojového seznamu (AbstrDoubleList).
 * 
 * Tato třída využívá instanci {@link AbstrDoubleList} pro správu dat v zásobníku, což umožňuje efektivní vkládání prvků
 * na začátek seznamu (pro vkládání do zásobníku) a odebírání prvků z začátku seznamu (pro odebírání ze zásobníku).
 * 
 * @param <T> Typ prvků, které se budou vkládat do zásobníku.
 */
public class AbstrLifo<T> implements IAbstrLifo<T> {
    
    private final IAbstrDoubleList<T> zasobnik = new AbstrDoubleList<>();

    /**
    * Smaže všechny prvky v zásobníku.
    */
    @Override
    public void zrus() {
        zasobnik.zrus();
    }
    
    /**
    * Zkontroluje, zda je zásobník prázdný.
    * 
    * @return True pokud je zásobník prázdný, jinak false.
    */
    @Override
    public boolean jePrazdny() {
        return zasobnik.jePrazdny();
    }

    /**
    * Vloží nový prvek na začátek zásobníku.
    * 
    * @param data Prvek, který se má vložit do zásobníku.
    */
    @Override
    public void vloz(T data) {
        zasobnik.vlozPrvni(data);
    }

    /**
    * Odebere prvek ze začátku zásobníku a vrátí jeho hodnotu.
    * 
    * @return Odebraný prvek.
    * @throws NullPointerException pokud je zásobník prázdný.
    */
    @Override
    public T odeber() {
        if (jePrazdny()) {
            throw new NullPointerException("Zásobník je prázdný!");
        }
        
        return zasobnik.odeberPrvni();
    }

    /**
    * Vytvoří iterátor pro průchod zásobníkem.
    * 
    * @return Iterátor pro procházení prvků v zásobníku.
    */
    @Override
    public Iterator vytvorIterator() {
        return new AbstrLifoIterator();
    }
    
    /**
    * Vnitřní třída pro implementaci iterátoru zásobníku.
    */
    private class AbstrLifoIterator implements Iterator<T> {
        private final Iterator<T> iterator = zasobnik.iterator();

        /**
        * Zkontroluje, zda jsou v zásobníku další prvky.
        * 
        * @return True pokud jsou v zásobníku další prvky, jinak false.
        */
        @Override
        public boolean hasNext() {
            return iterator.hasNext();
        }
        
        /**
        * Vrátí další prvek v zásobníku.
        * 
        * @return Další prvek v zásobníku.
        */
        @Override
        public T next() {
            return iterator.next();
        }
    }
    
}
