package rozhrani;


import data.Obec;
import java.util.Iterator;


public interface IAgendaKraj {

    void importDat(String nazevSouboru);

    Obec najdi(String nazevObce);

    void vloz(String nazevObce, Obec obec);

    Obec odeber(String nazevObce);

    Iterator<Obec> vytvorIterator();

    Obec generuj();
    
    void zrus();
}
