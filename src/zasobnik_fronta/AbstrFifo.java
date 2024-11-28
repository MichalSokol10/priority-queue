package zasobnik_fronta;


import seznam.AbstrDoubleList;
import rozhrani.IAbstrDoubleList;
import rozhrani.IAbstrFifo;
import java.util.Iterator;

/**
* Třída implementující frontu (FIFO) pomocí abstraktního obousměrného spojového seznamu (AbstrDoubleList).
* 
* Tato třída využívá instanci {@link AbstrDoubleList} pro správu dat v frontě, což umožňuje efektivní vkládání prvků
* na začátek seznamu (pro vkládání do fronty) a odebírání prvků z konce seznamu (pro odebírání z fronty).
* 
* @param <T> Typ prvků, které se budou vkládat do fronty.
*/
public class AbstrFifo<T> implements IAbstrFifo<T> {
    
    private final IAbstrDoubleList<T> fronta = new AbstrDoubleList<>();

    /**
    * Smaže všechny prvky ve frontě.
    */
    @Override
    public void zrus() {
        fronta.zrus();
    }

    /**
    * Zkontroluje, zda je fronta prázdná.
    * 
    * @return True pokud je fronta prázdná, jinak false.
    */
    @Override
    public boolean jePrazdny() {
        return fronta.jePrazdny();
    }

    /**
    * Vloží nový prvek na začátek fronty.
    * 
    * @param data Prvek, který se má vložit do fronty.
    */
    @Override
    public void vloz(T data) {
        if (data != null) {
            fronta.vlozPrvni(data);
        }
    }

    /**
    * Odebere prvek z konce fronty a vrátí jeho hodnotu.
    * 
    * @return Odebraný prvek.
    * @throws NullPointerException pokud je fronta prázdná.
    */
    @Override
    public T odeber() {
        if (!jePrazdny()) {
            return fronta.odeberPosledni();
        }
        
        throw new NullPointerException("Fronta je prázdná!");
    }
    
    /**
    * Vytvoří iterátor pro průchod frontou.
    * 
    * @return Iterátor pro procházení prvků ve frontě.
    */
    @Override
    public Iterator vytvorIterator() {
        return new AbstrFifoIterator();
    }
    
    /**
    * Vnitřní třída pro implementaci iterátoru fronty.
    */
    private class AbstrFifoIterator implements Iterator<T> {
        private final Iterator<T> iterator = fronta.iterator();

        /**
        * Zkontroluje, zda jsou ve frontě další prvky.
        * 
        * @return True pokud jsou ve frontě další prvky, jinak false.
        */
        @Override
        public boolean hasNext() {
            return iterator.hasNext();
        }

        /**
        * Vrátí další prvek ve frontě.
        * 
        * @return Další prvek ve frontě.
        */
        @Override
        public T next() {
            return iterator.next();
        }
    }
    
}
